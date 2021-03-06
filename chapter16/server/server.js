const express = require('express');
const bodyParser = require('body-parser');
const AWS = require('aws-sdk');
const { v4: uuidv4 }  = require('uuid');
const multiparty = require('multiparty');

const lib = require('./lib.js');
constdb = new AWS.DynamoDB({});
const sqs = new AWS.SQS({});
const s3 = new AWS.S3({});

const app = express();
app.use(bodyParser.json());
app.use(express.static('public'));

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

function uploadImage(image, part, response) {
  const rawS3Key = 'upload/' + image.id + '-' + Date.now();
  s3.putObject({
    'Bucket': process.env.ImageBucket,
    'Key': rawS3Key,
    'Body': part,
    'ContentLength': part.byteCount
  }, function(err, data) {
    if (err) {
      throw err;
    } else {
      db.updateItem({
        'Key': {
          'id': {
            'S': image.id
          }
        },
        'UpdateExpression': 'SET #s=:newState, version=:newVersion, rawS3Key=:rawS3Key',
        'ConditionExpression': 'attribute_exists(id) AND version=:oldVersion AND #s IN (:stateCreated, :stateUploaded)',
        'ExpressionAttributeNames': {
          '#s': 'state'
        },
        'ExpressionAttributeValues': {
          ':newState': {
            'S': 'uploaded'
          },
          ':oldVersion': {
            'N': image.version.toString()
          },
          ':newVersion': {
            'N': (image.version + 1).toString()
          },
          ':rawS3Key': {
            'S': rawS3Key
          },
          ':stateCreated': {
            'S': 'created'
          },
          ':stateUploaded': {
            'S': 'uploaded'
          }
        },
        'ReturnValues': 'ALL_NEW',
        'TableName': 'imagery-image'
      }, function(err, data) {
        if (err) {
           throw err;
        } else {
          sqs.sendMessage({
            'MessageBody': JSON.stringify({'imageId': image.id, 'desiredState': 'processed'}),
            'QueueUrl': process.env.ImageQueue,
          }, function(err) {
            if (err) {
              throw err;
            } else {
              response.redirect('/#view=' + image.id);
              response.end();
            }
          });
        }
      });
    }
  });
}

app.post('/image', function(request, response) {
  const id = uuidv4();
  db.putItem({
    'Item': {
      'id': {
        'S': id
      },
      'version': {
        'N': '0'
      },
      'created': {
        'N': Date.now().toString()
      },
      'state': {
        'S': 'created'
      }
    },
    'TableName': 'imagery-image',
    'ConditionExpression': 'attribute_not_exists(id)'
  }, function(err, data) {
    if (err) {
      throw err;
    } else {
      response.json({'id': id, 'state': 'created'});
    }
  });
});

app.get('/image/:id', function(request, response) {
  getImage(request.params.id, function(err, image) {
    if (err) {
      throw err;
    } else {
      response.json(image);
    }
  });
});

app.post('/image/:id/upload', function(request, response) {
  getImage(request.params.id, function(err, image) {
    if (err) {
      throw err;
    } else {
      const form = new multiparty.Form();
      form.on('part', function(part) {
        uploadImage(image, part, response);
      });
      form.parse(request);
    }
  });
});

app.listen(process.env.PORT || 8080, function() {
  console.log('Server started. Open http://localhost:' + (process.env.PORT || 8080) + ' with browser.');
});
