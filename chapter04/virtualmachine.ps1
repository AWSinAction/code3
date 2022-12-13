# To start PowerShell scripts first start PowerShell as Administrator
# to allow unsigned scripts to be executed. To do so enter:
# Set-ExecutionPolicy Unrestricted
# Close the PowerShell window (you don't need Administrator privileges to run the scripts)
#
# You also need to install the AWS Command Line Interface from http://aws.amazon.com/cli/
#
# Right click on the *.ps1 file and select Run with PowerShell
$ErrorActionPreference = "Stop"

$AMIID=aws ec2 describe-images --filters "Name=name,Values=amzn2-ami-hvm-2.0.202*-x86_64-gp2" --query "Images[0].ImageId" --output text
$VPCID=aws ec2 describe-vpcs --filter "Name=isDefault, Values=true" --query "Vpcs[0].VpcId" --output text
$SUBNETID=aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPCID" --query "Subnets[0].SubnetId" --output text
$INSTANCEID=aws ec2 run-instances --image-id $AMIID --instance-type t2.micro --subnet-id $SUBNETID --iam-instance-profile "Name=ec2-ssm-core" --query "Instances[0].InstanceId" --output text
Write-Host "waiting for $INSTANCEID ..."
aws ec2 wait instance-running --instance-ids $INSTANCEID
Write-Host "$INSTANCEID is up and running"
Write-Host "connect to the instance using Session Manager"
Write-Host "https://console.aws.amazon.com/systems-manager/session-manager/$INSTANCEID"
Write-Host "Press [Enter] key to terminate $INSTANCEID ..."
Read-Host
aws ec2 terminate-instances --instance-ids $INSTANCEID
Write-Host "terminating $INSTANCEID ..."
aws ec2 wait instance-terminated --instance-ids $INSTANCEID
Write-Host "done."
