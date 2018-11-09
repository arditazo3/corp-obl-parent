select * from co_tasktemplate;

select * from co_task;

select * from co_taskoffice;

select * from co_taskofficerelations;

select * from co_translations;

select * from co_office;

select * from co_expiration;

select * from co_expirationactivity;

select * from co_translations where description like '%Companies%';

select * from co_company c left join co_companyuser cc on c.id = cc.company_id where cc.companyadmin = 1;