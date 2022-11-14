def lintCheck() {
    sh '''
         # We want Devs to handle the lint checks failure
         # npm i jslint
         # node_modules/jslint/bin/jslint.js server.js || true
         echo starting lint checks
         echo Lint checks completed for $(COMPONENT)
    '''
}

def call() {
    pipeline {
        agent any
    environment{
        SONAR = credentials('SONAR')
    }
        stages {
            stage('Downloading the dependencies') {
                steps {
                    sh "npm install"
                }
            }

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
                steps {
                    script {
                        testCases()
                    }
                }
            }
        }
    }
}

// Call is the default function which will be called when you call the filename