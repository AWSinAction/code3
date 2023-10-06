package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter04Test extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "chapter4-" + this.random8String();
        final String vpcId = this.getDefaultVPC().getVpcId();
        final String subnetId = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter04/virtualmachine.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpcId),
                    new Parameter().withParameterKey("Subnet").withParameterValue(subnetId)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
