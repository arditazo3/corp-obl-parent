delete from co_translations where tablename = 'tasktemplate#configurationinterval';
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#configurationinterval', 'EN', 'Configuration: ');
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#configurationinterval', 'IT', 'Configurazione: ');

ALTER TABLE co_taskofficerelations DROP FOREIGN KEY co_taskofficerelations_co_task_id_fk;
ALTER TABLE co_taskofficerelations
ADD CONSTRAINT co_taskofficerelations_co_taskoffice_id_fk
FOREIGN KEY (taskoffice_id) REFERENCES co_taskoffice (id);

ALTER TABLE co_taskoffice ADD enabled TINYINT(1) NOT NULL;
ALTER TABLE co_taskoffice
  MODIFY COLUMN creationdate DATETIME NOT NULL COMMENT 'The DATE of creation of the record' AFTER enabled,
  MODIFY COLUMN enabled TINYINT(1) NOT NULL AFTER enddate;