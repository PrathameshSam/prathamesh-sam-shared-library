def call(String [] args){
    stages{
        stage('Build'){
            sh 'mvn -B -DskipTests clean package'
        }
    }
}