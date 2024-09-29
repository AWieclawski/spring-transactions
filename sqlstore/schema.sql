-- set default schema, sequences, tables

-- default simple hibernate sequence table

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

-- public.addresses definition

-- Drop table

-- DROP TABLE public.addresses;

CREATE TABLE IF NOT EXISTS public.addresses (
	id int8 NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	version int8 NULL,
	transaction_name varchar(255) NULL,
	verification_key varchar(255) NULL,
	city varchar(50) NOT NULL,
	country varchar(49) NULL,
	postal_code varchar(6) NOT NULL,
	street_local varchar(100) NULL,
	CONSTRAINT addresses_pkey PRIMARY KEY (id)
);

-- public.addresses unique index

CREATE UNIQUE INDEX addresses_street_local_idx ON addresses (street_local)

-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS public.users (
	id int8 NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	version int8 NULL,
	transaction_name varchar(255) NULL,
	verification_key varchar(255) NULL,
	login varchar(255) NOT NULL UNIQUE,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	address_id int8 NOT NULL,
	email varchar(255) NOT NULL UNIQUE,
	phone varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- public.users foreign keys

ALTER TABLE public.users ADD CONSTRAINT users_addresses_fk FOREIGN KEY (address_id) REFERENCES public.addresses(id);

-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE IF NOT EXISTS public.orders (
	id int8 NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	version int8 NULL,
	transaction_name varchar(255) NULL,
	verification_key varchar(255) NULL,
	order_date timestamp NULL,
	order_no varchar(255) NOT NULL UNIQUE,
	email varchar(255) NOT NULL,
	phone varchar(255) NULL,
	user_id int8 NOT NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id)
);

-- public.orders foreign keys

ALTER TABLE public.orders ADD CONSTRAINT orders_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id);

-- public.positions definition

-- Drop table

-- DROP TABLE public.positions;

CREATE TABLE IF NOT EXISTS public.positions (
	id int8 NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	version int8 NULL,
	transaction_name varchar(255) NULL,
	verification_key varchar(255) NULL,
	order_id int8 NOT NULL,
	description varchar(255) NOT NULL,
	quantity numeric(16, 2) NULL,
	unit_value numeric(16, 2) NULL,
	measure varchar(255) NULL,
	CONSTRAINT positions_pkey PRIMARY KEY (id)
);

-- public.positions foreign keys

ALTER TABLE public.positions ADD CONSTRAINT positions_orders_fk FOREIGN KEY (order_id) REFERENCES public.orders(id);

-- public.positions unique columns pair

ALTER TABLE public.positions ADD CONSTRAINT positions_unique_order_description UNIQUE (order_id, description);

--test
SELECT table_schema,table_name
FROM information_schema.tables
WHERE table_schema NOT LIKE ALL (ARRAY['pg_catalog','information_schema'])
ORDER BY table_schema, table_name;