def call(Map config=[:], Closure body) {
    node{
        parameters {
            choice(name: 'mpi-enviornment', choices: ['rcp-mpi-dev', 'rcp-mpi-qa', 'rcp-mpi-prod'], description:  'Select an Environment')
        }
    }
}