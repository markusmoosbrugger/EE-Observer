[![EE DB CI Java Repository](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml/badge.svg)](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml)

# EE-DB-Writer

Project containing a few experiments to log function properties to various databases. 

The package `logback` contains a simple logger which uses a logback DB appender to store function
parameters such as function id, function type and execution time to a relational MySQL database.
Here I tested the logging functionality with a local MySQL database, as well as a database hosted in
AWS and Google.

The package `influxdb` contains a database writer which stores similar function parameters to the
time series database InfluxDB. Furthermore it contains an POJO `InfluxFunction` which defines the
properties which are stored in the DB, besides a simulator, which simulates multiple runs of 
simple function executions.
