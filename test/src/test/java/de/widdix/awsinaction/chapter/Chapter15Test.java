package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter15Test extends ACloudFormationTest {

    @Test
    public void testCloudFormationTemplate() {
        final String stackName = "chapter15-cloudformation-" + this.random8String();
        try {
            this.createStack(stackName, "chapter15/cloudformation.yaml");
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testCodeDeployTemplate() {
        final String stackName = "chapter15-codedeploy-" + this.random8String();
        try {
            this.createStack(stackName, "chapter15/codedeploy.yaml");
        } finally {
            this.deleteStack(stackName);
        }
    }

}
