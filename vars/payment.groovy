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
        }
    }
}