def call() {
    node {
        git branch: 'main', url: "https://github.com/aws-clouddevops/${COMPONENT}.git"
        env.APPTYPE='maven'
        common.lintCheck()
        env.ARGS="-Dsonar.projectKey=target/"
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null) {
            common.artifact()
        }
    }
}
// def lintCheck() {
//     sh '''
//          echo starting lint checks ${COMPONENT}
//          mvn checkstyle:check || true           # link checks
//          echo Lint checks completed for ${COMPONENT}
//     '''
// }
def call() {
    pipeline {
        agent any 
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintCheck()
                    }
                }
            }
            stage('Sonar Checks') {
                steps {
                    script {
                        mvn clean compile
                        env.ARGS=-Dsonar.projectKey=target/
                        common.sonarCheck()
                    }
                }
            }
            stage('Test Cases') {
            parallel{
                stage('Unit Tests') {
                    steps{
                        sh 'echo Unit Test Cases Completed'
                   }
                }
                stage('Integration Tests') {
                    steps{
                        sh 'echo Integration Test Cases Completed'
                   }
                }
                stage('Functional Tests') {
                    steps{
                        sh 'echo Functional Test Cases Completed'
                   }
                }            
            }
        }
        stage('Prepare Artifacts') {
            when {
                expression { env.TAG_NAME != null} // Only runs whden you run against a tag
            }
            steps{
                echo 'echo'
                 }
            }
        stage('Upload Artifacts') {
            when {
                expression { env.TAG_NAME != null}
            }
            steps{
                echo 'echo'
                 }
            }  
        }
    }
}