package de.widdix.awsinaction.chapter;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import de.widdix.awsinaction.ACloudFormationTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Chapter17Test extends ACloudFormationTest {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(this.credentialsProvider).build();

    private long countObjects(final AmazonS3 s3, final String bucketName, final String marker, final long agg) {
        final ObjectListing res = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withMarker(marker).withMaxKeys(50));
        final long count = agg + res.getObjectSummaries().size();
        if (res.isTruncated()) {
            return countObjects(s3, bucketName, res.getNextMarker(), count);
        } else {
            return count;
        }
    }

    private void waitForS3BucketCount(final long expected, final String bucketName) {
        final long start = System.currentTimeMillis();
        final long end = start + TimeUnit.MINUTES.toMillis(90);
        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(15000);
            } catch (final InterruptedException e) {
                // continue
            }
            final long count = countObjects(s3, bucketName, null, 0);
            if (count == expected) {
                return;
            }
        }
        throw new RuntimeException("waitForS3BucketCount[" + bucketName + "] timed out");
    }

    private long deleteAllObjects(final String bucketName, final String marker) {
        final ObjectListing res = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withMarker(marker).withMaxKeys(25));
        final List<KeyVersion> keys = new ArrayList<>(25);
        for (final S3ObjectSummary os: res.getObjectSummaries()) {
            keys.add(new KeyVersion(os.getKey()));
        }
        final DeleteObjectsRequest req2 = new DeleteObjectsRequest(bucketName);
        req2.setKeys(keys);
        final DeleteObjectsResult res2 = s3.deleteObjects(req2);
        if (res.isTruncated()) {
            return res2.getDeletedObjects().size() + deleteAllObjects(bucketName, res.getNextMarker());
        } else {
            return res2.getDeletedObjects().size();
        }
    }

    private void waitForDeletedObjects(final long expected, final String bucketName) {
        final long start = System.currentTimeMillis();
        final long end = start + TimeUnit.MINUTES.toMillis(60);
        long deleted = 0;
        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(15000);
            } catch (final InterruptedException e) {
                // continue
            }
            deleted += this.deleteAllObjects(bucketName, null);
            if (deleted == expected) {
                return;
            }
        }
        throw new RuntimeException("waitForDeletedObjects[" + bucketName + "] timed out (" + deleted + ", " + expected + ")");
    }

    @Test
    public void testURL2PNGLoadTest() {
        final String stackName = "chapter17-url2png-loadtest-" + this.random8String();
        final String bucketName = "ch17-url2png-" + this.getAccount();
        try {
            this.createStack(stackName, "chapter17/url2png-loadtest.yaml");
            this.waitForS3BucketCount(250, bucketName);
            this.waitForDeletedObjects(250, bucketName);
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testURL2PNG() {
        final String stackName = "chapter17-url2png-" + this.random8String();
        try {
            this.createStack(stackName, "chapter17/url2png.yaml");
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testWordpress() {
        final String stackName = "chapter17-wordpress-" + this.random8String();
        try {
            this.createStack(stackName, "chapter17/wordpress.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testWordpressLoadTest() {
        final String stackName = "chapter17-wordpress-loadtest-" + this.random8String();
        try {
            this.createStack(stackName, "chapter17/wordpress-loadtest.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

    @Test
    public void testWordpressSchedule() {
        final String stackName = "chapter17-wordpress-schedule-" + this.random8String();
        try {
            this.createStack(stackName, "chapter17/wordpress-schedule.yaml",
                    new Parameter().withParameterKey("WordpressAdminPassword").withParameterValue(this.random8String())
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
