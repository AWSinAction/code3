const AWS = require('aws-sdk');
const ec2 = new AWS.EC2({
  region: 'us-east-1'
});

module.exports = (cb) => {
  ec2.describeInstances({
    Filters: [{
      Name: 'instance-state-name',
      Values: ['pending', 'running']
    }],
    MaxResults: 10
  }, (err, data) => {
    if (err) {
      cb(err);
    } else {
      const instanceIds = data.Reservations.map(reservation => reservation.Instances.map(instance => instance.InstanceId)).flat();
      cb(null, instanceIds);
    }
  });
};
