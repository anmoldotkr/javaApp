pipeline {
    agent any

    environment {
        AWS_SESSION_TOKEN = credentials('aws-session-token')
    }
    
    parameters {
        string(name: 'STACK_NAME', description: 'Enter your stack name')

    }
    stages {
        stage('creating EC2') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
                   ]])
                    {   
                    sh """
                        aws cloudformation deploy \
                        --stack-name ${params.STACK_NAME} \
                        --template-file ./ec2.yml
                    """
                    }
            }  
        }
    }
    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        failure {
            echo 'Build failed. Please check the logs for details.'
        }
        success {
            echo 'Build succeeded!'
        }
    }
}

