alter table co_company change modifcationdate modificationdate datetime not null;

ALTER TABLE co_topicconsultant CHANGE consultant_id consultantcompany_id int(11) NOT NULL COMMENT 'The FK of the TABLE Consultant';

ALTER TABLE co_topicconsultant ADD CONSTRAINT co_topicconsultant_co_companyconsultant_id_fk FOREIGN KEY (consultantcompany_id) REFERENCES co_companyconsultant (id);