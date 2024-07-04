pipeline {
    agent any

    environment {
        AWS_SESSION_TOKEN = credentials('session-token')
        SSH_KEY = credentials('ec2-ssh-key')
    }
    
    parameters {
        string(name: 'STACK_NAME', description: 'Enter your stack name')
        string(name: 'InstanceType', description: 'Enter your Instance type')
        string(name: 'ImageId', description: 'Enter ami image')
        string(name: 'VpcId', description: 'Enter your VPC id')
        string(name: 'SubnetId', description: 'Enter your subnet')
        string(name: 'AwsRegion', description: 'Enter your AwsRegion')
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
                        --region ${params.AwsRegion}
                        --template-file ./ec2.yml \
                        --parameter-overrides \
                        InstanceType=${params.InstanceType} \
                        ImageId=${params.ImageId} \
                        VpcId=${params.VpcId} \
                        SubnetId=${params.SubnetId}
                    """
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
