-- Run inside FREEPDB1 as SYS/SYSTEM to create the application user:
--   sqlplus sys/oracle@//localhost:1521/FREEPDB1 as sysdba @src/main/resources/db/init.sql
CREATE USER hotel IDENTIFIED BY hotel;
GRANT CONNECT, RESOURCE TO hotel;
ALTER USER hotel QUOTA UNLIMITED ON USERS;
