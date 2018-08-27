# Creates the table: co_company

CREATE TABLE IF NOT EXISTS co_company
(
  id              INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  description     VARCHAR(100) NOT NULL
  COMMENT 'The description',
  enabled         TINYINT(1)   NOT NULL
  COMMENT 'Indicate the STATUS of company:Active OR NOT Active',
  creationdate    DATETIME     NOT NULL
  COMMENT 'DATE of creation',
  createdby       VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has inserted the record',
  modifcationdate DATETIME     NOT NULL,
  modifiedby      VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that made the LAST modification of the record'
)
  COMMENT 'The TABLE company';

# -------------------------------------- #

# Creates the table: co_companyconsultant

CREATE TABLE IF NOT EXISTS co_companyconsultant
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  company_id       INT          NOT NULL
  COMMENT 'FK of the TABLE Company',
  name             VARCHAR(255) NOT NULL
  COMMENT 'Company NAME / NAME ',
  email            VARCHAR(255) NOT NULL
  COMMENT 'The email',
  phone1           VARCHAR(30)  NULL
  COMMENT 'Phone NO .1',
  phone2           VARCHAR(30)  NULL
  COMMENT 'Phone NO .2',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_companyconsultant_co_company_id_fk
  FOREIGN KEY (company_id) REFERENCES co_company (id)
)
  COMMENT 'The TABLE Company Consultant';

CREATE INDEX co_companyconsultant_co_company_id_fk
  ON co_companyconsultant (company_id);

# -------------------------------------- #

# Creates the table: co_companytopic

CREATE TABLE IF NOT EXISTS co_companytopic
(
  id               INT AUTO_INCREMENT
  COMMENT 'The Pk of the TABLE '
  PRIMARY KEY,
  topic_id         INT          NOT NULL
  COMMENT 'FK of the TABLE Topic',
  company_id       INT          NOT NULL
  COMMENT 'FK of the TABLE Company',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_companytopic_co_company_id_fk
  FOREIGN KEY (company_id) REFERENCES co_company (id)
)
  COMMENT 'The TABLE of association BETWEEN Office AND Topic';

CREATE INDEX co_companytopic_co_company_id_fk
  ON co_companytopic (company_id);

CREATE INDEX co_companytopic_co_topic_id_fk
  ON co_companytopic (topic_id);

# -------------------------------------- #

# Creates the table: co_companyuser

CREATE TABLE IF NOT EXISTS co_companyuser
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  username         VARCHAR(100) NOT NULL
  COMMENT 'The FK of the TABLE CORPOBLIG_USER ',
  company_id       INT          NOT NULL
  COMMENT 'The FK of the TABLE Company',
  companyadmin     TINYINT(1)   NOT NULL
  COMMENT 'Indicates whether it IS a company administrator OR NOT ',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The creaation DATE of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has created the record',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of creation',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_companyuser_co_company_id_fk
  FOREIGN KEY (company_id) REFERENCES co_company (id)
)
  COMMENT 'The junction TABLE of the CORPOBLIG_USER AND Company';

CREATE INDEX co_companyuser_co_company_id_fk
  ON co_companyuser (company_id);

CREATE INDEX co_companyuser_co_user_username_fk
  ON co_companyuser (username);

# -------------------------------------- #

# Creates the table: co_expiration

CREATE TABLE IF NOT EXISTS co_expiration
(
  id                   INT AUTO_INCREMENT
    PRIMARY KEY,
  tasktemplate_id      INT          NOT NULL
  COMMENT 'FK of the TABLE Task Template',
  task_id              INT          NOT NULL
  COMMENT 'FK of the TABLE Task',
  office_id            INT          NOT NULL
  COMMENT 'FK of the TABLE Office',
  expirationclosableby INT(1)       NOT NULL
  COMMENT '1:Anyone can CLOSE the task, 2:EACH CORPOBLIG_USER has his task',
  username             VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER ',
  expirationdate       DATETIME     NOT NULL
  COMMENT 'The expired TIME ',
  completed            DATETIME     NOT NULL
  COMMENT 'DATE of WHEN it was declared executed ( BY the controlled)',
  approved             DATETIME     NOT NULL
  COMMENT 'DATE WHEN it was declared approved BY the controller',
  registered           DATETIME     NOT NULL
  COMMENT 'DATE WHEN it was archived BY the controller',
  enabled              TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate         DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby            VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby           VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
)
  COMMENT 'The TABLE Expiration';

