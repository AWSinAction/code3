packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "etherpad" {
  ami_name      = "awsinaction-etherpad-{{timestamp}}"
  tags = {
    Name = "awsinaction-etherpad"
  }
  instance_type = "t2.micro"
  region        = "us-east-1"
  source_ami_filter {
    filters = {
      name                = "amzn2-ami-hvm-2.0.*-x86_64-gp2"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["137112412989"]
  }
  ssh_username         = "ec2-user"
  ssh_interface        = "session_manager"
  communicator         = "ssh"
  iam_instance_profile = "ec2-ssm-core"
  ami_groups = ["all"]
  ami_regions = ["us-east-1"]
}

build {
  name    = "awsinaction-etherpad"
  sources = [
    "source.amazon-ebs.etherpad"
  ]
  
  provisioner "shell" {
    inline = [
      "curl -fsSL https://rpm.nodesource.com/setup_14.x | sudo bash -",
      "sudo yum install -y nodejs git",
      "sudo mkdir /opt/etherpad-lite",
      "sudo chown -R ec2-user:ec2-user /opt/etherpad-lite",
      "cd /opt",
      "git clone --depth 1 --branch 1.8.17 https://github.com/AWSinAction/etherpad-lite.git",
      "cd etherpad-lite",
      "./src/bin/installDeps.sh",
    ]
  }
}