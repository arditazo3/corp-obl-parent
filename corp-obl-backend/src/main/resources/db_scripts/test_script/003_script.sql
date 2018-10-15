select * from co_expiration;

insert into co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby)
select tt.id, task.id, o.id, 1, cu.username, date_add(now(), INTERVAL 15 DAY), date_add(now(), INTERVAL 5 DAY), now(), now(), 1, now(), cu.username, now(), cu.username
from co_tasktemplate tt
       left join co_task task on tt.id = task.tasktemplate_id
       left join co_taskoffice t on task.id = t.task_id
       left join co_office o on t.office_id = o.id
       left join co_company c on o.company_id = c.id
       left join co_company company on o.company_id = company.id
       left join co_companyuser cu on c.id = cu.company_id
where cu.username = 'user2'
group by task.id;

insert into co_expiration (tasktemplate_id, task_id, office_id, expirationclosableby, username, expirationdate, completed, approved, registered, enabled, creationdate, createdby, modificationdate, modifiedby)
select tt.id, task.id, o.id, 1, cu.username, date_add(now(), INTERVAL 15 DAY), date_add(now(), INTERVAL 15 DAY), date_add(now(), INTERVAL 15 DAY), date_add(now(), INTERVAL 15 DAY), 1, now(), cu.username, now(), cu.username
from co_tasktemplate tt
       left join co_task task on tt.id = task.tasktemplate_id
       left join co_taskoffice t on task.id = t.task_id
       left join co_office o on t.office_id = o.id
       left join co_company c on o.company_id = c.id
       left join co_company company on o.company_id = company.id
       left join co_companyuser cu on c.id = cu.company_id
where cu.username = 'user2'
group by task.id;


select * from co_tasktemplate;

select * from co_task;

select * from co_office;

select * from co_translations where tablename like 'tasktemplate#periodicity%';

select *
from co_tasktemplate tt
       left join co_task task on tt.id = task.tasktemplate_id
       left join co_taskoffice t on task.id = t.task_id
       left join co_office o on t.office_id = o.id
       left join co_company c on o.company_id = c.id
       left join co_company company on o.company_id = company.id
       left join co_companyuser cu on c.id = cu.company_id
where cu.username = 'user2'
group by task.id;
