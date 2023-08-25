def skipRemainingStages = true
pipeline {

    parameters {
        choice(name: 'action', choices: 'create\ndestroy', description: 'Create/update or destroy the eks cluster.')
        string(name: 'cluster', defaultValue : 'eks-demo-java-devsu', description: "EKS cluster name")
    }

    agent any

    stages {
        stage('Github Checkout') {
            steps {
                git branch: 'main', changelog: false, poll: false, url: 'https://github.com/kevinspo94/demo-devops-java.git'
            }
        }

        stage('Setup') {
            steps {
                script {
                    currentBuild.displayName = "#" + env.BUILD_NUMBER + " " + params.action + " eks-" + params.cluster
                    plan = params.cluster + '.plan'
                }
            }
        }
        stage('Set Terraform path') {
            steps {
                /*script {
                    def tfHome = tool name: 'terraform'
                    env.PATH = "${tfHome}:${env.PATH}"
                }*/
                sh 'terraform -version'
            }
        }
        stage('TF Plan') {
            when {
                expression { params.action == 'create' }
            }
            steps {
                script {
                    dir('terraform') {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                            sh """
                                terraform init
                                terraform workspace new ${params.cluster} || true
                                terraform workspace select ${params.cluster}
                                terraform plan \
                                    -var cluster_name=${params.cluster} \
                                    -out ${plan}
                                echo ${params.cluster}
                            """
                        }
                    }
                }
            }
        }
        stage('TF Apply') {
            when {
                expression { params.action == 'create' }
            }
            steps {
                script {
                    dir('terraform') {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                            if (fileExists('$HOME/.kube')) {
                                echo '.kube Directory Exists'
                            } else {
                                sh 'mkdir -p $HOME/.kube'
                            }
                            sh """
                                terraform apply -input=false -auto-approve ${plan}
                                terraform output -raw kubeconfig > $HOME/.kube/config
                            """
                            withCredentials([string(credentialsId: 'server-admin-user', variable: 'ADMIN_PASSWORD')]) { //set SECRET with the credential content
                                sh 'echo ${ADMIN_PASSWORD} | sudo -kS chown $(id -u):$(id -g) $HOME/.kube/config'
                            }
                            sleep 30
                            sh 'kubectl get nodes'
                        }
                    }
                }
            }
        }
        stage('TF Destroy') {
            when {
                expression { params.action == 'destroy' }
            }
            steps {
                script {
                    dir('terraform') {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                            sh """
                                terraform workspace select ${params.cluster}
                                terraform destroy -auto-approve
                            """
                        }
                    }
                }
            }
        }
    }
}