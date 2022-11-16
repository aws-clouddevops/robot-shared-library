
def call() {
    node {
        git branch: 'main', url: "https://github.com/aws-clouddevops/${COMPONENT}.git"
        env.APPTYPE='nodejs'
        common.lintCheck()
        env.ARGS="-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }
}

// def call() {
//     pipeline {
//         agent any
//     environment {
//         SONAR = credentials('SONAR')
//         NEXUS = credentials('NEXUS')
//     }
//         stages {
//             stage('Downloading the dependencies') {
//                 steps {
//                     sh "npm install"
//                 }
//             }

//               stage('Lint Checks') {
//                  steps {
//                      script {
//                          lintCheck()
//                      }
//                  }
//              }
//               stage('Sonar Checks') {
//                  steps {
//                      script {
//                         //  env.ARGS="-Dsonar.sources=."
//                         //  common.sonarCheck()
//                      }
//                  }
//              } 
//         //     stage('Test Cases') {
//         //     parallel{
//         //         stage('Unit Tests') {
//         //             steps{
//         //                 sh 'echo Unit Test Cases Completed'
//         //            }
//         //         }
//         //         stage('Integration Tests') {
//         //             steps{
//         //                 sh 'echo Integration Test Cases Completed'
//         //            }
//         //         }
//         //         stage('Functional Tests') {
//         //             steps{
//         //                 sh 'echo Functional Test Cases Completed'
//         //            }
//         //         }            
//         //     }
//         // }
//         stage('Check The release') {
//             when {
//                 expression { env.TAG_NAME != null } // Only runs whden you run against a tag
//             }
//             steps{
//                 script{
//                     env.UPLOAD_STATUS=sh(returnstdout: true, script: 'curl -L -s http://172.31.11.49:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true')
//                     print UPLOAD_STATUS
//                     }
//                 }
//             }
//         stage('Prepare Artifacts') {
//             when {
//                 expression { env.TAG_NAME != null } // Only runs whden you run against a tag
//                 expression { env.UPLOAD_STATUS == "" }
//             }
//             steps{
//                 sh '''
//                     npm install
//                     zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js

//                 '''
//                  }
//             }
//         stage('Upload Artifacts') {
//             when {
//                 expression { env.TAG_NAME != null } // Only runs when you run this against the tag
//                 expression { env.UP<OAD_STATUS == ""}
//             }
//             steps{
//                 sh '''
//                     curl -f -v -u  ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.11.49:8081/repository/${COMPONENT}/${TAG_NAME}.zip

//                 '''
//                  }
//             }  
//         }
//     }
// }

// Call is the default function which will be called when you call the filename