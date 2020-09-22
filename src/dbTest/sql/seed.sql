INSERT INTO admins (id)
VALUES (1),
       (2),
       (3);

INSERT INTO chat_states (id, state)
VALUES (1, '{
	"id": 1,
	"type": "Started"
}'::JSONB),
       (2, '{
	       "id": 2,
	       "type": "Helpfulness1"
       }'::JSONB),
       (3, '{
	       "id": 3,
	       "type": "Helpfulness2",
	       "name": "Darth Vader"
       }'::JSONB),
       (4, '{
	       "id": 4,
	       "type": "Helpfulness3",
	       "name": "Darth Vader",
	       "description": "Force choking"
       }'::JSONB);

INSERT INTO applications(id, user_id, user_name, helpfulness, contact)
VALUES ('test1', 1, 'Darth Vader', 'Force choking', '@annie_the_dustboy'),
       ('test2', 1, 'Darth Vader', 'Jedi hunting', '@annie_the_dustboy'),
       ('test3', 2, 'Lil Yo', 'Protecting you from mudhorns and scam', '@YodaKid2019');
