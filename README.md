# CreditSuisseCodeTest

Code test for Credit Suisse

## Build project

To build the project run

```
gradle build
```

## Run the project

The program will create the database if it does not exist, 
read log entries from the "events.log" log files and save the events on the database

To run the code test run.

```
gradle run
```

If the database has contents, they have to be deleted before running the program again.
To drop the database, run 

```
gradle dropDb
```

To see the contents on the database run

```
gradle readDb
```