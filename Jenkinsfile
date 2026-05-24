pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/debi201326/NotesAutomationCapstoneProject.git'
            }
        }

        stage('Clean and Build') {
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
                bat 'mvn test -Dtest=GetNotesAPITest,DeleteNoteAPITest,ResponseTimeAPITest,JMeterTest,NegativeAPITest'
            }
        }

        stage('E2E Tests') {
            steps {
                bat 'mvn test -Dtest=EndToEndTest'
            }
        }

        stage('Publish Reports') {
            steps {

                junit 'target/surefire-reports/*.xml'

                allure([
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])

                publishHTML([
                    reportDir: 'target/extent-report',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Test Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
    }

    post {

        always {
            echo 'Test Execution Completed'
        }

        success {
            echo 'Build SUCCESS'
        }

        failure {
            echo 'Build FAILED'
        }
    }
}