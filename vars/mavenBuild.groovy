def call(String [] args){
    node{
        sh '''
        mvn -B -DskipTests clean package
        echo "Building the project for ${agrs[0]} ,${args[1]}"
        '''
    }
} 