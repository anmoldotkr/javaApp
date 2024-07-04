pipeline {
    agent any

    environment {
        AWS_SESSION_TOKEN = credentials('aws-session-token')
        SSH_KEY = credentials('ec2-ssh-key')
    }
    
    parameters {
        string(name: 'STACK_NAME', description: 'Enter your stack name')
        string(name: 'InstanceType', description: 'Enter your Instance type')
        string(name: 'ImageId', description: 'Enter ami image')
        string(name: 'VpcId', description: 'Enter your VPC id')
    }
    
    stages {
        stage('creating EC2') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]) {
                    sh """
                        aws cloudformation deploy \
                        --stack-name ${params.STACK_NAME} \
                        --template-file ./ec2.yml \
                        --parameter-overrides \
                        InstanceType=${params.InstanceType} \
                        ImageId=${params.ImageId} \
                        VpcId=${params.VpcId}
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
