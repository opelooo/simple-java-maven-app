node {
    properties([
        pipelineTriggers([pollSCM('*/2 * * * *')]) // Memeriksa perubahan setiap 2 menit
    ])
    
    // Docker setup
    def mavenImage = 'maven:3.9.0'

    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            docker.image(mavenImage).inside {
                sh 'mvn -B -DskipTests clean package -e'
            }
        }

        stage('Test') {
            docker.image(mavenImage).inside {
                sh 'mvn test'
            }
            junit 'target/surefire-reports/*.xml'
        }

        stage('Deliver') {
            docker.image(mavenImage).inside {
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