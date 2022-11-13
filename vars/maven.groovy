def lintCheck() {
    sh '''
         echo starting lint checks ${COMPONENT}
         mvn checkstyle:check || true
         echo Lint checks completed for ${COMPONENT}
    '''
}
def call() {
    pipeline {
        agent any
        stages {}
            stage('Lint Checks') {
                steps {
                    script {
                        lintCheck()
                    }
                }
            }
        }
    }
}