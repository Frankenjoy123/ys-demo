/**
 * Created by  : Lijian
 * Created on  : 2015/4/26
 * Descriptions: about type please refer to http://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
 */

var params = [];

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

//logistics_path
params.push({
    TableName: 'logistics_path',
    KeySchema: [
        {
            AttributeName: 'key',
            KeyType: 'HASH'
        },
        {
            AttributeName: 'start_date_dt',
            KeyType: "RANGE"
        }
    ],
    AttributeDefinitions: [ // The names and types of all primary and index key attributes only
        {
            AttributeName: 'key',
            AttributeType: 'S' // (S | N | B) for string, number, binary
        },
        {
            AttributeName: 'start_date_dt',
            AttributeType: 'N'
        }
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 1,
        WriteCapacityUnits: 1
    }
});


module.exports.params = params;