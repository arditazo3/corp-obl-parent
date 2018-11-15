ALTER TABLE co_tasktemplate ADD israpidconfiguration tinyint(1) NOT NULL;
ALTER TABLE co_tasktemplate
  MODIFY COLUMN israpidconfiguration tinyint(1) NOT NULL AFTER day,
  MODIFY COLUMN createdby varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification' AFTER creationdate,
  MODIFY COLUMN daysofnotice int(11) NOT NULL COMMENT 'Frequency of the alert ( EVERY how many days IS sent)' AFTER daysbeforeshowexpiration,
  MODIFY COLUMN enabled tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active' AFTER expirationclosableby;

# Opzionale, si potra fare update per mantenere separati i task template rapid
update co_tasktemplate tt
set israpidconfiguration = 1
where ( select count(*) from co_task t where t.tasktemplate_id = tt.id  ) = 1;

update co_tasktemplate tt
set israpidconfiguration = 0
where ( select count(*) from co_task t where t.tasktemplate_id = tt.id  ) <> 1;