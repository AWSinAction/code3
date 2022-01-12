var bodyParser = require('body-parser');
var AWS = require('aws-sdk');
var assert = require('assert-plus');
var Jimp = require('jimp');
var fs = require('fs');

var lib = require('./lib.js');

var db = new AWS.DynamoDB({
  'region': 'us-east-1'
});
var s3 = new AWS.S3({
  'region': 'us-east-1'
});
var sqs = new AWS.SQS({
  'region': 'us-east-1'
});

var states = {
  'processed': processed
};

function getImage(id, cb) {
  db.getItem({
    'Key': {
      'id': {
        'S': id
      }
    },
    'TableName': 'imagery-image'
  }, function(err, data) {
    if (err) {
      cb(err);
    } else {
      if (data.Item) {
        cb(null, lib.mapImage(data.Item));
      } else {
        cb(new Error('image not found'));
      }
    }
  });
}

function processImage(image, cb) {
  var processedS3Key = 'processed/' + image.id + '-' + Date.now() + '.png';
  var rawFile = './tmp_raw_' + image.id;
  var processedFile = './tmp_processed_' + image.id;
  s3.getObject({
    'Bucket': process.env.ImageBucket,
    'Key': image.rawS3Key
  }, function(err, data) {
    if (err) {
      cb(err);
    } else {
      fs.writeFile(rawFile, data.Body, {'encoding': null}, function(err) {
        if (err) {
          cb(err);
        } else {
          Jimp.read(rawFile, (err, lenna) => {
            if (err) {
              throw err;
            } else {
              lenna.sepia().write(processedFile);
              fs.unlink(rawFile, function() {
                fs.readFile(processedFile, {'encoding': null}, function(err, buf) {
                  if (err) {
                    cb(err);
                  } else {
                    s3.putObject({
                      'Bucket': process.env.ImageBucket,
                      'Key': processedS3Key,
                      'ACL': 'public-read',
                      'Body': buf,
                      'ContentType': 'image/png'
                    }, function(err) {
                      if (err) {
                        cb(err);
                      } else {
                        fs.unlink(processedFile, function() {
                          cb(null, processedS3Key);
                        });
                      }
                    });
                  }
                });
              });
            }
          });
        }
      });
    }
  });
}

function processed(image, cb) {
  processImage(image, function(err, processedS3Key) {
    if (err) {
      throw err;
    } else {
      db.updateItem({
        'Key': {
          'id': {
            'S': image.id
          }
        },
        'UpdateExpression': 'SET #s=:newState, version=:newVersion, processedS3Key=:processedS3Key',
        'ConditionExpression': 'attribute_exists(id) AND version=:oldVersion AND #s IN (:stateUploaded, :stateProcessed)',
        'ExpressionAttributeNames': {
          '#s': 'state'
        },
        'ExpressionAttributeValues': {
          ':newState': {
            'S': 'processed'
          },
          ':oldVersion': {
            'N': image.version.toString()
          },
          ':newVersion': {
            'N': (image.version + 1).toString()
          },
          ':processedS3Key': {
            'S': processedS3Key
          },
          ':stateUploaded': {
            'S': 'uploaded'
          },
          ':stateProcessed': {
            'S': 'processed'
          }
        },
        'ReturnValues': 'ALL_NEW',
        'TableName': 'imagery-image'
      }, cb);
    }
  });
}

function fetchMessages(cb) {
  sqs.receiveMessage({
    QueueUrl: process.env.QueueUrl,
    MaxNumberOfMessages: 1
  }, function(err, data) {
    if (err) {
      console.log('Could not fetch message from queue', err);
      cb(err);
    } else {
      if (data.Messages && data.Messages.length > 0) {
        var task = JSON.parse(data.Messages[0].Body);
        var receiptHandle = data.Messages[0].ReceiptHandle;
        assert.string(task.imageId, 'imageId');
        assert.string(task.desiredState, 'desiredState');
        getImage(task.imageId, function(err, image) {
          if (err) {
            console.log('Could not get image', err);
            cb(err);
          } else {
            if (typeof states[task.desiredState] === 'function') {
              states[task.desiredState](image, function(err, data) {
                sqs.deleteMesage({
                  QueueUrl: process.env.QueueUrl,
                  ReceiptHandle: receiptHandle
                }, function(err, data) {
                  if (err) {
                    console.log('Could not delete message', err);
                    cb(err);
                  } else {
                    setTimeout(function() {
                      fetchMessages(cb);
                    }, 10000)
                  }
                });
              });
            } else {
              throw new Error('unsupported desiredState');
            }
          }
        });
      }
    }
  });
}

fetchMessages();
