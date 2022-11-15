def sonarCheck() {
    sh '''
        sonar-scanner -Dsonar.host.url=http://172.31.7.40:9000 -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} ${ARGS}
        sonar-quality-gate ${SONAR_USR} ${SONAR_PSW} 172.31.7.40 ${COMPONENT} // this gives the resukt of the scan based on that either it will abort the pipeline or will proceed further.
        echo sonarchecks for ${COMPONENT}
       '''
}

// For Non Java, Code source parameter is -Dsonar.sources=.
// For Java, Code source parameter is -Dsonar.projectKey=target/