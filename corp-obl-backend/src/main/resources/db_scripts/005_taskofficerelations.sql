ALTER TABLE co_taskofficerelations DROP FOREIGN KEY co_taskofficerelations_co_task_id_fk;
ALTER TABLE co_taskofficerelations
ADD CONSTRAINT co_taskofficerelations_co_taskoffice_id_fk
FOREIGN KEY (taskoffice_id) REFERENCES co_taskoffice (id);  