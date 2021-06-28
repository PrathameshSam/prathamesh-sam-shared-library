void call(String [] args){
    node {
        stage ('build') {
          withEnv(["MAVEN_HOME=/usr/share/maven"]) {
            buildInfo.env.capture = true

            try{

	            if (shortEnv.equals("rcp-crawler-prod")) {

	           		rtMaven.run pom: 'pom.xml', goals: 'clean install -U -P prod', buildInfo: buildInfo

	            }else if (shortEnv.equals("rcp-crawler-staging")) {

	           		rtMaven.run pom: 'pom.xml', goals: 'clean install -U -P stg', buildInfo: buildInfo

	            }else if(shortEnv.equals("rcp-crawler-dev")){

	                rtMaven.run pom: 'pom.xml', goals: 'clean install -U -P dev', buildInfo: buildInfo

	            }else if(shortEnv.equals("rcp-crawler-uat")) {

	                rtMaven.run pom: 'pom.xml', goals: 'clean install -U -P uat', buildInfo: buildInfo
	            }else{

	                rtMaven.run pom: 'pom.xml', goals: 'clean install -U', buildInfo: buildInfo
	            }

            }catch(Exception ex){
              currentBuild.result = 'FAILURE'
			  notifyBuild()
   		      throw ex
            }finally {
                try{
                    junit allowEmptyResults: true, testResults: 'crawler/target/surefire-reports/*.xml'

                    publishCoverage adapters: [jacocoAdapter('crawler/target/site/jacoco/jacoco.xml')], sourceFileResolver: sourceFiles('NEVER_STORE')
                }catch(Exception ex){
                    echo ex.toString()
                }
                echo "Current build status: " + currentBuild.result
                if (currentBuild.result == 'UNSTABLE') {
                  currentBuild.result = 'FAILURE'
                    error("Tests failed")
                 }
            }
          }
        }
        '''
    }
}