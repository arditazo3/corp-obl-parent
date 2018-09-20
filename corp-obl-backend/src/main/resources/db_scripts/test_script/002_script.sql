# Get Task by Companies and Topics

SELECT t.*
FROM co_task t LEFT JOIN co_tasktemplate tt ON t.tasktemplate_id = tt.id
  LEFT JOIN co_topic top ON tt.topic_id = top.id
  LEFT JOIN co_companytopic ct ON top.id = ct.topic_id;


UPDATE co_tasktemplate
SET    description = CONCAT('Task Template: @', id);

update co_tasktemplate set expirationclosableby = 1;