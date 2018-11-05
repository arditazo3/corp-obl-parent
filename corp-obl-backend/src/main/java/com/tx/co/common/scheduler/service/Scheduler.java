package com.tx.co.common.scheduler.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.common.constants.AppConstants;
import com.tx.co.common.mail.service.EmailService;
import com.tx.co.common.utils.UtilDate;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.service.IExpirationService;
import com.tx.co.user.domain.User;
import com.tx.co.user.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class with the alghoritms that check the tasks and create the expirations
 *
 * @author rfratti
 */
@Service
public class Scheduler {

    private static final String PATTERN_INT_DATE = AppConstants.PATTERN_INT_DATE;
    private static final String PERIODICITY_WEEKLY = AppConstants.REC_WEEKLY;
    private static final String PERIODICITY_MONTHLY = AppConstants.REC_MONTHLY;
    private static final String PERIODICITY_YEARLY = AppConstants.REC_YEARLY;
    private static final String EXPIRATIONTYPE_FIX_DAY = AppConstants.EXP_FIX_DAY;
    private static final String EXPIRATIONTYPE_MONTH_START = AppConstants.EXP_MONTH_START;
    private static final String EXPIRATIONTYPE_MONTH_END = AppConstants.EXP_MONTH_END;
    private static final int EXPIRATION_CLOSABLEBY_ONE = AppConstants.EXP_CLOSABLEBY_ONE;
    private static final int EXPIRATION_CLOSABLEBY_ALL = AppConstants.EXP_CLOSABLEBY_ALL;
    private static final int DAY = UtilDate.DAY;
    private static final int MONTH = UtilDate.MONTH;

    private ITaskService taskService;
    private IExpirationService expirationService;
    private IUserService userService;
    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private Map<String, User> userMap;
    //
    @Value("${scheduler.maxAttempts}")
    private int maxAttempts = 20;

