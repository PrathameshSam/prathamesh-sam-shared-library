def call(Map config=[:], Closure body) {
    node{
        parameters {
            choice(name: 'crawler-enviornment', choices: ['rcp-crawler-dev', 'rcp-crawler-staging', 'rcp-crawler-uat', 'rcp-crawler-prod'], description:  'Select an Environment')
        }
    }
}