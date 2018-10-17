ALTER TABLE co_expiration MODIFY expirationdate date NOT NULL COMMENT 'The expired TIME';

ALTER TABLE co_expiration MODIFY completed datetime COMMENT 'DATE of WHEN it was declared executed ( BY the controlled)';
ALTER TABLE co_expiration MODIFY approved datetime COMMENT 'DATE WHEN it was declared approved BY the controller';
ALTER TABLE co_expiration MODIFY registered datetime COMMENT 'DATE WHEN it was archived BY the controller';