const AWS = require('aws-sdk');
var { v4: uuidv4 }  = require('uuid');
const config = require('./config.json');
const sqs = new AWS.SQS({});

if (process.argv.length !== 3) {
  console.log('URL missing');
  process.exit(1);
}

const id = uuidv4();
const body = {
  id: id,
  url: process.argv[2]
};

sqs.sendMessage({
  MessageBody: JSON.stringify(body),
  QueueUrl: config.QueueUrl
}, (err) => {
  if (err) {
    console.log('error', err);
  } else {
    console.log('PNG will be soon available at http://' + config.Bucket + '.s3.amazonaws.com/' + id + '.png');
  }
});
