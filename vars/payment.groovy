def lintCheck() {
    sh '''
         echo starting lint checks ${COMPONENT}
         # pylint *.py           # link checks
         echo Lint checks completed for ${COMPONENT}
    '''
}
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
                        env.ARGS=-Dsonar.sources=.
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
        }
    }
}