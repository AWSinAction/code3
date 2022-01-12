packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "imagery" {
  ami_name      = "awsinaction-imagery-{{timestamp}}"
  tags = {
    Name = "awsinaction-imagery"
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
  ssh_username = "ec2-user"
  ami_groups = ["all"]
  ami_regions = ["us-east-1"]
}

build {
  name    = "awsinaction-imagery"
  sources = [
    "source.amazon-ebs.imagery"
  ]

  provisioner "file" {
    source = "./"
    destination = "/home/ec2-user/"
  }
  
  provisioner "shell" {
    inline = [
      "curl -sL https://rpm.nodesource.com/setup_14.x | sudo bash -",
      "sudo yum update",
      "sudo yum install -y nodejs cairo-devel libjpeg-turbo-devel giflib-devel",
      "cd server/ && npm install && cd -",
      "cd worker/ && npm install && cd -"
    ]
  }
}