ALTER TABLE co_tasktemplate ADD frequenceofnotice int NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)';
ALTER TABLE co_tasktemplate
  MODIFY COLUMN frequenceofnotice int NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)' AFTER daysofnotice,
  MODIFY COLUMN creationdate datetime NOT NULL COMMENT 'The DATE of creation of the record' AFTER modifiedby,
  MODIFY COLUMN expirationclosableby int(1) NOT NULL COMMENT '1:Anyone can CLOSE the task, 2:EACH CORPOBLIG_USER has his task' AFTER enabled;
  
ALTER TABLE co_task ADD frequenceofnotice int NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)';
ALTER TABLE co_task
  MODIFY COLUMN frequenceofnotice int NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)' AFTER daysofnotice,
  MODIFY COLUMN creationdate datetime NOT NULL COMMENT 'The DATE of creation of the record' AFTER modifiedby,
  MODIFY COLUMN daysbeforeshowexpiration int(11) NOT NULL COMMENT 'How many days BEFORE TO SHOW the deadline' AFTER enabled;
  
ALTER TABLE co_tasktemplateattachment ADD filesize int NOT NULL COMMENT 'File size';
ALTER TABLE co_tasktemplateattachment
  MODIFY COLUMN creationdate datetime NOT NULL COMMENT 'The DATE of creation of the record' AFTER filesize,
  MODIFY COLUMN filesize int NOT NULL AFTER filepath;  