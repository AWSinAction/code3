const AWS = require('aws-sdk');
const assert = require('assert-plus');
const Jimp = require('jimp');
const fs = require('fs/promises');

const lib = require('./lib.js');

const db = new AWS.DynamoDB({});
const s3 = new AWS.S3({});
const sqs = new AWS.SQS({});

const states = {
  'processed': processed
};

async function getImage(id) {
  let res = await db.getItem({
    'Key': {
      'id': {
        'S': id
      }
    },
    'TableName': 'imagery-image'
  }).promise();
  if (res.Item) {
    return lib.mapImage(res.Item)
  } else {
    throw new Error('image not found');
  }
}

async function processImage(image) {
  let processedS3Key = 'processed/' + image.id + '-' + Date.now() + '.png';
  let rawFile = './tmp_raw_' + image.id;
  let processedFile = './tmp_processed_' + image.id;
  let data = await s3.getObject({
    'Bucket': process.env.ImageBucket,
    'Key': image.rawS3Key
  }).promise();
  await fs.writeFile(rawFile, data.Body, {'encoding': null});
  let lenna = await Jimp.read(rawFile);
  await lenna.sepia().write(processedFile);
  await fs.unlink(rawFile);
  let buf = await fs.readFile(processedFile, {'encoding': null});
  await s3.putObject({
    'Bucket': process.env.ImageBucket,
    'Key': processedS3Key,
    'ACL': 'public-read',
    'Body': buf,
    'ContentType': 'image/png'
  }).promise();
  await fs.unlink(processedFile);
  return processedS3Key;
}

async function processed(image) {
  let processedS3Key = await processImage(image);
  await db.updateItem({
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
  }).promise();
}

async function processMessage() {
  let data = await sqs.receiveMessage({
    QueueUrl: process.env.ImageQueue,
    MaxNumberOfMessages: 1
  }).promise();
  if (data.Messages && data.Messages.length > 0) {
    var task = JSON.parse(data.Messages[0].Body);
    var receiptHandle = data.Messages[0].ReceiptHandle;
    assert.string(task.imageId, 'imageId');
    assert.string(task.desiredState, 'desiredState');
    let image = await getImage(task.imageId);
    if (typeof states[task.desiredState] === 'function') {
      await states[task.desiredState](image);
      await sqs.deleteMessage({
        QueueUrl: process.env.ImageQueue,
        ReceiptHandle: receiptHandle
      }).promise();
    } else {
      throw new Error('unsupported desiredState');
    }
  }
}

async function run() {
  while (true) {
    try {
    await processMessage();
    await new Promise(resolve => setTimeout(resolve, 1000));
    } catch (e) {
      console.log('ERROR', e);
    }
  }
}

run();
