TRUNCATE TABLE co_userrole;
INSERT INTO co_userrole (username, roleuid) VALUES ('ADMIN', 'CORPOBLIG_ADMIN');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('FOREIGN', 'CORPOBLIG_BACKOFFICE_FOREIGN');
INSERT INTO co_userrole (username, roleuid) VALUES ('INLAND', 'CORPOBLIG_BACKOFFICE_INLAND');

TRUNCATE TABLE co_user;
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('ADMIN', 'Administrator', 'admin@admin.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('FOREIGN', 'Foreign', 'foreign@foreign.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('INLAND', 'Inland', 'inland@inland.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('TEST', 'Test', 'test@test.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER', 'User', 'user@user.com', 'IT', 1);

INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (22, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (24, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (26, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (25, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (27, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (28, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');