select * from co_topic;
select * from co_companytopic;
select * from co_companyuser;
select * from co_user;

# Retrieve topics by roles

# Admin - tutti
SELECT t.*
FROM co_topic t
WHERE t.enabled <> 0
GROUP BY t.id
ORDER BY t.description;

# corpoblig_backoffice_foreign - quelli cui sei abilitato

SELECT t.*
FROM co_topic t
  LEFT JOIN co_companytopic ct ON t.id = ct.topic_id
  LEFT JOIN co_company c ON ct.company_id = c.id
  LEFT JOIN co_companyuser cu ON c.id = cu.company_id
WHERE cu.username = 'FOREIGN' and t.enabled <> 0 and ct.enabled <> 0 and c.enabled <> 0 and cu.enabled <> 0
GROUP BY t.id
ORDER BY t.description;

# corpoblig_backoffice_inland - NON LO VISUALIZZA, di default si prende il primo della lista

SELECT t.*
FROM co_topic t
  LEFT JOIN co_companytopic ct ON t.id = ct.topic_id
  LEFT JOIN co_company c ON ct.company_id = c.id
  LEFT JOIN co_companyuser cu ON c.id = cu.company_id
WHERE cu.username = 'INLAND' and t.enabled <> 0 and ct.enabled <> 0 and c.enabled <> 0 and cu.enabled <> 0
GROUP BY t.id
ORDER BY t.description
limit 1;

# general query

SELECT t.*
FROM co_topic t
  JOIN co_companytopic ct ON t.id = ct.topic_id
  JOIN co_company c ON ct.company_id = c.id
  JOIN co_companyuser cu ON c.id = cu.company_id
  JOIN co_user u ON cu.username = u.username
  JOIN co_userrole ur ON u.username = ur.username;