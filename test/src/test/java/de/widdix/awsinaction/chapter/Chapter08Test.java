package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Chapter08Test extends ACloudFormationTest {

    @Test
    public void testEBSTemplate() {
        final String stackName = "chapter08-ebs-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter08/ebs.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    public void testInstanceStoreTemplate() {
        final String stackName = "chapter08-is-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter08/instancestore.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }
    
}
