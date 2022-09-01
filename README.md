# COSC 516 - Cloud Databases<br/>Lab 1 - MySQL on Amazon RDS

## Setup

Create a Amazon AWS free tier account at: [https://aws.amazon.com/free](https://aws.amazon.com/free).

The free tier account allows for free trials forever and 12-months free offers from your sign up period. You will need an email address to use. The sign-up also asks for a credit card. If you do not have a credit card, then a pre-paid credit card with a small amount should work.

## AWS Console

Login to AWS. In the AWS console, select `Databases` then `RDS`.

## Amazon RDS

Click on `Create database`. Select `MySQL` as the engine and `Free tier`. For database identifier use `mysql516`. Select a master password for your database. The instance configuration is `db.t3.micro`. Leave storage as `General Purpose SSD`.

For Connectivity, make sure to have public access as `Yes`. Select `Create new VPC` and `DB Subnet Group`. 


Leave database authentication as `Password authentication`. Click `Create database` to create the database.  It may take a few minutes to create the database.


## Connecting to the Database



## Tasks

Starter code is available including JUnit test cases and the JDBC drivers for MySQL and PostgreSQL. 

You will repeat the same tasks for both PostgreSQL and MySQL.  The goal is to build experience with both systems and see their similarities and differences.  Expect to consult the online documentation for MySQL and PostgreSQL to help with this assignment. **Hint: Complete one of the databases first then copy the code to the other.  There are only a few changes between the two.**

The two classes you will write are `QueryMySQL.java` and `QueryPostgreSQL.java`.  The test classes are `TestQueryMySQL.java` and `TestQueryPostgreSQL.java` respectively.  You will fill in the methods requested (search for **TODO**).  Marks for each method are below.  You receive the marks if you pass the JUnit tests AND have followed the requirements asked in the question (including documentation and proper formatting).

- +1 mark - Method `connect()` to make a connection to the database.
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

**Total Marks: 30** (15 marks for each database)

## Submission

The lab can be marked immediately by the professor or TA by showing the output of the JUnit tests and by a quick code review.  Otherwise, submit the URL of your GitHub repository on Canvas. **Make sure to commit and push your updates to GitHub.**

