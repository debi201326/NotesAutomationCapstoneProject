pipeline {
    agent any
    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/debi201326/NotesAutomationCapstoneProject.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('UI Tests') {
            steps {
                bat 'mvn test -Dtest=LoginTest,CreateNoteTest,NegativeUITest,NegativeLoginTest'
            }
        }

        stage('API Tests') {
            steps {
                bat 'mvn test -Dtest=GetNotesAPITest,DeleteNoteAPITest,ResponseTimeAPITest,NegativeAPITest'
            }
        }

        stage('E2E Tests') {
            steps {
                bat 'mvn test -Dtest=EndToEndTest'
            }
        }

        stage('Allure Report') {
            steps {
                bat 'mvn allure:report'
            }
        }

        stage('Publish Reports') {
            steps {
                publishHTML([
                    reportDir: 'target/extent-report',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Test Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }

    }
}