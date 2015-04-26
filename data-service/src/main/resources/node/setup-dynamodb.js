/**
 * Created by  : Lijian
 * Created on  : 2015/4/26
 * Descriptions:
 */
var program = require('commander');
var AWS = require('aws-sdk');
var params = require('./dynamodb-table-definition').params;

var config = {};

program
    .version('0.0.1')
    .option('-l, --local', 'if it\'s local environment')
    .option('-e, --endpoint <value>', 'specific endpoint for local only')
    .option('-r, --region <value>', 'region')
    .option('-n, --environment <value>', 'environment', 'dev')
    .parse(process.argv);

if (program.local) {
    AWS.config.update({accessKeyId: 'AKIAPRMKTUV3VW24UJCA', secretAccessKey: 'secret'});
    config.endpoint = program.endpoint || 'http://localhost:8000/';
    config.region = program.region || 'us-east-1';
} else {
    AWS.config.credentials = new AWS.SharedIniFileCredentials();
    config.region = program.region || 'cn-north-1';
}
var environment = program.environment;
console.log('environment: ', environment);
console.log('config: ', config);

var dynamodb = new AWS.DynamoDB(config);

//create table
for (var i = 0; i < params.length; i++) {
    params[i].TableName = environment + '-' + params[i].TableName;

    dynamodb.createTable(params[i], (function (p) {
        return function (err, data) {
            if (err) {// an error occurred
                console.log(err.message);
            }
            else {// successful response
                console.log('Table created: ', p.TableName);
            }
        };
    })(params[i]));
}

