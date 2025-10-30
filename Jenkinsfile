pipeline {
    agent any
    
    tools {
        maven 'M3'
        jdk 'Java17'
    }
    
    environment {
        GITHUB_REPO = 'https://github.com/MariaLazaroVelarde/PRS-back.git'
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "${GITHUB_REPO}"
            }
        }

        stage('Build') {
            steps {
                echo '⚙️ Compilando el proyecto...'
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                echo '🧪 Ejecutando pruebas unitarias...'
                sh 'mvn test -Dspring.profiles.active=test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    publishCoverage adapters: [[parser: 'JACOCO', path: 'target/site/jacoco/jacoco.xml']]
                }
            }
        }

        stage('Integration Tests') {
            steps {
                echo '🔗 Ejecutando pruebas de integración...'
                sh 'mvn test -Dtest=*IntegrationTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                echo '🔍 Analizando calidad de código con SonarQube...'
                withSonarQubeEnv('MySonarQubeServer') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '✅ Pipeline ejecutado con éxito!'

            slackSend(
                channel: '#jenkins-alerts',
                color: 'good',
                message: """
                ✅ *BUILD EXITOSO*
                Proyecto: *${env.JOB_NAME}*
                Build: *#${env.BUILD_NUMBER}*
                Ver detalles: ${env.BUILD_URL}
                """
            )
        }
        failure {
            echo '❌ Pipeline falló!'

            slackSend(
                channel: '#jenkins-alerts',
                color: 'danger',
                message: """
                ❌ *BUILD FALLIDO*
                Proyecto: *${env.JOB_NAME}*
                Build: *#${env.BUILD_NUMBER}*
                Ver detalles: ${env.BUILD_URL}
                """
            )
        }
    }
}
