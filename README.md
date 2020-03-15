# BATCH PROCESSOR

This is a simple batch project that explains behaviour of the batch process

First step of the batch will:
1. `READ` data from `sample-data.cvs` which is in format ID,FIRST_NAME,LAST_NAME,GENDER,AGE
2. `PROCESS` data in big capitals
3. `WRITE` data into the database

Second step will:
1. `READ` from database all PENDING entries
2. `PROCESS` data in a way to calculate years to retirement if MEN retire with 68 and WOMEN with 63
3. `WRITE` data in `result-data.cvs` which is in format ID,FIRST_NAME,LAST_NAME ,AGE

TODO: test restart if step 2 fails

######sample-data.cvs
```
ID,FIRST_NAME,LAST_NAME,AGE
1,Mathew,Robertson,33
2,Joe,Stone,17
3,Justin,Falconer,42
4,Stefan,Jones,24
```


Good example: 
https://aboullaite.me/spring-batch-tutorial-with-spring-boot/
https://spring.io/blog/2017/02/01/spring-tips-spring-batch


