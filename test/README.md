# AWS in Action (3rd edition) Code tests

Tests for https://github.com/AWSinAction/code3

The goal of this tests is to ensure that our templates are always working. The test are implemented in Java 8 and run in JUnit 4.

If you run this tests, many AWS CloudFormation tests are created and **charges will apply**!

## Supported env variables

* `IAM_ROLE_ARN` if the tests should assume an IAM role before they run supply the ARN of the IAM role
* `TEMPLATE_DIR` Load templates from local disk (instead of S3 bucket `widdix-aws-cf-templates`). Must end with an `/`. See `BUCKET_NAME` as well.
* `BUCKET_NAME` Some templates are to big to be passed as a string from local disk, therefore you need to supply the name of the bucket that is used to upload templates.
* `BUCKET_REGION` **required if BUCKET_NAME is set** Region of the bucket
* `DELETION_POLICY` (default `delete`, allowed values [`delete`, `retain`]) should resources be deleted?

## Usage

### AWS credentials

The AWS credentials are passed in as defined by the AWS SDK for Java: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html

One addition is, that you can supply the env variable `IAM_ROLE_ARN` which let's the tests assume a role before they start with the default credentials.

### Region selection

The region selection works like defined by the AWS SDK for Java: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-region-selection.html

### Run all tests

```
AWS_REGION="us-east-1" mvn test
```

### Run a single test suite

to run the `TestJenkins` tests:

```
AWS_REGION="us-east-1" mvn -Dtest=TestJenkins test
```

### Run a single test

to run the `TestJenkins.testHA` test:

```
AWS_REGION="us-east-1" mvn -Dtest=TestJenkins#testHA test
```

### Load templates from local file system

```
AWS_REGION="us-east-1" BUCKET_REGION="..." BUCKET_NAME="..." TEMPLATE_DIR="/path/to/widdix-aws-cf-templates/" mvn test
```

### Assume role

This is useful if you run on a integration server like Jenkins and want to assume a different IAM role for this tests.

```
IAM_ROLE_ARN="arn:aws:iam::ACCOUNT_ID:role/ROLE_NAME" mvn test
```
