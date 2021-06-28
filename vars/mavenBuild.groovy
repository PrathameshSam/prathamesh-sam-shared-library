def call(String [] config){
    node{
        sh '''
        #mvn -B -DskipTests clean package
        echo "Building the project for ${config[0]} ,${config[1]}"
        '''
    }
} 