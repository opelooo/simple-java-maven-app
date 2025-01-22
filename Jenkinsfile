node {
    properties([
        pipelineTriggers([pollSCM('H/2 * * * *')]) // Check for changes every 2 minutes
    ])

    // Docker setup
    def mavenImage = 'maven:3.9.2'

    try {
        stage('Checkout') {
            // Checkout the code from version control
            checkout scm
        }

        stage('Build') {
            docker.image(mavenImage).inside('-v /tmp/.m2:/root/.m2') {
                // Set a custom local repository location and run Maven
                sh 'mvn -B -DskipTests -Dmaven.repo.local=/tmp/.m2/repository clean package'
            }
        }

        stage('Test') {
            docker.image(mavenImage).inside('-v /tmp/.m2:/root/.m2') {
                sh 'mvn test'
            }
            // Always run junit collection after test
            junit 'target/surefire-reports/*.xml'
        }

        stage('Deliver') {
            docker.image(mavenImage).inside('-v /tmp/.m2:/root/.m2') {
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