CREATE INDEX co_expiration_co_office_id_fk
  ON co_expiration (office_id);

CREATE INDEX co_expiration_co_tasktemplate_id_fk
  ON co_expiration (tasktemplate_id);

CREATE INDEX co_expiration_co_task_id_fk
  ON co_expiration (task_id);

# -------------------------------------- #

# Creates the table: co_expirationactivity

CREATE TABLE IF NOT EXISTS co_expirationactivity
(
  id               INT AUTO_INCREMENT
    PRIMARY KEY,
  expiration_id    INT          NOT NULL
  COMMENT 'FK of the TABLE Expiration',
  body             LONGTEXT     NOT NULL,
  deleted          TINYINT(1)   NOT NULL,
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_expirationactivity_co_expiration_id_fk
  FOREIGN KEY (expiration_id) REFERENCES co_expiration (id)
)
  COMMENT 'The TABLE Exiration Activity';

CREATE INDEX co_expirationactivity_co_expiration_id_fk
  ON co_expirationactivity (expiration_id);

# -------------------------------------- #

# Creates the table: co_expirationactivityattachment

CREATE TABLE IF NOT EXISTS co_expirationactivityattachment
(
  id                    INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  expirationactivity_id INT          NOT NULL
  COMMENT 'FK of the TABLE ',
  filename              VARCHAR(255) NOT NULL
  COMMENT 'The NAME of the FILE ',
  filetype              VARCHAR(255) NOT NULL
  COMMENT 'The type of the FILE ',
  filepath              VARCHAR(255) NOT NULL
  COMMENT 'The path of the FILE ',
  creationdate          DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby             VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate      DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby            VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_expirationactivityattachment_co_expirationactivity_id_fk
  FOREIGN KEY (expirationactivity_id) REFERENCES co_expirationactivity (id)
)
  COMMENT 'The TABLE Exiration Activity Attachment';

CREATE INDEX co_expirationactivityattachment_co_expirationactivity_id_fk
  ON co_expirationactivityattachment (expirationactivity_id);

# -------------------------------------- #

# Creates the table: co_office

CREATE TABLE IF NOT EXISTS co_office
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  company_id       INT          NOT NULL
  COMMENT 'The FK of the TABLE Company',
  description      VARCHAR(100) NOT NULL
  COMMENT 'The description',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_office_co_company_id_fk
  FOREIGN KEY (company_id) REFERENCES co_company (id)
)
  COMMENT 'The TABLE Office';

CREATE INDEX co_office_co_company_id_fk
  ON co_office (company_id);

ALTER TABLE co_expiration
  ADD CONSTRAINT co_expiration_co_office_id_fk
FOREIGN KEY (office_id) REFERENCES co_office (id);

# -------------------------------------- #

# Creates the table: co_task

CREATE TABLE IF NOT EXISTS co_task
(
  id                       INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  tasktemplate_id          INT          NOT NULL
  COMMENT 'The FK of the TABLE Task Template',
  recurrence               VARCHAR(20)  NOT NULL
  COMMENT 'Periodicity:weekly,
  n  monthly,
  n  yearly',
  expirationtype           VARCHAR(20)  NOT NULL
  COMMENT 'Expiry type:fix_day,
  n  month_start,
  n  month_end',
  day                      INT          NULL
  COMMENT 'Expiry DAY (filled IN only IN the "fix_day" CASE )',
  daysofnotice             INT          NOT NULL
  COMMENT 'Frequency of the alert ( EVERY how many days IS sent)',
  daysbeforeshowexpiration INT          NOT NULL
  COMMENT 'How many days BEFORE TO SHOW the deadline',
  enabled                  TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate             DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby                VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate         DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby               VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
)
  COMMENT 'The TABLE of the Task';

CREATE INDEX co_task_co_tasktemplate_id_fk
  ON co_task (tasktemplate_id);

ALTER TABLE co_expiration
  ADD CONSTRAINT co_expiration_co_task_id_fk
FOREIGN KEY (task_id) REFERENCES co_task (id);

# -------------------------------------- #

# Creates the table: co_taskoffice

