void call(String [] args){
    node {
        stage ('build') {
          withEnv(["MAVEN_HOME=/usr/share/maven"]) {
            buildInfo.env.capture = true

            try{
                shortEnv = args[1]
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
            }
            // }catch(Exception ex){
            //   currentBuild.result = 'FAILURE'
			//   notifyBuild()
   		    //   throw ex
            // }
            //     echo "Current build status: " + currentBuild.result
            //     if (currentBuild.result == 'UNSTABLE') {
            //       currentBuild.result = 'FAILURE'
            //         error("Tests failed")
                 }
            }
          }
        }

    }