TRUNCATE TABLE co_userrole;
INSERT INTO co_userrole (username, roleuid) VALUES ('ADMIN', 'CORPOBLIG_ADMIN');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER2', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER3', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER4', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('USER5', 'CORPOBLIG_USER');
INSERT INTO co_userrole (username, roleuid) VALUES ('FOREIGN', 'CORPOBLIG_BACKOFFICE_FOREIGN');
INSERT INTO co_userrole (username, roleuid) VALUES ('INLAND', 'CORPOBLIG_BACKOFFICE_INLAND');

TRUNCATE TABLE co_user;
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('ADMIN', 'Administrator', 'admin@admin.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('FOREIGN', 'Foreign', 'foreign@foreign.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('INLAND', 'Inland', 'inland@inland.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('TEST', 'Test', 'test@test.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER', 'User', 'user@user.com', 'IT', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER2', 'User 2', 'user2@user.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER3', 'User 3', 'user3@user.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER4', 'User 4', 'user4@user.com', 'EN', 1);
INSERT INTO co_user (username, fullname, email, lang, enabled) VALUES ('USER5', 'User 5', 'user5@user.com', 'EN', 1);

INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (22, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (24, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (26, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (25, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (27, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');
INSERT INTO co_task (tasktemplate_id, recurrence, expirationtype, day, daysofnotice, daysbeforeshowexpiration, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (28, '20', '10', 10, 10, 10, 1, '2018-09-19 05:22:11', 'admin', '2018-09-19 05:22:19', '1');

select * from co_taskofficerelations;

insert into co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby)
select tt.id, t.id, tasko.office_id, tt.expirationclosableby, tor.username, '2018-11-5', null, null, null, 1, now(), tor.username, now(), tor.username
from co_tasktemplate tt
       left join co_task t on tt.id = t.tasktemplate_id
       left join co_expiration e on t.id = e.task_id
       left outer join co_taskoffice tasko on t.id = tasko.task_id
       left join co_taskofficerelations tor on tasko.id = tor.taskoffice_id
where tor.username = 'user2'
group by tt.id, t.id,tasko.office_id;

insert into co_expirationactivity(expiration_id, body, deleted, creationdate, createdby, modificationdate, modifiedby)
select 5, 'Test test', 0, now(), modifiedby, now(), modifiedby
from co_expiration;

insert into co_expirationactivityattachment(expirationactivity_id, filename, filetype, filepath, filesize, createdby, modificationdate, modifiedby, creationdate)
select 15, filename, filetype, filepath, filesize, 'user 5', now(), 'user 5', now()
from co_tasktemplateattachment;

select * from co_expiration;
select * from co_expirationactivity;
select * from co_expirationactivityattachment;

select * from co_taskoffice;
select * from co_taskofficerelations;

select * from co_topic;

INSERT INTO co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (73, 131, 8, 1, 'USER2', '2018-11-06', '2018-10-27 19:20:00', '2018-10-27 19:18:59', '2018-10-27 19:19:31', 1, '2018-10-27 20:46:32', 'USER2', '2018-10-27 20:46:32', 'USER2');


------------------



INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (76, 134, 8, 2, 'USER5', '2018-11-10', '2018-11-01 09:57:56', null, null, 1, '2018-10-31 15:21:05', 'ADMIN', '2018-11-01 09:57:56', 'USER5');
INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (76, 134, 8, 2, 'USER4', '2018-11-10', '2018-11-01 09:58:42', null, null, 1, '2018-10-31 15:21:05', 'ADMIN', '2018-11-01 09:58:42', 'USER4');
INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (76, 134, 8, 2, 'USER3', '2018-11-10', '2018-11-01 09:59:16', null, null, 1, '2018-10-31 15:21:05', 'ADMIN', '2018-11-01 09:59:16', 'USER3');
INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (76, 134, 9, 2, 'USER4', '2018-11-10', '2018-11-01 09:58:30', null, null, 1, '2018-10-31 15:21:05', 'ADMIN', '2018-11-01 09:58:30', 'USER4');
INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (76, 134, 9, 2, 'USER5', '2018-11-10', '2018-11-01 09:58:08', null, null, 1, '2018-10-31 15:21:05', 'ADMIN', '2018-11-01 09:58:08', 'USER5');
INSERT INTO corporate_obligations.co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby) VALUES (77, 135, 10, 1, '', '2018-11-15', '2018-11-01 09:57:45', null, null, 1, '2018-10-31 16:10:02', 'ADMIN', '2018-11-01 09:57:45', 'USER5');