CREATE TABLE IF NOT EXISTS co_taskoffice
(
  id               INT AUTO_INCREMENT
    PRIMARY KEY,
  tasktemplate_id  INT          NOT NULL
  COMMENT 'The FK of the TABLE Task Template',
  task_id          INT          NOT NULL
  COMMENT 'The FK of the TABLE Task',
  office_id        INT          NOT NULL
  COMMENT 'The FK of the TABLE Office',
  startdate        DATETIME     NOT NULL,
  enddate          DATETIME     NOT NULL,
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_taskoffice_co_task_id_fk
  FOREIGN KEY (task_id) REFERENCES co_task (id),
  CONSTRAINT co_taskoffice_co_office_id_fk
  FOREIGN KEY (office_id) REFERENCES co_office (id)
)
  COMMENT 'The TABLE Task Office, associated TO the Office';

CREATE INDEX co_taskoffice_co_office_id_fk
  ON co_taskoffice (office_id);

CREATE INDEX co_taskoffice_co_task_id_fk
  ON co_taskoffice (task_id);

CREATE INDEX co_taskoffice_co_tasktemplate_id_fk
  ON co_taskoffice (tasktemplate_id);

# -------------------------------------- #

# Creates the table: co_taskofficerelations

CREATE TABLE IF NOT EXISTS co_taskofficerelations
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  taskoffice_id    INT          NOT NULL
  COMMENT 'FK of the TABLE Task Office',
  username         VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER ',
  relationtype     INT          NOT NULL
  COMMENT '1:Controller, 2:Controlled',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_taskofficerelations_co_task_id_fk
  FOREIGN KEY (taskoffice_id) REFERENCES co_task (id)
)
  COMMENT 'The TABLE Task Office Relations, association BETWEEN Users AND the task of the Office';

CREATE INDEX co_taskofficerelations_co_task_id_fk
  ON co_taskofficerelations (taskoffice_id);

# -------------------------------------- #

# Creates the table: co_tasktemplate

CREATE TABLE IF NOT EXISTS co_tasktemplate
(
  id                       INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  topic_id                 INT          NOT NULL
  COMMENT 'The FK of the TABLE Topic',
  description              VARCHAR(100) NOT NULL
  COMMENT 'The description',
  recurrence               VARCHAR(20)  NOT NULL
  COMMENT 'Periodicity:weekly,
  n  monthly,
  n  yearly',
  expirationtype           VARCHAR(20)  NOT NULL
  COMMENT 'Expiry type:fix_day,
  n  month_start,
  n  month_end',
  day                      INT          NULL
  COMMENT 'Expiry DAY (filled IN only IN the "fix_day" CASE )',
  daysofnotice             INT          NOT NULL
  COMMENT 'Frequency of the alert ( EVERY how many days IS sent)',
  daysbeforeshowexpiration INT          NOT NULL
  COMMENT 'How many days BEFORE TO SHOW the deadline',
  expirationclosableby     INT(1)       NOT NULL
  COMMENT '1:Anyone can CLOSE the task, 2:EACH CORPOBLIG_USER has his task',
  enabled                  TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate             DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby                VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate         DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby               VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
)
  COMMENT 'The TABLE Task Template';

CREATE INDEX co_tasktemplate_co_topic_id_fk
  ON co_tasktemplate (topic_id);

ALTER TABLE co_expiration
  ADD CONSTRAINT co_expiration_co_tasktemplate_id_fk
FOREIGN KEY (tasktemplate_id) REFERENCES co_tasktemplate (id);

ALTER TABLE co_task
  ADD CONSTRAINT co_task_co_tasktemplate_id_fk
FOREIGN KEY (tasktemplate_id) REFERENCES co_tasktemplate (id);

ALTER TABLE co_taskoffice
  ADD CONSTRAINT co_taskoffice_co_tasktemplate_id_fk
FOREIGN KEY (tasktemplate_id) REFERENCES co_tasktemplate (id);

# -------------------------------------- #

# Creates the table: co_tasktemplateattachment

CREATE TABLE IF NOT EXISTS co_tasktemplateattachment
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  tasktemplate_id  INT          NOT NULL
  COMMENT 'The FK of the TABLE Task template',
  filename         VARCHAR(255) NOT NULL
  COMMENT 'The NAME of the FILE ',
  filetype         VARCHAR(255) NOT NULL
  COMMENT 'The type of the FILE ',
  filepath         VARCHAR(255) NOT NULL
  COMMENT 'The path of the FILE ',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_tasktemplateattachment_co_tasktemplate_id_fk
  FOREIGN KEY (tasktemplate_id) REFERENCES co_tasktemplate (id)
)
  COMMENT 'The TABLE of Task template attached ';

CREATE INDEX co_tasktemplateattachment_co_tasktemplate_id_fk
  ON co_tasktemplateattachment (tasktemplate_id);

