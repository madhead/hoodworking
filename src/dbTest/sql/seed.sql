INSERT INTO admins (id)
VALUES (1),
       (2),
       (3);

INSERT INTO chat_states (id, state)
VALUES (1, '{
	"id": 1,
	"type": "Started"
}'::JSONB);
