import java.nio.file.Paths

// Map getProperties(Map config) {
//   final dir = sh returnStdout: true, script: '''#!/bin/sh -e
//       mvn help:evaluate -Dexpression=project.build.directory -q -DforceStdout
//   '''

// //   def version = config.APP_VERSION ?: '0.0.0'
//   final tagIdx = config.APP_VERSION.indexOf '-'
//   final tag = tagIdx == -1 ? '' : config.APP_VERSION.substring(tagIdx)

//   if (version.startsWith('0.0.0')) {
//     version = sh returnStdout: true, script: '''#!/bin/sh -e
//         mvn help:evaluate -Dexpression=project.version -q -DforceStdout
//     '''

//     if (version.endsWith('-SNAPSHOT')) {
//       version = version[0..-10]

//       if (!tag.endsWith('-SNAPSHOT')) {
//         final iteration = ".$env.BUILD_NUMBER"
//         if (tag.endsWith(iteration)) {
//           tag = tag[0..-iteration.length()-1]
//         }
//         tag += '-SNAPSHOT'
//       }
//     }

//     version = "$version$tag"
//   }

//   (_, version) = abbreviate(config.APP_NAME, version)

//   [
//       APP_IS_STABLE: config.APP_IS_STABLE && !version.contains('-'),
//       APP_VERSION: version,
//       BIN_DIR: Paths.get(env.WORKSPACE).relativize(Paths.get(dir)).toString(),
//   ]
// }

Build newBuild(Map config = [:], Map defaults = [:]) {

//   final String resolverRepo = "gradle-${config.APP_IS_STABLE ? 'release' : 'dev'}"
//   final String deployerRepo = "$resolverRepo-local"
  final maven

  // The Artifactory plugin reads env.JAVA_HOME and env.MAVEN_HOME, so
  // these must be explicitly set based on the values of the env
//   // variables in the build container.
//   [ 'JAVA_HOME', 'MAVEN_HOME' ].each {
//     env."$it" = sh(returnStdout: true, script: """#!/bin/sh -e
//         echo \$$it
//     """).trim()
//   }

  withArtifactoryEnv(config, cli: false) {
    maven = Artifactory.newMavenBuild()
    maven.resolver server: server,
        releaseRepo: resolverRepo,
        snapshotRepo: resolverRepo
    maven.deployer server: server,
        releaseRepo: deployerRepo,
        snapshotRepo: deployerRepo
  }

  new Build(maven, config, pom: 'pom.xml', goals: 'install')
}

class Build {
  private final Map args
  private final maven
  private final Map config

  private Build(Map args, maven, Map config) {
    this.args = args
    this.maven = maven
    this.config = config
  }

  Map run() {
    maven.run args

    [:]
  }
}