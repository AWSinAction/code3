const express = require('express');
const AWS = require('aws-sdk');
const mu = require('mu2-updated');
const uuid = require('uuid');
const multiparty = require('multiparty');

const app = express();
const s3 = new AWS.S3({
  'region': 'us-east-1'
});

const bucket = process.argv[2];
if (!bucket || bucket.length < 1) {
  console.error('Missing S3 bucket. Start with node server.js BUCKETNAME instead.');
  process.exit(1);
}

async function listImages(response) {
  try {
    let data = await s3.listObjects({
      Bucket: bucket
    }).promise();
    let stream = mu.compileAndRender(
      'index.html',
      {
        Objects: data.Contents,
        Bucket: bucket
      }
    );
    stream.pipe(response);
  } catch (err) {
    console.error(err);
    response.status(500);
    response.send('Internal server error.');
  }
}

async function uploadImage(image, response) {
  try {
    await s3.putObject({
      Body: image,
      Bucket: bucket,
      Key: uuid.v4(),
      ACL: 'public-read',
      ContentLength: image.byteCount,
      ContentType: image.headers['content-type']
    }).promise();
    response.redirect('/');
  } catch (err) {
    console.error(err);
    response.status(500);
    response.send('Internal server error.');
  }
}

app.get('/', async (request, response) => {
  await listImages(response);
})



app.post('/upload', async (request, response) => {
  let form = new multiparty.Form();
  form.on('part', async (part) => {
    await uploadImage(part, response);
  });
  form.parse(request);
});

app.listen(8080);

console.log('Server started. Open http://localhost:8080 with browser.');
