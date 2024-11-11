-- create new users, grants, functions if any

-- test
SELECT usename, usesuper, usecreatedb
FROM pg_catalog.pg_user
ORDER BY usename;