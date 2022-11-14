def lintCheck() {
    sh '''
         echo starting lint checks ${COMPONENT}
         mvn checkstyle:check || true           # link checks
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
                        env.ARGS=-Dsonar.projectKey=target/
                        common.sonarCheck()
                    }
                }
            }
        }
    }
}