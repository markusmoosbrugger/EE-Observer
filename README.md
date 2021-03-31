[![EE DB CI Java Repository](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml/badge.svg)](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml)

# EE-DB-Writer

Project containing a few experiments to log function properties to various databases.

The package `logback` contains a simple logger which uses a logback DB appender to store function
invocation parameters such as function id, function type and execution time to a relational MySQL
database. Here I tested the logging functionality with a local MySQL database, as well as a database
hosted in AWS and Google.

The package `influxdb` contains a database writer which stores similar function parameters to the
time series database InfluxDB. Furthermore it contains an POJO `InfluxFunction` which defines the
properties which are stored in the DB.

The package `dynamodb` contains a database writer which stores the function invocations to a
DynamoDB table hosted on AWS.

The package `functioninvocation` contains a simulator, which simulates multiple runs of multiple
functions. Furthermore, it defines an interface for the different database writers which is
implemented in the other three packages.

## Setup instructions

In order to save function invocation logs to all three databases you need to have the 
database instances running and following property files located in the `database` folder in the 
root directory:

### MySQL

database/mysql.properties

```properties
USER=username
PASSWORD=password
DB_INSTANCE=host
DB_NAME=database_name
```

### InfluxDB
database/influxdb.properties
```properties
token=token
bucket=bucket_name
org=org_name
url=url
```

### DynamoDB
database/dynamodb.properties
```properties
aws_access_key_id = aws_access_key_id
aws_secret_access_key = aws_secret_access_key
table = table_name
```



