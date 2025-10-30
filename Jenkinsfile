pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
        SONARQUBE_ENV = "SonarQubeServer"
        SLACK_CHANNEL = "#jenkins"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📥 Clonando repositorio desde GitHub..."
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                echo "🧪 Ejecutando pruebas unitarias con Maven..."
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco execPattern: '**/target/jacoco.exec'
                }
                success {
                    echo "✅ Pruebas unitarias exitosas."
                }
                failure {
                    echo "❌ Fallaron las pruebas unitarias."
                    slackSend(channel: env.SLACK_CHANNEL, message: "❌ *Build fallido:* pruebas unitarias no pasaron en ${env.JOB_NAME} #${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Code Quality - SonarQube') {
            when {
                expression { fileExists('sonar-project.properties') }
            }
            steps {
                echo "🔍 Ejecutando análisis de código con SonarQube..."
                withSonarQubeEnv(env.SONARQUBE_ENV) {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Integration / Selenium Tests') {
            when {
                expression { fileExists('src/test/java/selenium') }
            }
            steps {
                echo "🌐 Ejecutando pruebas Selenium (UI)..."
                sh 'mvn test -Dtest=*Selenium*'
            }
            post {
                success {
                    echo "✅ Pruebas Selenium completadas correctamente."
                }
                failure {
                    echo "⚠️ Fallaron las pruebas de Selenium."
                    slackSend(channel: env.SLACK_CHANNEL, message: "⚠️ *Selenium Tests fallaron* en ${env.JOB_NAME} #${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Reports & Notifications') {
            steps {
                echo "📊 Generando reportes finales..."
                archiveArtifacts artifacts: 'target/site/jacoco/index.html', allowEmptyArchive: true
                slackSend(channel: env.SLACK_CHANNEL, message: "✅ *Build exitoso:* ${env.JOB_NAME} #${env.BUILD_NUMBER} - Todos los tests pasaron.")
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline completado exitosamente."
        }
        failure {
            echo "💥 Pipeline fallido. Revisa los logs o errores de test."
        }
    }
}
