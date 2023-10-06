package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter18Test extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "chapter18-" + this.random8String();
        final String applicationId = this.random8String();
        try {
            this.createStack(stackName, "chapter18/notea.yaml",
                    new Parameter().withParameterKey("ApplicationID").withParameterValue(applicationId),
                    new Parameter().withParameterKey("Password").withParameterValue(this.random8String())
            );
        } finally {
            this.emptyBucket("awsinaction-notea-" + applicationId);
            // While the application is running, it will write to the bucket, as a workaround, we are deleting the bucket.
            this.deleteBucket("awsinaction-notea-" + applicationId);
            this.deleteStack(stackName);
        }
    }

}
