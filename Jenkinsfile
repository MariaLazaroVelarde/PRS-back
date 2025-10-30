pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'M3'
    }

    environment {
        GITHUB_REPO = 'https://github.com/MariaLazaroVelarde/PRS-back.git'
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📦 Clonando repositorio...'
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
                    echo '📄 Publicando resultados de pruebas...'
                    junit 'target/surefire-reports/*.xml'

                    // Publicar reporte de cobertura (JaCoCo)
                    script {
                        try {
                            publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')],
                                            sourceFileResolver: sourceFiles('STORE_ALL_BUILD')
                        } catch (Exception e) {
                            echo "⚠️ No se pudo publicar cobertura: ${e.message}"
                        }
                    }
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
            echo '🧹 Limpiando workspace...'
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
