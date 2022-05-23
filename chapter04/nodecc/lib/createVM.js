const AWS = require('aws-sdk');
const ec2 = new AWS.EC2({
  region: 'us-east-1'
});

module.exports = (amiId, subnetId, cb) => {
  ec2.runInstances({
    IamInstanceProfile: {
      Name: 'ec2-ssm-core'
    },
    ImageId: amiId,
    MinCount: 1,
    MaxCount: 1,
    InstanceType: 't2.micro',
    SubnetId: subnetId
  }, (err) => {
    if (err) {
      cb(err);
    } else {
      cb(null);
    }
  });
};
