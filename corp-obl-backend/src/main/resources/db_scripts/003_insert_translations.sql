delete from co_translations where tablename like 'tasktemplate#periodicity%';
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#periodicity#weekly', 'EN', 'Weekly');
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#periodicity#weekly', 'IT', 'Settimanale');

insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#periodicity#monthly', 'EN', 'Monthly');
insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#periodicity#monthly', 'IT', 'Mensile');

insert into co_translations(entity_id, tablename, lang, description) VALUES (3, 'tasktemplate#periodicity#yearly', 'EN', 'Yearly');
insert into co_translations(entity_id, tablename, lang, description) VALUES (3, 'tasktemplate#periodicity#yearly', 'IT', 'Annuale');

delete from co_translations where tablename like 'tasktemplate#expirationtype%';
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#expirationtype#fix_day', 'EN', 'Fixed day');
insert into co_translations(entity_id, tablename, lang, description) VALUES (1, 'tasktemplate#expirationtype#fix_day', 'IT', 'Giorno fisso');

insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#expirationtype#month_start', 'EN', 'Beginning of the month');
insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#expirationtype#month_start', 'IT', 'Inizio mese');

insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#expirationtype#month_end', 'EN', 'End of the month');
insert into co_translations(entity_id, tablename, lang, description) VALUES (2, 'tasktemplate#expirationtype#month_end', 'IT', 'Fine mese');