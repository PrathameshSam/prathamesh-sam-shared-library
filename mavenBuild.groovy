def call(String [] args){
    stages{
        stage('Build'){
            sh '''
            mvn -B -DskipTests clean package
            echo "Building the project for ${agrs[0]} ,${args[1]}"
            '''
        }
    }
}