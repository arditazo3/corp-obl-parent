select * from co_company;

select * from co_companyuser;

select * from co_office;

select * from co_tasktemplate;

select * from co_task;

select * from co_taskoffice;

select * from co_taskofficerelations;

select * from co_topic;

select * from co_companyconsultant;

select * from co_topicconsultant;

select * from co_tasktemplate;

select * from co_tasktemplateattachment;

select * from co_task;

select * from co_taskoffice;

select * from co_taskofficerelations;

select * from co_expiration;



update co_tasktemplate tt
set israpidconfiguration = 1
where (select count(task.id) from co_task task group by task.tasktemplate_id) > 1;

;
