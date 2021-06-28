def call(String env, String gcp){
    node{
        sh '''
        #mvn -B -DskipTests clean package
        echo "Building the project for ${env} ,${gcp}"
        '''
    }
} 