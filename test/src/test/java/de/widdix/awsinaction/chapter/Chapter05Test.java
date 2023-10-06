package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Chapter05Test extends ACloudFormationTest {

    @Test
    public void testVPCTemplate() {
        final String stackName = "chapter6-vpc-" + this.random8String();
        try {
            this.createStack(stackName, "chapter05/vpc.yaml");
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testEc2IamRoleTemplate() {
        final String stackName = "chapter6-iam-role-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter05/ec2-iamrole.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testEc2YumUpdateTemplate() {
        final String stackName = "chapter6-yum-update-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter05/ec2-yum-update.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testEc2OsUpdateTemplate() {
        final String stackName = "chapter6-os-update-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter05/ec2-os-update.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testFirewallTemplate() {
        final String stackName = "chapter6-firewall-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter05/firewall1.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
            this.updateStack(stackName, "chapter05/firewall2.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
            this.updateStack(stackName, "chapter05/firewall3.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
            this.updateStack(stackName, "chapter05/firewall4.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId),
                    new Parameter().withParameterKey("WhitelistedIpAddress").withParameterValue("10.10.10.10")
            );
            this.updateStack(stackName, "chapter05/firewall5.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
