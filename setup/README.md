Setup local environment
========

# DynamoDB
1. Download LocalDynamoDB from [http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.zip]
   and extract to local path.
2. Download and install nodejs from [http://www.nodejs.org/download/]
3. Run the below command in the LocalDynamoDB home path.
```
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -inMemory
```
4. Run gradle task "dynamodb" of setup project.
```
gradle setup:dynamodb
```
