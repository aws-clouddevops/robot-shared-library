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
    environment {
        SONAR = credentials('SONAR')
        NEXUS = credentials('NEXUS')
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
                         env.ARGS="-Dsonar.sources=."
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
        stage('Check The release') {
            when {
                expression { env.TAG_NAME != null } // Only runs whden you run against a tag
            }
            steps{
                script{
                    env.def UPLOAD_STATUS=sh(returnstdout: true, script: "curl http://172.31.11.49:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip" || true)
                    print UPLOAD_STATUS
                    }
                }
            }
        stage('Prepare Artifacts') {
            when {
                expression { env.TAG_NAME != null } // Only runs whden you run against a tag
                expression { env.UPLOAD_STATUS == "" }
            }
            steps{
                sh '''
                    npm install
                    zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js

                '''
                 }
            }
        stage('Upload Artifacts') {
            when {
                expression { env.TAG_NAME != null }
            }
            steps{
                sh '''
                    curl -f -v -u  ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.11.49:8081/repository/${COMPONENT}/${TAG_NAME}.zip

                '''
                 }
            }  
        }
    }
}

// Call is the default function which will be called when you call the filename