    @Autowired
    public void setTaskService(ITaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setExpirationService(IExpirationService expirationService) {
        this.expirationService = expirationService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public Scheduler() {

    }

    /**
     * Execute scheduler
     */
    public void execute() {
        this.init();
        this.elaborateTasksAndSendNotification();
    }

    /**
     * Elaborate task
     * @param task
     */
    public void elaborateTask(final Task task) {
        this.init();
        this.createTaskExpirations(task);
        this.removeTaskExpirations(task);
    }


    private void init() {
        if (null == userMap) {
            userMap = new LinkedHashMap<>();
            final List<User> userList = userService.findAllUsers();
            if (!CollectionUtils.isEmpty(userList)) {
                userMap.putAll(userList.stream().collect(
                        Collectors.toMap(x -> x.getUsername(), x -> x)));
            }
        }
    }

    private void elaborateTasksAndSendNotification() {
        // List of task to check
        final List<Task> taskList = taskService.getAllTasksForScheduler();
        //
        for (final Task task : taskList) {
            // Create expiration
            this.createTaskExpirations(task);
            // Send notification
            this.sendTaskNotifications(task);
        }
    }

    private void sendTaskNotifications(final Task task) {
        int iToday = -1;
        try {
            // Today
            iToday = UtilDate.getDateAsInt(new Date(), PATTERN_INT_DATE);
        } catch (Exception e) {
            logger.error("Error on creating task extensions.Task: " + task.getIdTask() + "\nException: " + e, e);
        }
        if (iToday > -1) {
            // Task expirations
            final Set<Expiration> expirationList = task.getExpirations();
            for (final Expiration expiration : expirationList) {
                int iExpirationDate = -1;
                try {
                    iExpirationDate = UtilDate.getDateAsInt(expiration.getExpirationDate(), PATTERN_INT_DATE);
                } catch (Exception e) {
                    logger.error("Error on getting expiration date as int.\nException: " + e, e);
                }
                if (iExpirationDate > 0 && null == expiration.getCompleted() && null == expiration.getRegistered()) {
                    final boolean sendFirstNotification = this.checkFirstNotificationValidity(iExpirationDate, iToday, task.getDaysOfNotice());
                    final boolean sendNextNotification = checkNextNotificationValidity(iExpirationDate, iToday, task.getFrequenceOfNotice());
                    // Check if send first notification to user
                    if (sendFirstNotification || sendNextNotification) {
                        // Init office related to expiration
                        final Office expirationOffice = this.getTaskOffice(task, expiration.getOffice().getIdOffice());
                        if (null != expirationOffice) {
                            // Providers
                            final List<User> providerList = expirationOffice.getUserProviders();
                            final String providerEmails = sendFirstNotification
                                    // First Notification: just to beneficiaries
                                    ? ""
                                    // Next Notification: to beneficiaries and to providers
                                    : providerList.stream().map(User::getEmail).collect(Collectors.joining(","));
                            // Check if send everybody could close the expiration or just a user
                            if (expiration.getExpirationClosableBy() == EXPIRATION_CLOSABLEBY_ALL) {
                                // email to all user
                                final List<User> beneficiaryList = expirationOffice.getUserBeneficiaries();
                                final String beneficiaryEmails = beneficiaryList.stream()
                                        .map(User::getEmail)
                                        .collect(Collectors.joining(","));
                                EmailService emailService = new EmailService();
                                emailService.sendSimpleMessage(beneficiaryEmails, providerEmails, "", "Scadenza task ", "E' scaduto il task");
                            } else if (expiration.getExpirationClosableBy() == EXPIRATION_CLOSABLEBY_ONE) {
                                // email just to user
                                final User user = userMap.get(expiration.getUsername());
                                if (null != user) {
                                    EmailService emailService = new EmailService();
                                    final String email = user.getEmail();
                                    emailService.sendSimpleMessage(email, providerEmails, "", "Scadenza task ", "E' scaduto il task");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createTaskExpirations(final Task task) {
        // Get list of expirations
        final List<Integer> iExpirationDateList = this.getNextExpirationsDate(task);
        // Create extensions
        this.createTaskExpirations(task, iExpirationDateList);
    }

    private void removeTaskExpirations(final Task task) {
        final Iterable<Expiration> expirationList = expirationService.getSchedulerRemovableExpirationList(task.getIdTask());
        if (!ObjectUtils.isEmpty(expirationList)) {
            for (final Expiration expiration : expirationList) {
                expiration.setEnabled(false);
            }

            final boolean result = expirationService.updateExpirationList(expirationList);
        }
    }

    /**
     * Create task extensions
     *
     * @param task                task to elaborate
     * @param iExpirationDateList list of expiration dates (to check)
     */
    private void createTaskExpirations(final Task task, final List<Integer> iExpirationDateList) {
        int iToday = -1;
        try {
            // Today
            iToday = UtilDate.getDateAsInt(new Date(), PATTERN_INT_DATE);
        } catch (Exception e) {
            logger.error("Error on creating task extensions.Task: " + task.getIdTask() + "\nException: " + e, e);
        }
        // Task expirations
        final Set<Expiration> expirationList = task.getExpirations();
        // Expiration closable by
        final int expirationClosableBy = task.getTaskTemplate().getExpirationClosableBy();
        // Map of expiration; key=yearMonthDay
        Map<String, Expiration> expirationMap = new LinkedHashMap<>();
        for (final Expiration expiration : expirationList) {
            int iExpirationDate = -1;
            try {
                iExpirationDate = UtilDate.getDateAsInt(expiration.getExpirationDate(), PATTERN_INT_DATE);
            } catch (Exception e) {
                logger.error("Error on getting expiration date as int.\nException: " + e, e);
            }
            if (iExpirationDate > 0) {
                String key = expirationClosableBy == 1
                        // Anyone
                        ? "" + iExpirationDate + "_" + expiration.getOffice().getIdOffice()
                        // One for user: key = expirationDate _ username
                        : iExpirationDate + "_" + expiration.getOffice().getIdOffice() + "_" + expiration.getUsername();
                expirationMap.put(key, expiration);
            }
        }

        // Check each expirationDate
        for (final Integer iExpirationDate : iExpirationDateList) {
            // Check number 1
            if (checkExpirationValidity(iExpirationDate, iToday, task.getDaysBeforeShowExpiration())) {
                // Check all office related to task
                final List<Office> officeList = this.getTaskOfficeList(task);
                // Check each office
                for (final Office office : officeList) {
                    final String searchingKey = iExpirationDate + "_" + office.getIdOffice();
                    if (expirationClosableBy == EXPIRATION_CLOSABLEBY_ALL) {
                        Expiration expiration = expirationMap.get(searchingKey);
                        // If expiration not exist: create
                        if (null == expiration) {
                            // create expiration generic
                            expiration = this.createExpiration(task, office, iExpirationDate, expirationClosableBy, "");
                            if (null != expiration) {
                                expirationService.saveUpdateExpiration(expiration);
                            }
                        } else if(!expiration.getEnabled()) {
                            expiration.setEnabled(true);
                            expiration.setModificationDate(new Date());
                            //expiration.setModifiedBy();
                            expirationService.saveUpdateExpiration(expiration);
                        }
                    } else if (expirationClosableBy == EXPIRATION_CLOSABLEBY_ONE &&
                    		!isEmpty(office.getUserBeneficiaries())) {
                    	// Check each user
                    	for (final User beneficiary : office.getUserBeneficiaries()) {
                    		// For each user if expiration not exist: create
                    		Expiration expiration = expirationMap.get(searchingKey + "_" + beneficiary.getUsername());
                    		if (null == expiration) {
                    			// create expiration for each user
                    			expiration = this.createExpiration(task, office, iExpirationDate, expirationClosableBy, beneficiary.getUsername());
                    			if (null != expiration) {
                    				expirationService.saveUpdateExpiration(expiration);
                    			}
                    		} else if(!expiration.getEnabled()) {
                    			expiration.setEnabled(true);
                    			expiration.setModificationDate(new Date());
                    			//expiration.setModifiedBy();
                    			expirationService.saveUpdateExpiration(expiration);
                    		}
                    	}
                    }
                }
            }
        }
    }

    private Expiration createExpiration(final Task task, final Office office, final int iExpirationDate, final int expirationClosableBy, final String userName) {
        try {
            final Expiration expiration = new Expiration();
            expiration.setExpirationDate(UtilDate.getIntDateAsDate(iExpirationDate, PATTERN_INT_DATE));
            expiration.setTask(task);
            expiration.setTaskTemplate(task.getTaskTemplate());
            expiration.setOffice(office);
            expiration.setUsername(userName);
            expiration.setExpirationClosableBy(expirationClosableBy);
            return expiration;
        } catch (Exception e) {
            logger.error("Error on creating Expiration.Task: " + task.getIdTask() + "\nException: " + e, e);
        }
        return null;
    }

    /**
     * (data scadenza - data di oggi) compresa tra 0 ed il numero di giorni decorrenza
     * (expirationDate - today) between 0 and daysBeforeShowExpiration
     *
     * @param iExpirationDate
     * @param daysBeforeShowExpiration
     * @return true if task satisfy first check
     */
    private boolean checkExpirationValidity(final int iExpirationDate, final int today, final int daysBeforeShowExpiration) {
        try {
            final int diff =Math.toIntExact(Math.round(
                    UtilDate.dateDiff(UtilDate.getIntDateAsDate(iExpirationDate, PATTERN_INT_DATE),
                    UtilDate.getIntDateAsDate(today, PATTERN_INT_DATE), DAY)));
            return diff > 0 && diff <= daysBeforeShowExpiration;
        } catch (Exception e) {
            logger.error("Error on checking expiration validity. Expiration date: " + iExpirationDate + "\nException: " + e, e);
        }
        return false;
    }

    /**
     * (data scadenza - data di oggi) compresa tra 0 ed il numero di giorni di preavviso alert
     * (expirationDate - today) between 0 and daysOfNotice
     *
     * @param iExpirationDate
     * @param daysBeforeSendNotification
     * @return true if send first notification
     */
    private boolean checkFirstNotificationValidity(final int iExpirationDate, final int today, final int daysBeforeSendNotification) {
        try {
            final int diff = Math.toIntExact(Math.round(
                    UtilDate.dateDiff(UtilDate.getIntDateAsDate(iExpirationDate, PATTERN_INT_DATE),
                    UtilDate.getIntDateAsDate(today, PATTERN_INT_DATE), DAY)));
            return diff > 0 && diff <= daysBeforeSendNotification;
        } catch (Exception e) {
            logger.error("Error on checking notification validity. Expiration date: " + iExpirationDate + "\nException: " + e, e);
        }
        return false;
    }

    /**
     * (data scadenza - data di oggi) = -1 oppure (data scadenza + (giorni frequenza alert * N) = data di oggi”
     * dove N è il numero di tentativi di invio dell’alert (va da 1 a N). Quindi un esempio di formula potrebbe essere:
     * (13/08/2018 - 14/08/2018) = -1
     * || (13/08/2018 + 7 * 1) = 14/08/2018
     * || (13/08/2018 + 7 * 2) = 14/08/2018
     * || (13/08/2018 + 7 * 3) = 14/08/2018
     * …………………..
     * || (13/08/2018 + 7 * 20) = 14/08/2018
     * (expirationDate - today) between 0 and frequenceOfNotice
     *
     * @param iExpirationDate
     * @param frequenceOfNotice
     * @return true if send first notification
     */
    private boolean checkNextNotificationValidity(final int iExpirationDate, final int today, final int frequenceOfNotice) {
        try {
            final int diff = Math.toIntExact(Math.round(UtilDate.dateDiff(UtilDate.getIntDateAsDate(iExpirationDate, PATTERN_INT_DATE),
                    UtilDate.getIntDateAsDate(today, PATTERN_INT_DATE), DAY)));
            // Check if expired by one day
            if (diff >= -1 && diff < 0) {
                return true;
            }
            int attempts = 1;
            while (attempts < maxAttempts) {
                // Get next date when system have to send a notification
                final Date nextDateOfNotice = UtilDate.postpone(UtilDate.getIntDateAsDate(iExpirationDate, PATTERN_INT_DATE), DAY, (attempts * frequenceOfNotice));
                final int iNextDate = UtilDate.getDateAsInt(nextDateOfNotice, PATTERN_INT_DATE);
                // if next date == today: send notification
                if (iNextDate == today) {
                    return true;
                }
                // If next date is greater than today: no notification to send
                else if (iNextDate > today) {
                    return false;
                }
                attempts++;
            }
        } catch (Exception e) {
            logger.error("Error on checking notification validity. Expiration date: " + iExpirationDate + "\nException: " + e, e);
        }
        return false;
    }


    private List<Integer> getNextExpirationsDate(final Task task) {
        List<Integer> result = new LinkedList<>();
        try {
            // Today
            final int iToday = UtilDate.getDateAsInt(new Date(), PATTERN_INT_DATE);
            final String expirationType = task.getExpirationType();
            final String recurrence = task.getRecurrence();
            final int day = task.getDay();
            final int daysBeforeShowExpiration = task.getDaysBeforeShowExpiration();
            final int iCurrentYear = UtilDate.getYear();
            int maxDeadline = UtilDate.getDateAsInt(UtilDate.postpone(new Date(), DAY, daysBeforeShowExpiration), PATTERN_INT_DATE);
            int iExpirationDate = 0;
            Date currentDate = new Date();
            switch (recurrence) {
                // Yearly
                case PERIODICITY_YEARLY:
                    if ((day + "").length() == 8) {
                        // Calculate first expiration
                        final int monthDay = Integer.parseInt((day + "").substring(4));
                        iExpirationDate = iCurrentYear * 10000 + monthDay;
                        if (iExpirationDate > iToday) {
                            result.add(iExpirationDate);
                        }
                        // Next expiration
                        iExpirationDate = (iCurrentYear + 1) * 10000 + monthDay;
                        result.add(iExpirationDate);
                        // Next expiration
                        iExpirationDate = (iCurrentYear + 2) * 10000 + monthDay;
                        result.add(iExpirationDate);
                    }
                    break;
                case PERIODICITY_MONTHLY:
                    // Calculate max deadline in the future
                    switch (expirationType) {
                        //
                        case EXPIRATIONTYPE_MONTH_START:
                            iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth() * 100 + 1;
                            if (UtilDate.getDayOfMonth() == 1) {
                                result.add(iExpirationDate);
                            }
                            // Add all the expiration until the max deadline
                            while (iExpirationDate <= maxDeadline) {
                                currentDate = UtilDate.postpone(currentDate, MONTH, 1);
                                // First day of month
                                iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth(currentDate) * 100 + 1;
                                if (iExpirationDate <= maxDeadline) {
                                    result.add(iExpirationDate);
                                }
                            }
                            break;
                        case EXPIRATIONTYPE_MONTH_END:
                            iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth() * 100 + 1;
                            if (UtilDate.getDayOfMonth() == 1) {
                                result.add(iExpirationDate);
                            }
                            // Add all the expiration until the max deadline
                            while (iExpirationDate <= maxDeadline) {
                                currentDate = UtilDate.postpone(currentDate, MONTH, 1);
                                // Last day of month
                                iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth(currentDate) * 100 + UtilDate.getActualMaximumDayOfMonth(currentDate);
                                if (iExpirationDate <= maxDeadline) {
                                    result.add(iExpirationDate);
                                }
                            }
                            break;
                        case EXPIRATIONTYPE_FIX_DAY:
                            iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth() * 100 + day;
                            if (day >= UtilDate.getDayOfMonth()) {
                                result.add(iExpirationDate);
                            }
                            // Add all the expiration until the max deadline
                            while (iExpirationDate <= maxDeadline) {
                                currentDate = UtilDate.postpone(currentDate, MONTH, 1);
                                iExpirationDate = iCurrentYear * 10000 + UtilDate.getMonth(currentDate) * 100 + day;
                                if (iExpirationDate <= maxDeadline) {
                                    result.add(iExpirationDate);
                                }
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case PERIODICITY_WEEKLY:
                    currentDate = new Date();
                    if (UtilDate.getDayOfWeek(currentDate) == day) {
                        iExpirationDate = UtilDate.getDateAsInt(currentDate, PATTERN_INT_DATE);
                        result.add(iExpirationDate);
                    } else {
                        // Go to the correct weekly day
                        while (UtilDate.getDayOfWeek(currentDate) != day) {
                            currentDate = UtilDate.postpone(currentDate, DAY, 1);
                        }
                        iExpirationDate = UtilDate.getDateAsInt(currentDate, PATTERN_INT_DATE);
                        result.add(iExpirationDate);
                    }

                    // Add all the expiration until the max deadline
                    while (iExpirationDate <= maxDeadline) {
                        // Postpone of 7 days
                        currentDate = UtilDate.postpone(currentDate, DAY, 7);
                        iExpirationDate = UtilDate.getDateAsInt(currentDate, PATTERN_INT_DATE);
                        if (iExpirationDate <= maxDeadline) {
                            result.add(iExpirationDate);
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            logger.error("Error on retrieving next expiration dates. Task: " + task.getIdTask() + "\nException: " + e, e);
        }
        return result;

    }

    /**
     * Get Office beneficiaries
     *
     * @param task
     * @return list of Office with beneficiaries
     */
    private Office getTaskOffice(final Task task, final long officeId) {
        // Check all office related to task
        for (final TaskOffice taskOffice : task.getTaskOffices()) {
            final Office office = taskOffice.getOffice();
            if (office.getIdOffice() == officeId) {
                // Init office relations
                this.initOfficeRelations(office, taskOffice);
                return office;
            }
        }
        return null;
    }

    /**
     * Get Office beneficiaries
     *
     * @param task
     * @return list of Office with beneficiaries
     */
    private List<Office> getTaskOfficeList(final Task task) {
        // Check all office related to task
        final List<Office> officeList = new LinkedList<>();
        for (final TaskOffice taskOffice : task.getTaskOffices()) {
            final Office office = taskOffice.getOffice();
            if (!officeList.contains(office)) {
                // Add to office list
                officeList.add(office);
                // Init office relations
                this.initOfficeRelations(office, taskOffice);
            }
        }
        return officeList;
    }

    private void initOfficeRelations(final Office office, final TaskOffice taskOffice) {
        final Set<TaskOfficeRelations> taskOfficeRelations = taskOffice.getTaskOfficeRelations();
        if (!ObjectUtils.isEmpty(taskOfficeRelations)) {
            List<User> userProviders = new ArrayList<>();
            List<User> userBeneficiaries = new ArrayList<>();
            for (TaskOfficeRelations taskOfficeRelation : taskOfficeRelations) {
                if (taskOfficeRelation.getRelationType().compareTo(AppConstants.CONTROLLER) == 0) {
                    final User user = userMap.get(taskOfficeRelation.getUsername());
                    if (null != user) {
                        userProviders.add(user);
                    }
                } else if (taskOfficeRelation.getRelationType().compareTo(AppConstants.CONTROLLED) == 0) {
                    final User user = userMap.get(taskOfficeRelation.getUsername());
                    if (null != user) {
                        userBeneficiaries.add(user);
                    }

                }
            }
            office.setUserBeneficiaries(userBeneficiaries);
            office.setUserProviders(userProviders);
        }
    }
}
