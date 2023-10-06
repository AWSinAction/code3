package de.widdix.awsinaction.chapter;

import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Chapter09Test extends ACloudFormationTest {

    @Test
    public void testTemplateDefault() {
        final String stackName = "chapter09-efs-" + this.random8String();
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(stackName, "chapter09/efs.yaml");
                this.updateStack(stackName, "chapter09/efs-backup.yaml");
            } finally {
                this.deleteStack(stackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

    @Test
    public void testTemplateBackup() {
        final String stackName = "chapter09-efs-backup-" + this.random8String();
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(stackName, "chapter09/efs-backup.yaml");
            } finally {
                this.deleteStack(stackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

    @Test
    public void testTemplateProvisioned() {
        final String stackName = "chapter09-efs-provisioned-" + this.random8String();
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(stackName, "chapter09/efs-provisioned.yaml");
            } finally {
                this.deleteStack(stackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }
    
}
