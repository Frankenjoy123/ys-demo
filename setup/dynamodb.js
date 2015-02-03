/**
 * Created by Lijian on 2015/2/2.
 */
var program = require('commander');
var AWS = require('aws-sdk');

var createTables = require('./lib/dynamodbHelper').createTables;

var config = {};

program
  .version('0.0.1')
  .option('-l, --local', 'if it\'s local environment')
  .option('-e, --endpoint <value>', 'specific endpoint for local')
  .option('-r, --region <value>', 'region')
  .parse(process.argv);


if (program.local) {
  AWS.config.update({accessKeyId: 'AKIAJAMFJ5DUPOD426CQ', secretAccessKey: 'secret'});
  config.endpoint = program.endpoint || 'http://localhost:8000/';
  config.region = program.region || 'local-1';
} else {
  AWS.config.credentials = new AWS.SharedIniFileCredentials({profile: 'dynamodb'});
  config.region = program.region || 'us-west-2';
}

var dynamodb = new AWS.DynamoDB(config);

//create tables
createTables(dynamodb);

