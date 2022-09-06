# COSC 516 - Cloud Databases<br/>Lab 1 - MySQL on Amazon RDS

## Setup

Create a Amazon AWS free tier account at: [https://aws.amazon.com/free](https://aws.amazon.com/free).

The free tier account allows for free trials forever and 12-months free offers from your sign up period. You will need an email address to use. The sign-up also asks for a credit card. If you do not have a credit card, then a pre-paid credit card with a small amount should work.

## AWS Console

Login to AWS. In the AWS console, select `Database` then `RDS`.

![AWS Console](img/2_aws_console_rds.png)
![AWS RDS Dashboard](img/2b_aws_rds_dashboard.png)

## Amazon RDS

Click on `Create database`. Select `MySQL` as the engine and `Free tier`. 

<img src="img/3_create_mysql1.png" alt="Create MySQL Database" width="600">

For database identifier use `mysql516`. Select a master password for your database. The instance configuration is `db.t3.micro`. Leave storage as `General Purpose SSD`.

<img src="img/3_create_mysql2.png" alt="Instance Configuration for MySQL Database" width="600">
<img src="img/3_create_mysql3.png" alt="Storage Configuration for MySQL Database" width="600">

For Connectivity, make sure to have public access as `Yes`. Select `Create new VPC` and `DB Subnet Group`. 

<img src="img/3_create_mysql4.png" alt="Connectivity Configuration for MySQL Database" width="600">

Leave database authentication as `Password authentication`. Click `Create database` to create the database.  It may take a few minutes to create the database.

<img src="img/3_create_mysql5.png" alt="Authentication Configuration for MySQL Database" width="600">

## Configuring VPC and Network Access

In addition to making the database public, you must also configure the database VPC to allow inbound traffic from your machine. A [virtual private cloud (VPC)](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html) is a virtual network that is similar to a physical network. An administrator defines subnets, IP addresses, routing, and firewall information for their VPC. A database instance is created in a particular VPC, and the VPC must be configured to allow network access to it.

Once the databsae is created, click on the database identifier to get to an overview screen. Click on the VPC security group (in the figure it is `default (sg-00bb5776c03caa8c6)`). Then select `Inbound rules`. 

<img src="img/4_security_group.png" alt="Configure VPC security group" width="600">
<img src="img/4b_security_group_config.png" alt="Configure VPC security group with Inbound IP Rule" width="600">

Click on `Edit the inbound rules`. In the next screen, `Add rule` that allows all traffic from your IP address. Only your machine will have access to the database. You can add other rules as required.

<img src="img/4c_vpc_add_inbound_ip.png" alt="VCP Add Inbound IP" width="600">

## Connecting to the Database

Connecting to the database can be done using MySQL command line or using the MySQL Workbench GUI. For more info, see [Connecting to MySQL on AWS RDS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_ConnectToInstance.html). If you have connection issues, it is most likely related to the VPC configuration (make sure to add your IP for inbound access). AWS provides a [troubleshooting guide](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_Troubleshooting.html#CHAP_Troubleshooting.Connecting).

### Accessing MySQL using SQuirreL SQL
[SQuirreL](https://squirrel-sql.sourceforge.io/) is an open source graphical query tool capable of querying any JDBC-accessible database including Oracle, MySQL, and SQL Server.

Start up SQuirreL. Register our MySQL server with the information:

```
Name: 516_MySQL
Login name: admin
Password: (password used when created in AWS)
Host name: (see RDS console)
Port: (leave blank for default)
Database: (leave blank)
```
<img src="img/5_squirrel_config.png" alt="Configuration for SQuirreL SQL Connection" width="300">

## Tasks

To test your database, write Java code using [Visual Studio Code](https://code.visualstudio.com/). The file to edit is `MySQLonAWS.java`.  The test file is `TestMySQLonAWS.java`.  Fill in the methods requested (search for **TODO**).  Marks for each method are below.  You receive the marks if you pass the JUnit tests AND have followed the requirements asked in the question (including documentation and proper formatting).

- +1 mark - Method `connect()` to make a connection to the database.
- +1 mark - Method `connectSSL()` to make a secure connection to the database. Requires updating database configuration on RDS.
- +1 mark - Method `close()` to close the connection to the database.
- +1 mark - Method `drop()` to drop the table "person" that we will be using.
- +2 marks - Method `create()` to create a "person" table with fields:
  	- id - integer, must auto-increment
	- name - variable character field up to size 40
	- salary - must hold up to 99,999,999.99 exactly
	- birthdate - date
	- last_update - timestamp	
- +3 marks - Method `insert()` to add the following records.  **You must use PreparedStatements to get full marks.**	

```
name, salary, birthdate, last_update
1, Ann Alden, 123000.00, 1986-03-04, 2022-01-04 11:30:30.0
2, Bob Baron, 225423.00, 1993-12-02, 2022-01-04 12:30:25.0
3, Chloe Cat, 99999999.99, 1999-01-15, 2022-01-04 12:25:45.0
4, Don Denton, 91234.24, 2004-08-03, 2022-01-04 12:45:00.0
5, Eddy Edwards, 55125125.25, 2003-05-17, 2022-01-05 23:00:00.0
```

- +1 mark - Write the method `delete()` to delete the person with name `'Bob Baron'`.</li>
- +2 marks - Write the method `query1()` that returns the person name and salary where rows are sorted by salary descending.</li>
- +2 marks - Write the method `query2()` that returns the person's last name and salary if the person's salary is greater than the average salary of all people.</li>
- +2 marks - Write the method `query3()` that returns all fields of a pair of people where a pair of people is returned if the last_update field of their records have been updated less than an hour apart. Do not duplicate pairs.  Example: Only show (Ann, Bob) and not also (Bob, Ann).</li>

**Total Marks: 20**

## Bonus Marks:

- +3 bonus marks for configuring multi-AZ fail-over and testing query when have failure (requires not using free-tier)
- 

## Submission

The lab can be marked immediately by the professor or TA by showing the output of the JUnit tests and by a quick code review.  Otherwise, submit the URL of your GitHub repository on Canvas. **Make sure to commit and push your updates to GitHub.**

