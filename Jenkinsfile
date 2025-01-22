node {
    properties([
        pipelineTriggers([pollSCM('H/2 * * * *')]) // Memeriksa perubahan setiap 2 menit
    ])
    
    // sh 'docker images -a'
    // sh 'docker ps -a'
    // sh 'docker image rm maven:3.9.0'
    // Docker setup
    def mavenImage = 'maven:3.9.2'

    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            docker.image(mavenImage).inside('-v /root/.m2:/root/.m2') {
                sh 'mkdir -p /root/.m2/repository'
                sh 'chmod -R 777 /root/.m2'  // Give read/write/execute permissions
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            docker.image(mavenImage).inside('-v /root/.m2:/root/.m2') {
                sh 'mvn test'
            }
            junit 'target/surefire-reports/*.xml'
        }

        stage('Deliver') {
            docker.image(mavenImage).inside('-v /root/.m2:/root/.m2') {
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