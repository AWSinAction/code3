package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

public class Chapter13Test extends ACloudFormationTest {

    @Test
    public void testRecovery() {
        final String stackName = "chapter13-recovery-" + this.random8String();
        try {
            this.createStack(stackName, "chapter13/recovery.yaml",
                    new Parameter().withParameterKey("JenkinsAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testMultiAZ() {
        final String stackName = "chapter13-multiaz-" + this.random8String();
        try {
            this.createStack(stackName, "chapter13/multiaz.yaml",
                    new Parameter().withParameterKey("JenkinsAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testMultiAZEFS() {
        final String stackName = "chapter13-multiaz-efs-" + this.random8String();
        try {
            this.createStack(stackName, "chapter13/multiaz-efs.yaml",
                    new Parameter().withParameterKey("JenkinsAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testMultiAZEFSEIP() {
        final String stackName = "chapter13-multiaz-efs-eip-" + this.random8String();
        try {
            this.createStack(stackName, "chapter13/multiaz-efs-eip.yaml",
                    new Parameter().withParameterKey("JenkinsAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
