package de.widdix.awsinaction;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public abstract class ACliTest extends AAWSTest {

    private String read(final InputStream is) throws Exception {
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        final StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void printCommand(final String[] command) {
        for (final String s : command) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
    }

    protected final String exec(final String[] command) throws Exception {
        this.printCommand(command);
        final ProcessBuilder pb = new ProcessBuilder(command);
        if (System.getenv().containsKey("AWS_REGION")) {
            pb.environment().put("AWS_DEFAULT_REGION", System.getenv("AWS_REGION"));
        }
        if (System.getenv().containsKey("AWS_PROFILE")) {
            pb.environment().put("AWS_DEFAULT_PROFILE", System.getenv("AWS_PROFILE"));
        }
        final AWSCredentials credentials = this.credentialsProvider.getCredentials();
        pb.environment().put("AWS_ACCESS_KEY_ID", credentials.getAWSAccessKeyId());
        pb.environment().put("AWS_SECRET_ACCESS_KEY", credentials.getAWSSecretKey());
        if (credentials instanceof AWSSessionCredentials) {
            pb.environment().put("AWS_SESSION_TOKEN", ((AWSSessionCredentials)credentials).getSessionToken());
        }
        final Process p = pb.start();
        final String stdout = this.read(p.getInputStream());
        final String stderr = this.read(p.getErrorStream());
        System.out.print(stdout);
        System.out.println();
        if (stderr.length() > 0) {
            System.err.print(stderr);
            System.out.println();
        }
        final int exitCode = p.waitFor();
        if (exitCode != 0) {
            throw new Error("exec terminated with exit code: " + exitCode);
        }
        return stdout;
    }

    protected final void waitFor(final String expectedStdout, final String[] command) throws Exception {
        final long start = System.currentTimeMillis();
        final long end = start + TimeUnit.MINUTES.toMillis(60);
        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(5000);
            } catch (final InterruptedException e) {
                // continue
            }
            final String stdout = this.exec(command);
            if (stdout.equals(expectedStdout)) {
                return;
            }
        }
        throw new RuntimeException("waitFor timed out");
    }

}
