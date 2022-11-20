def call() {
    node {
        git branch: 'main', url: "https://github.com/aws-clouddevops/${COMPONENT}.git"
        env.APPTYPE='python'
        common.lintCheck()
        env.ARGS="-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null) {
            common.artifact()
        }
    }
}

// def call() {
//     pipeline {
//         agent any
//         stages {
//             stage('Lint Checks') {
//                 steps {
//                     script {
//                         lintCheck()
//                     }
//                 }
//             }
//             stage('Sonar Checks') {
//                 steps {
//                     script {
//                         env.ARGS=-Dsonar.sources=.
//                         common.sonarCheck()
//                     }
//                 }
//             }
//             stage('Test Cases') {
//             parallel{
//                 stage('Unit Tests') {
//                     steps{
//                         sh 'echo Unit Test Cases Completed'
//                    }
//                 }
//                 stage('Integration Tests') {
//                     steps{
//                         sh 'echo Integration Test Cases Completed'
//                    }
//                 }
//                 stage('Functional Tests') {
//                     steps{
//                         sh 'echo Functional Test Cases Completed'
//                    }
//                 }            
//             }
//         }
//         stage('Prepare Artifacts') {
//             when {
//                 expression { env.TAG_NAME != null} // Only runs whden you run against a tag
//             }
//             steps{
//                 echo 'echo'
//                  }
//             }
//         stage('Upload Artifacts') {
//             when {
//                 expression { env.TAG_NAME != null}
//             }
//             steps{
//                 echo 'echo'
//                  }
//             }  
//         }
//     }
// }