package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter02Test extends ACloudFormationTest {

    @Test
    public void testTemplate() {
        final String stackName = "chapter2-" + this.random8String();
        try {
            this.createStack(stackName, "chapter02/template.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
