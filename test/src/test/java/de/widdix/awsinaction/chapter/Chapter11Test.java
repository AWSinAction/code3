package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter11Test extends ACloudFormationTest {

    @Test
    public void testDiscourse() {
        final String stackName = "chapter11-discourse-" + this.random8String();
        final String adminEmailAddress = "team@widdix.de";
        try {
            this.createStack(stackName, "chapter11/discourse.yaml",
                    new Parameter().withParameterKey("AdminEmailAddress").withParameterValue(adminEmailAddress)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testRedisMinimal() {
        final String stackName = "chapter11-redis-minimal-" + this.random8String();
        final String vpc = this.getDefaultVPC().getVpcId();
        final String subnetA = this.getDefaultSubnets().get(0).getSubnetId();
        final String subnetB = this.getDefaultSubnets().get(0).getSubnetId();
        try {
            this.createStack(stackName, "chapter11/redis-minimal.yaml",
                    new Parameter().withParameterKey("VPC").withParameterValue(vpc),
                    new Parameter().withParameterKey("SubnetA").withParameterValue(subnetA),
                    new Parameter().withParameterKey("SubnetB").withParameterValue(subnetB)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testMemoryDBMinimal() {
        final String stackName = "chapter11-memorydb-minimal-" + this.random8String();
        try {
            this.createStack(stackName, "chapter11/memorydb-minimal.yaml");
        } finally {
            this.deleteStack(stackName);
        }
    }

}
