pipeline {
    agent any

    environment {
        AWS_SESSION_TOKEN = credentials('session-token')
    }
    
    parameters {
        string(name: 'AwsRegion', description: 'Enter your stack name')
        string(name: 'ENVIRONMENT', description: 'Enter the environment (dev, stage, testing)')
        string(name: 'PRODUCT', description: 'Enter the product name (default: Training)')
        string(name: 'SERVICE', description: 'Enter the service name (default: s3)')
    }
    
    stages {
        stage('Creating VPC') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]) {
                    script {
                        sh """
                            aws cloudformation deploy \
                            --stack-name ${params.ENVIRONMENT}-${params.PRODUCT}-${params.SERVICE} \
                            --region ${params.AwsRegion} \
                            --template-file ./Training/Anmol/aws/service/vpc.yml
                        """
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed. Please check the logs for details.'
        }
        always {
            cleanWs()
        }
    }
}
