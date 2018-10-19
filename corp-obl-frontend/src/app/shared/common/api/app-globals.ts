import {IMyDpOptions} from 'mydatepicker';

export class AppGlobals {

    public static emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$';
    public static dateFormat = 'dd/mm/yyyy';

    public static myDatePickerOptions: IMyDpOptions = {
        // other options...
        dateFormat: AppGlobals.dateFormat,
    };

    public static convertDateToDatePicker(date): any {

        if (!date) {
            return;
        }
        const dateConverted: Object = {
            date:
                {
                    year: date.getFullYear(),
                    month: date.getMonth(),
                    day: date.getDate()
                }
        };
        return dateConverted;
    }

    public static convertDatePickerToDate(date): any {

        if (!date) {
            return;
        }

        return date.day.toString() + '/' + date.month.toString() + '/' + date.year.toString();
    }
}
