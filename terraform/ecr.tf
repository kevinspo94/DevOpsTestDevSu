
resource "aws_ecr_repository" "demo_java_ecr" {
  name         = "demo-java-ecr"
  force_delete = true
}

data "aws_caller_identity" "current" {}

locals {
  ecr_url = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/demo-java"
}

resource "aws_ssm_parameter" "ecr" {
  name = "/demo-java/ecr"
  value = local.ecr_url
  type  = "String"
}

resource "local_file" "ecr" {
  filename = "${path.module}/../ecr-url.txt"
  content = local.ecr_url
}

output "repository_base_url" {
  value = local.ecr_url
}