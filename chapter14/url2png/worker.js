const fs = require('fs');
const AWS = require('aws-sdk');
const puppeteer = require('puppeteer');
const config = require('./config.json');
const sqs = new AWS.SQS();
const s3 = new AWS.S3();

async function acknowledge(message) {
  await sqs.deleteMessage({
    QueueUrl: config.QueueUrl,
    ReceiptHandle: message.ReceiptHandle
  }).promise();
};

async function process(message) {
  const body = JSON.parse(message.Body);
  const browser = await puppeteer.launch();
  const page = await browser.newPage();

  await page.goto(body.url);
  page.setViewport({ width: 1024, height: 768})
  const screenshot = await page.screenshot();
  
  await s3.upload({
    Bucket: config.Bucket,
    Key: `${body.id}.png`,
    Body: screenshot,
    ContentType: 'image/png',
    ACL: 'public-read',
  }).promise();

  await browser.close();
};

async function receive() {
  const result = await sqs.receiveMessage({
    QueueUrl: config.QueueUrl,
    MaxNumberOfMessages: 1,
    VisibilityTimeout: 120,
    WaitTimeSeconds: 10
  }).promise();

  if (result.Messages) {
    return result.Messages[0]
  } else {
    return null;
  }
};

async function run() {
  // Workaround as AWS enabled public access block for all new buckets in April 2023
  console.log('Disabling public access block.');
  await s3.putPublicAccessBlock({
    Bucket: config.Bucket,
    PublicAccessBlockConfiguration: {
      BlockPublicAcls: false,
      BlockPublicPolicy: false,
      IgnorePublicAcls: false,
      RestrictPublicBuckets: false
    }
  }).promise();
  console.log('Enabling ACLs.');
  await s3.putBucketOwnershipControls({
    Bucket: config.Bucket,
    OwnershipControls: {
      Rules: [{
        ObjectOwnership: 'BucketOwnerPreferred'
      }]
    }
  }).promise();
  while(true) {
    const message = await receive();
    if (message) {
      console.log('Processing message', message);
      await process(message);
      await acknowledge(message);
    }
    await new Promise(r => setTimeout(r, 1000));
  }
};

run();
