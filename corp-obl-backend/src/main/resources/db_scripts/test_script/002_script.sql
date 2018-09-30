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
where o.id =  4;

select * from office left JOIN

select * from co_office;

UPDATE co_tasktemplate
SET    description = CONCAT('Task Template: @', id);

update co_tasktemplate set expirationclosableby = 1;

select * from co_tasktemplateattachment;