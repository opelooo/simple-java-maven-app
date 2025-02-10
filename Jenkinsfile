node {
    properties([
        pipelineTriggers([pollSCM('*/2 * * * *')]) // Memeriksa perubahan setiap 2 menit
    ])

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

        // Kriteria 4: Tambahkan Manual Approval Stage sebelum Deploy
        stage('Manual Approval') {
            input message: 'Lanjutkan ke tahap Deploy?', ok: 'Proceed'
        }

        // Kriteria 2: Stage Deploy
        stage('Deploy') {
            docker.image(mavenImage).inside {
                sh './jenkins/scripts/deliver.sh'
            }
            // Kriteria 3: menjeda eksekusi pipeline agar aplikasi bisa tetap berjalan selama 1 menit
            sleep time: 60, unit: 'SECONDS'
        }
        
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        
    }
}