# -------------------------------------- #

# Creates the table: co_topic

CREATE TABLE IF NOT EXISTS co_topic
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  description      VARCHAR(100) NOT NULL
  COMMENT 'The description',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
)
  COMMENT 'The TABLE Topic';

ALTER TABLE co_companytopic
  ADD CONSTRAINT co_companytopic_co_topic_id_fk
FOREIGN KEY (topic_id) REFERENCES co_topic (id);

ALTER TABLE co_tasktemplate
  ADD CONSTRAINT co_tasktemplate_co_topic_id_fk
FOREIGN KEY (topic_id) REFERENCES co_topic (id);

# -------------------------------------- #

# Creates the table: co_topicconsultant

CREATE TABLE IF NOT EXISTS co_topicconsultant
(
  id               INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  topic_id         INT          NOT NULL
  COMMENT 'The FK of the TABLE Topic',
  consultant_id    INT          NOT NULL
  COMMENT 'The FK of the TABLE Consultant',
  enabled          TINYINT(1)   NOT NULL
  COMMENT 'The STATUS :Active OR NOT active',
  creationdate     DATETIME     NOT NULL
  COMMENT 'The DATE of creation of the record',
  createdby        VARCHAR(100) NOT NULL
  COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  modificationdate DATETIME     NOT NULL
  COMMENT 'The DATE of the creation of the record',
  modifiedby       VARCHAR(100) NOT NULL
  COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  CONSTRAINT co_topicconsultant_co_topic_id_fk
  FOREIGN KEY (topic_id) REFERENCES co_topic (id)
)
  COMMENT 'The TABLE Topic Consultant';

CREATE INDEX co_topicconsultant_co_topic_id_fk
  ON co_topicconsultant (topic_id);

# -------------------------------------- #

# Creates the table: co_translations

CREATE TABLE IF NOT EXISTS co_translations
(
  id          INT AUTO_INCREMENT
  COMMENT 'The PK of the TABLE '
  PRIMARY KEY,
  entity_id   INT          NOT NULL
  COMMENT 'The ID of entity TO translate the desciption',
  tablename   VARCHAR(100) NOT NULL
  COMMENT 'The TABLE NAME serves AS reference IN ORDER TO translate the TEXT ',
  lang        VARCHAR(2)   NOT NULL
  COMMENT 'The LANGUAGE of the translation TEXT ',
  description VARCHAR(255) NOT NULL
  COMMENT 'The TEXT translated'
)
  COMMENT 'The TABLE that CONTAINS the TEXT of different languanges';

# -------------------------------------- #

# Creates the table: co_user

CREATE TABLE IF NOT EXISTS co_user
(
  username VARCHAR(100) NOT NULL
  COMMENT 'The username of the CORPOBLIG_USER has ben used during whole SESSION '
  PRIMARY KEY,
  fullname VARCHAR(255) NOT NULL
  COMMENT 'The NAME AND LAST NAME of the CORPOBLIG_USER ',
  email    VARCHAR(255) NOT NULL
  COMMENT 'The email of the CORPOBLIG_USER ',
  lang     VARCHAR(2)   NOT NULL
  COMMENT 'The LANGUAGE of the CORPOBLIG_USER used TO display the CORPOBLIG_USER LANGUAGE ON the application',
  enabled  TINYINT(1)   NOT NULL
  COMMENT 'The STATUS of the CORPOBLIG_USER :Active OR NOT Active'
)
  COMMENT 'The TABLE of the Users';

ALTER TABLE co_companyuser
  ADD CONSTRAINT co_companyuser_co_user_username_fk
FOREIGN KEY (username) REFERENCES co_user (username);

# -------------------------------------- #

# Creates the table: co_userrole

create table co_userrole
(
  id int auto_increment comment 'The PK of the TABLE '
    primary key,
  username varchar(100) not null comment 'The FK of the TABLE CORPOBLIG_USER,n  n  defined the role of the CORPOBLIG_USER ',
  roleuid varchar(100) not null comment 'The FK of the TABLE role,n  n  defines the role FOR the CORPOBLIG_USER ',
  constraint co_userrole_co_user_username_fk
  foreign key (username) references co_user (username)
)
  comment 'The junction TABLE of CORPOBLIG_USER AND role'
;

create index co_userrole_co_role_role_fk
  on co_userrole (roleuid)
;

create index co_userrole_co_user_username_fk
  on co_userrole (username)
;

