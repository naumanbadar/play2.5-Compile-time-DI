-- If dropping database is needed
IF EXISTS(SELECT m.name
          FROM master.sys.sysdatabases m
          WHERE m.name = 'MyDatabase')
  ALTER DATABASE MyDatabase SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
DROP DATABASE IF EXISTS MyDatabase;
CREATE DATABASE MyDatabase;

CREATE TABLE MyDatabase.dbo.MyModels (
  Id   INT PRIMARY KEY NOT NULL IDENTITY,
  Name VARCHAR(200)
);



