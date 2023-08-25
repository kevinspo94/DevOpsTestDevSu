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
                        dockerImage = docker.build("kevinspo94/demo-dev-ops", "--build-arg dbuser=$H2_USERNAME --build-arg dbpass=$H2_PASSWORD .")
                    }
                }
            }
        }

        stage('Push Docker image') {
            steps {
                echo 'Pushing....'
                withCredentials([
                        usernamePassword(credentialsId: 'docker-hub-crs', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')
                ]){
                    sh "docker login -u '${DOCKER_USER}' -p '${DOCKER_PASSWORD}'"
                    script{
                        dockerImage.push("${env.BUILD_NUMBER}")
                        dockerImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
                sh "kubectl apply -f ./kubernetes/docker"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}