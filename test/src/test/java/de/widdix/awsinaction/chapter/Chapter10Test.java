package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter10Test extends ACloudFormationTest {

    @Test
    public void testTemplateDefault() {
        final String stackName = "chapter10-default-" + this.random8String();
        try {
            this.createStack(stackName, "chapter10/template.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testTemplateSnapshot() {
        final String stackName = "chapter10-snapshot-" + this.random8String();
        try {
            this.createStack(stackName, "chapter10/template-snapshot.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testTemplateMultiAZ() {
        final String stackName = "chapter10-multiaz-" + this.random8String();
        try {
            this.createStack(stackName, "chapter10/template-multiaz.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
