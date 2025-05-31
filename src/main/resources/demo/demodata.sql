-- insert demo data in existing tables

INSERT INTO addresses(id, city, country, postal_code, street_local, verification_key)
VALUES (-2, 'Democity2', 'Democountry2', 'DEMOPC2', 'DEMO_STREET_LOCAL2', 'DEMO_UNIQUE_VERIFICATION_KEY2') ON CONFLICT DO NOTHING;

INSERT INTO users(id, first_name, last_name, login, email, address_id)
VALUES (-2, 'Demofirstname2', 'Demolastname2', 'DEMO_LOGIN2', 'demo2@email.edu', -2) ON CONFLICT DO NOTHING;

INSERT INTO orders(id, order_date, order_no, email, user_id)
VALUES (-2, TO_DATE('2024-09-22','YYYY-MM-DD'), 'DEMO ORDER NO 234', 'order22@demo.email.edu', -2) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-2, 'DEMO DESCRIPTION 2', 2.22, 2.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-3, 'DEMO DESCRIPTION 3', 3.33, 3.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-4, 'DEMO DESCRIPTION 4', 4.00, 4.00, 'PC', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-5, 'DEMO DESCRIPTION 5', 5.55, 5.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-6, 'DEMO DESCRIPTION 6', 6.66, 6.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-7, 'DEMO DESCRIPTION 7', 7.77, 7.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-8, 'DEMO DESCRIPTION 8', 8.00, 8.00, 'PC', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-9, 'DEMO DESCRIPTION 9', 9.99, 9.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-10, 'DEMO DESCRIPTION 10', 10.10, 10.00, 'M3', -1) ON CONFLICT DO NOTHING;


INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-12, 'DEMO DESCRIPTION 2', 12.22, 2.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-13, 'DEMO DESCRIPTION 3', 13.33, 3.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-14, 'DEMO DESCRIPTION 4', 14.00, 4.00, 'PC', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-15, 'DEMO DESCRIPTION 5', 15.55, 5.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-16, 'DEMO DESCRIPTION 6', 16.66, 6.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-17, 'DEMO DESCRIPTION 7', 17.77, 7.00, 'M3', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-18, 'DEMO DESCRIPTION 8', 18.00, 8.00, 'PC', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-19, 'DEMO DESCRIPTION 9', 19.99, 9.00, 'M2', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-10, 'DEMO DESCRIPTION 10', 110.10, 10.00, 'M3', -1) ON CONFLICT DO NOTHING;


-- test
SELECT * FROM addresses ORDER BY id LIMIT 1;
SELECT * FROM users ORDER BY id LIMIT 1;
SELECT * FROM orders ORDER BY id LIMIT 1;
SELECT * FROM positions ORDER BY id LIMIT 10;