create database if not exists ee_observer;

use ee_observer;

drop table if exists enactment;
create table enactment (
    id int primary key auto_increment,
    timestamp long,
    typeId varchar(100),
    enactmentMode varchar(100),
    implementationId varchar(500),
    executionTime double,
    inputComplexity double,
    success bool
);

insert into enactment (timestamp, typeId, enactmentMode, implementationId, executionTime, inputComplexity,
                  success) values (1618481617000, 'Addition', 'Local', 'Implementation 1', 1898.01, 0.87, 1)
