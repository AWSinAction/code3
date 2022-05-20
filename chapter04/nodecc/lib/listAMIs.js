const AWS = require('aws-sdk');
const ec2 = new AWS.EC2({
  region: 'us-east-1'
});

module.exports = (cb) => {
  ec2.describeImages({
    Filters: [{
      Name: 'name',
      Values: ['amzn2-ami-hvm-2.0.202*-x86_64-gp2']
    }]
  }, (err, data) => {
    if (err) {
      cb(err);
    } else {
      const amiIds = data.Images.map(image => image.ImageId);
      const descriptions = data.Images.map(image => image.Description);
      cb(null, {amiIds: amiIds, descriptions: descriptions});
    }
  });
};
