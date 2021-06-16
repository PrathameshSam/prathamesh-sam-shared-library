Map getProperties(Map config) {
  [:]
}

Build newBuild(Map config = [:], Map defaults = [:]) {
  final Map defaultConfig = [ os: 'linux', arch: 'amd64' ]
  config = defaultConfig + defaults + config

  if (makeTargetExists('dep')) {
    withArtifactoryEnv(config) {
      sh 'make dep'
    }
  }

  config.target = config.target ?: 'build'
  config.cmd = config.cmd ?: config.APP_NAME ?: config.target

  if (makeTargetExists(config.cmd)) {
    config.target = config.cmd
  } else if (config.target == config.cmd || !makeTargetExists(config.target)) {
    throw new ManifestEntryNotFoundException('Could not find build target')
  }

  new Build(config, this)
}

class Build {
  private final Map config
  private final job

  private Build(Map config, job) {
    this.config = config
    this.job = job
  }

  Map run() {
    final String dir = job.getBinaryDirectory config
    job.sh """#!/bin/sh -e
        rm -rf '$dir'
    """

    if (!config.init && requiresWrapperScript()) {
      config.init = '/env/variables'
    }
    config.init = config.init ? "INIT_SCRIPT_FILE=$config.init" : ''

    job.withArtifactoryEnv(config) {
      job.sh "make $config.target CMD=$config.cmd \
          ARCH=$config.arch \
          BIN_DIR=$dir \
          BIN_NAME=$config.BIN_NAME \
          GOARCH=$config.arch \
          GOOS=$config.os \
          OS=$config.os \
          $config.init"
    }

    [:]
  }

  private boolean requiresWrapperScript() {
    job.sh(returnStatus: true, script: '''#!/bin/sh -e
        [ -z "$(find . -name Dockerfile* -exec grep "exec.sh" {} \\;)" ]
    ''') as boolean
  }
}