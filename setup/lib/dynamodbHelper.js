/**
 * Created by Lijian on 2015/2/3.
 * about type pleaes visit http://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
 */

exports.createTables = function (dynamodb) {
    var params = [];

//product_key
    params.push({
        TableName: 'product_key',
        KeySchema: [
            {
                AttributeName: 'key',
                KeyType: 'HASH'
            }
        ],
        AttributeDefinitions: [ // The names and types of all primary and index key attributes only
            {
                AttributeName: 'key',
                AttributeType: 'S' // (S | N | B) for string, number, binary
            }
        ],
        ProvisionedThroughput: {
            ReadCapacityUnits: 1,
            WriteCapacityUnits: 1
        }
    });

//product_key_batch
    params.push({
        TableName: 'product_key_batch',
        KeySchema: [
            {
                AttributeName: 'id',
                KeyType: 'HASH'
            }
        ],
        AttributeDefinitions: [ // The names and types of all primary and index key attributes only
            {
                AttributeName: 'id',
                AttributeType: 'S' // (S | N | B) for string, number, binary
            }
        ],
        ProvisionedThroughput: {
            ReadCapacityUnits: 1,
            WriteCapacityUnits: 1
        }
    });

//product
    params.push({
        TableName: 'product',
        KeySchema: [
            {
                AttributeName: 'key',
                KeyType: 'HASH'
            }
        ],
        AttributeDefinitions: [ // The names and types of all primary and index key attributes only
            {
                AttributeName: 'key',
                AttributeType: 'S' // (S | N | B) for string, number, binary
            }
        ],
        ProvisionedThroughput: {
            ReadCapacityUnits: 1,
            WriteCapacityUnits: 1
        }
    });

//product_package
    params.push({
        TableName: 'product_package',
        AttributeDefinitions: [ // The names and types of all primary and index key attributes only
            {
                AttributeName: 'key',
                AttributeType: 'S' // (S | N | B) for string, number, binary
            }
        ],
        KeySchema: [
            {
                AttributeName: 'key',
                KeyType: 'HASH'
            }
        ],

        ProvisionedThroughput: {
            ReadCapacityUnits: 1,
            WriteCapacityUnits: 1
        }
    });


    for (var i = 0; i < params.length; i++) {
        dynamodb.createTable(params[i], (function (p) {
            return function (err, data) {
                if (err) {// an error occurred
                    console.log(err);
                }
                else {// successful response
                    console.log('TableCreated: ', p.TableName);
                }
            };
        })(params[i]));
    }
};

