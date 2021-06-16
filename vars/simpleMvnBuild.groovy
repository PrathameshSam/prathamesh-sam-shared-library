def call(Map config){
    node {
                sh "mvn clean install -P${config.ENV} -DskipTest"
        }
    }
}