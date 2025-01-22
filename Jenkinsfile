node {
    properties([
        pipelineTriggers([pollSCM('H/2 * * * *')]) // Memeriksa perubahan setiap 2 menit
    ])
    
    sh 'docker images -a'
    sh 'docker ps -a'
    // Docker setup
    def mavenImage = 'maven:3.9.2'

    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            docker.image(mavenImage) {
                sh 'mvn -B -DskipTests clean package -e'
            }
        }

        stage('Test') {
            docker.image(mavenImage) {
                sh 'mvn test'
            }
            junit 'target/surefire-reports/*.xml'
        }

        stage('Deliver') {
            docker.image(mavenImage) {
                sh './jenkins/scripts/deliver.sh'
            }
        }
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        // Ensure proper cleanup if needed
    }
}