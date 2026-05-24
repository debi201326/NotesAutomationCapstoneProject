pipeline {

    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        ALLURE_RESULTS = 'target/allure-results'
        EXTENT_REPORT  = 'target/extent-report/ExtentReport.html'
        SCREENSHOT_DIR = 'target/screenshots'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '===== Checking out code ====='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '===== Building project ====='
                bat 'mvn clean compile -q'
            }
        }

        stage('UI Tests') {
            steps {
                echo '===== Running UI Tests ====='
                bat 'mvn test -Dtest=LoginTest,CreateNoteTest,NegativeUITest,NegativeLoginTest -q'
            }
            post {
                always {
                    echo '===== UI Tests Completed ====='
                }
            }
        }

        stage('API Tests') {
            steps {
                echo '===== Running API Tests ====='
                bat 'mvn test -Dtest=GetNotesAPITest,DeleteNoteAPITest,ResponseTimeAPITest,NegativeAPITest -q'
            }
            post {
                always {
                    echo '===== API Tests Completed ====='
                }
            }
        }

        stage('E2E Tests') {
            steps {
                echo '===== Running E2E Tests ====='
                bat 'mvn test -Dtest=EndToEndTest -q'
            }
            post {
                always {
                    echo '===== E2E Tests Completed ====='
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo '===== Generating Allure Report ====='
                bat 'mvn allure:report -q'
            }
        }

        stage('Publish Reports') {
            steps {
                echo '===== Publishing Reports ====='

                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])

                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/extent-report',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Test Report'
                ])
            }
        }

    }

    post {

        always {
            echo '===== Archiving Artifacts ====='
            archiveArtifacts artifacts: 'target/screenshots/**/*.png', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/extent-report/**/*', allowEmptyArchive: true
        }

        success {
            echo '===== ALL TESTS PASSED ====='
        }

        failure {
            echo '===== SOME TESTS FAILED - CHECK REPORTS ====='
        }

    }
}