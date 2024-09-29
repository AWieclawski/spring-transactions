-- insert demo data in existing tables

INSERT INTO addresses(id, city, country, postal_code, street_local, verification_key)
VALUES (-1, 'Democity', 'Democountry', 'DEMOPC', 'DEMO_STREET_LOCAL'. 'DEMO_UNIQUE_VERIFICATION_KEY') ON CONFLICT DO NOTHING;

INSERT INTO users(id, first_name, last_name, login, email, address_id)
VALUES (-1, 'Demofirstname', 'Demolastname', 'DEMO_LOGIN', 'demo@email.edu', -1) ON CONFLICT DO NOTHING;

INSERT INTO orders(id, order_date, order_no, email, user_id)
VALUES (-1, TO_DATE('2024-08-03','YYYY-MM-DD'), 'DEMO ORDER NO 123', 'order@demo.email.edu', -1) ON CONFLICT DO NOTHING;

INSERT INTO positions(id, description, quantity, unit_value, measure, order_id)
VALUES (-1, 'DEMO DESCRIPTION 1', 2.00, 3.00, 'PC', -1) ON CONFLICT DO NOTHING;

-- test
SELECT * FROM addresses ORDER BY id LIMIT 1;
SELECT * FROM users ORDER BY id LIMIT 1;
SELECT * FROM orders ORDER BY id LIMIT 1;
SELECT * FROM positions ORDER BY id LIMIT 1;