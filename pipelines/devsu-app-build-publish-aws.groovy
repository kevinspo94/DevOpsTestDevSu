def skipRemainingStages = true
def dockerImage
pipeline {
    agent any

    stages {
        stage('Github Checkout') {
            steps {
                git branch: 'main', changelog: false, poll: false, url: 'https://github.com/kevinspo94/demo-devops-java.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building..'
                sh 'chmod +x ./mvnw'
                sh './mvnw package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing..'
                sh './mvnw test'
            }
        }

        stage('Static Code Analysis') {
            steps {
                echo 'Analyzing..'
                sh './mvnw pmd:check'
            }
        }

        stage('Code Coverage') {
            steps {
                echo 'Analyzing..'
                sh './mvnw verify'
            }
        }

        stage('Build Docker image') {
            steps {
                echo 'Building..'
                withCredentials([
                        usernamePassword(credentialsId: 'h2-user', usernameVariable: 'H2_USERNAME', passwordVariable: 'H2_PASSWORD')
                ]){
                    script{
                        dockerImage = docker.build("demo-java-ecr", "--build-arg dbuser=$H2_USERNAME --build-arg dbpass=$H2_PASSWORD .")
                    }
                }
            }
        }

        stage('Push Docker image') {
            steps {
                echo 'Pushing....'

                withAWS(credentials: 'aws-credentials', region: 'us-west-2') {
                    sh 'aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 394013649467.dkr.ecr.us-west-2.amazonaws.com/demo-java-ecr'
                    sh 'docker tag demo-java-ecr:latest 394013649467.dkr.ecr.us-west-2.amazonaws.com/demo-java-ecr:latest'
                    sh 'docker push 394013649467.dkr.ecr.us-west-2.amazonaws.com/demo-java-ecr:latest'
                }

            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
                withAWS(credentials: 'aws-credentials', region: 'us-west-2') {
                    sh "kubectl delete secret regcred --ignore-not-found"
                    sh "kubectl create secret docker-registry regcred   --docker-server=https://394013649467.dkr.ecr.us-west-2.amazonaws.com   --docker-username=AWS   --docker-password=\$(aws ecr get-login-password)"
                    sh "kubectl apply -f ./kubernetes/aws"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}