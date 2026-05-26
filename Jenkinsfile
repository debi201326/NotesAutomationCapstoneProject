pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/debi201326/NotesAutomationCapstoneProject.git'
            }
        }

        stage('Clean and Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('UI and API Tests') {
            parallel {

                stage('UI Tests') {
                    steps {
                        bat 'mvn test -Dtest=LoginTest,CreateNoteTest,NegativeUITest,NegativeLoginTest -Dproject.build.directory=target-ui'
                    }
                }

                stage('API Tests') {
                    steps {
                        bat 'mvn test -Dtest=GetNotesAPITest,DeleteNoteAPITest,ResponseTimeAPITest,JMeterTest,NegativeAPITest -Dproject.build.directory=target-api'
                    }
                }

            }
        }

        stage('E2E Tests') {
            steps {
                bat 'mvn test -Dtest=EndToEndTest'
            }
        }

        stage('Publish Reports') {
            steps {
                junit 'target-ui/surefire-reports/*.xml'
                junit 'target-api/surefire-reports/*.xml'
                junit 'target/surefire-reports/*.xml'

                allure([
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
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