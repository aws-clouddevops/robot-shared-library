def lintCheck() {
    sh '''
         echo starting lint checks ${COMPONENT}
         mvn checkstyle:check || true           # link checks
         echo Lint checks completed for ${COMPONENT}
    '''
}
def sonarCheck() {
    sh '''
        sonar-scanner -Dsonar.host.url=http://172.31.7.40:9000 -Dsonar.projectKey=cart -Dsonar.login=admin -Dsonar.password=password -Dsonar.java.binaries=target/
    '''
}
def call() {
    pipeline {
        agent any
    environment{
        SONAR = credentials('SONAR')
    }   
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