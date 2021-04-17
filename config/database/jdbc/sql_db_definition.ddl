create database if not exists ee_observer;

use ee_observer;

drop table if exists enactment;
create table enactment (
    id int primary key auto_increment,
    timestamp long,
    functionType varchar(100),
    functionId varchar(500),
    executionTime double,
    inputComplexity double,
    success bool
);

insert into enactment (timestamp, functionType, functionId, executionTime, inputComplexity,
                  success) values (1618481617000, 'Local', 'Addition', 1898.01, 0.87, 1)
