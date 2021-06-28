def call(Map [] config){
    node{
        sh '''
        #mvn -B -DskipTests clean package
        echo "Building the project for ${config.crawler_env} ,${config.gcp_project}"
        '''
    }
} 