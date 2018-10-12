ALTER TABLE co_expirationactivityattachment ADD filesizze INT(11) NOT NULL COMMENT 'The size of the File';
ALTER TABLE co_expirationactivityattachment
  MODIFY COLUMN creationdate DATETIME NOT NULL COMMENT 'The DATE of creation of the record' AFTER filesizze,
  MODIFY COLUMN filesizze INT(11) NOT NULL COMMENT 'The size of the File' AFTER filepath;