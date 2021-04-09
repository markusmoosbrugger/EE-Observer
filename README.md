[![EE DB CI Java Repository](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml/badge.svg)](https://github.com/markusmoosbrugger/EE-DB-Writer/actions/workflows/gradle.yml)

# EE-DB-Writer

Project containing database writers to log function invocation properties to the databases MySQL,
InfluxDB and DynamoDB.

The package `logback` contains a simple logger which uses a logback DB appender to store function
invocation parameters such as function id, function type, success and execution time to a 
relational MySQL database. Here I tested the logging functionality with a local MySQL database, 
as well as a database hosted in AWS and Google.

The package `influxdb` contains a database writer which stores similar function parameters to the
time series database InfluxDB. Furthermore it contains an POJO `InfluxFunction` which defines the
properties which are stored in the DB.

The package `dynamodb` contains a database writer which stores the function invocations to a
DynamoDB table hosted on AWS.

The package `functioninvocation` contains a simulator, which simulates multiple runs of multiple
functions. Furthermore, it defines an interface for the different database writers which is
implemented in the other three packages.

## Setup

In order to save function invocation logs to all three databases you need to have the
database instances running, and the property files containing the database credentials
need to be located in the respective folders within the `config/database` folder.

### Database Setup

For the setup of the individual database instances you can find instructions in the respective 
database folders:

- [MySQL hosted on AWS](config/database/mysql/setup_instructions_aws_mysql.txt)
- [MySQL hosted on Google](config/database/mysql/setup_instructions_google_mysql.txt)
- [MySQL hosted on localhost](config/database/mysql/setup_instructions_local_mysql.txt)
- [InfluxDB hosted on InfluxDB Cloud](config/database/influxdb/setup_instructions_influxdb.txt)
- [DynamoDB hosted on AWS](config/database/dynamodb/setup_instructions_dynamodb.txt)

#### Specifying database credentials

The database credentials are read from three different `.properties` files located in the 
respective database folders. The directory structure already exists, but you need to create the 
individual properties files yourself. **Please make sure that the database credentials are not 
committed.** The respective files should already be excluded in the `.gitignore` file by the entry 
`/config/database/*/*.properties`.

##### MySQL

`config/database/mysql/mysql.properties`

```properties
user=username
password=password
db_instance=database_url | ip_address | localhost
db_port=database_port (default 3306)
db_name=database_name
```

##### InfluxDB
`config/database/influxdb/influxdb.properties`

```properties
token=token
bucket=bucket_name
org=org_name
url=url
```

##### DynamoDB
`config/database/dynamodb/dynamodb.properties
`
```properties
aws_access_key_id=aws_access_key_id
aws_secret_access_key=aws_secret_access_key
table=table_name
region=region
```

### Project Setup
1. Using the terminal, switch to the root directory of the project and run `gradle build` to 
   download the dependencies and build the project.


#### Running simulator

1. Execute the main method of `Runner` to start the database simulator which simulates database 
   invocation entries and stores it in all three databases (Note: the database setup needs to be completed before
   this step).

2. (Optional): You can change the number of simulated functions and the number of simulated runs
   in the main method of the `Runner`. Additionally, you can change in the `Runner` class which 
   individual loggers are added to the simulator, e.g. if you like to try out a single logger.
   
#### Running within Apollo

1. Clone the repository [EE-Demo](https://github.com/markusmoosbrugger/EE-Demo) and have a look 
   at the description how to run the demo
   
2. In the Opt4J configurator you can define if the function enactments should be logged 
   (`logFunctionProperties`) and which logger you would like to use (`useLogback`, `useInfluxDB`,
   `useDynamoDB`). There is also the option to select multiple loggers at the same time.
   Please make sure that the path to your logger configurations/properties is 
   set correctly.
   
3. Click on Run. The function invocations should now be logged with your selected loggers.


## Contact

E-Mail: [Markus Moosbrugger](mailto:markus.moosbrugger@outlook.com)
