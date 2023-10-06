package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter14Test extends ACloudFormationTest {

    @Test
    public void testTemplate() {
        final String stackName = "chapter14-loadbalancer-" + this.random8String();
        try {
            this.createStack(stackName, "chapter14/loadbalancer.yaml",
                    new Parameter().withParameterKey("NumberOfVirtualMachines").withParameterValue("2")
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
