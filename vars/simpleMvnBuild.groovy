def call(Map config){
    node {
        sh '''
                // sh "mvn clean install -P${config.ENV} -DskipTest"
                git version
                docker version
                echo "${config.crawler_env}"
                echo "${config.gcp_project}"
        '''
        }
    }
}