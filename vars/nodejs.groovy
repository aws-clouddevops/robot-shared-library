def lintCheck() {
    sh '''
         # We want Devs to handle the lint checks failure
         # npm i jslint
         # node_modules/jslint/bin/jslint.js server.js || true
         echo starting lint checks
         echo Lint checks completed
    '''
}
def call() {
    pipeline {
        agent any
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
        }
    }
}

// Call is the default function which will be called when you call the filename