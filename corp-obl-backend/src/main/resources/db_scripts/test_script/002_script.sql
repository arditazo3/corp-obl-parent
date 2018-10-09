# Get Task by Companies and Topics

SELECT c.description, top.description
FROM co_task t LEFT JOIN co_tasktemplate tt ON t.tasktemplate_id = tt.id
  LEFT JOIN co_topic top ON tt.topic_id = top.id
  LEFT JOIN co_companytopic ct ON top.id = ct.topic_id
  LEFT JOIN co_company c ON ct.company_id = c.id
where tt.description = 'Task Template: @20' or (top.id in (1) and c.id in (1));

SELECT tt.*
FROM co_tasktemplate tt
  LEFT JOIN co_topic top ON tt.topic_id = top.id
  LEFT JOIN co_companytopic ct ON top.id = ct.topic_id
  LEFT JOIN co_company c ON ct.company_id = c.id
  LEFT JOIN co_office o ON c.id = o.company_id
where o.id =  6;

select * from co_taskoffice;

select * from co_tasktemplate tt
LEFT JOIN co_taskoffice too on tt.id = too.tasktemplate_id
  LEFT JOIN co_office o ON too.office_id = o.id
where tt.description like '%test%' and tt.enabled <> 0;

update co_tasktemplate set expirationclosableby = 1;

select * from co_tasktemplateattachment;

select *
from co_company c
       left join co_companyuser cu on c.id = cu.company_id
       left join co_user u on cu.username = u.username
       left join co_userrole ur on u.username = ur.username
where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0
and ur.roleuid in ('CORPOBLIG_BACKOFFICE_FOREIGN') and cu.companyadmin <> 0
group by c.id order by c.description asc;

select * from co_tasktemplate tt left join co_topic t on tt.topic_id = t.id
left join co_topicconsultant tc on t.id = tc.topic_id
left join co_companyconsultant cc on tc.consultantcompany_id = cc.id
left join co_company c on cc.company_id = c.id
left join co_companyuser cu on c.id = cu.company_id
where tt.enabled <> 0 and t.enabled <> 0 and tc.enabled <> 0 and cc.enabled <> 0
and c.enabled <> 0 and cu.enabled <> 0 and cu.username = 'FOREIGN';

select o.*, tt.*
from co_taskoffice tasko
       left join co_tasktemplate tt on tasko.tasktemplate_id = tt.id
       left join co_office o on tasko.office_id = o.id
       left join co_company c on o.company_id = c.id
       left join co_companyuser cc on c.id = cc.company_id
       left join co_user u on cc.username = u.username
       left JOIN co_userrole ur on u.username = ur.username
where o.enabled <> 0
  and tt.enabled <> 0
group by tasko.id
order by tt.description asc;

select * from co_user left join co_userrole on co_user.username = co_userrole.username;

select * from co_translations;

select * from co_task;

SELECT
  o.*,
  tt.*
FROM co_taskoffice tasko LEFT JOIN co_tasktemplate tt ON tasko.tasktemplate_id = tt.id
  LEFT JOIN co_office o ON tasko.office_id = o.id
  LEFT JOIN co_company c ON o.company_id = c.id
  LEFT JOIN co_companyuser cc ON c.id = cc.company_id
  LEFT JOIN co_user u ON cc.username = u.username
  LEFT JOIN co_userrole ur ON u.username = ur.username
WHERE o.enabled <> 0 AND tt.enabled <> 0
  where tt.description like '%task%' and to.office in (6, 5)
GROUP BY tasko.id
ORDER BY tt.description ASC ;