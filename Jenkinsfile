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
        string(name: 'KeyName', description: 'Enter your .pem key Name')
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
                    script {
                        sh """
                            aws cloudformation deploy \
                            --stack-name ${params.STACK_NAME} \
                            --template-file ./ec2.yml \
                            --parameter-overrides \
                            InstanceType=${params.InstanceType} \
                            ImageId=${params.ImageId} \
                            VpcId=${params.VpcId} \
                            SubnetId=${params.SubnetId} \
                            KeyName=${params.KeyName} \
                            --region=${params.AwsRegion}
                        """

                        // Get the EC2 instance ID from CloudFormation stack outputs
                        def describeStack = sh(script: "aws cloudformation describe-stacks --stack-name ${params.STACK_NAME} --region ${params.AwsRegion}", returnStdout: true).trim()
                        def stackOutput = readJSON(text: describeStack)
                        def instanceId = stackOutput.Stacks[0].Outputs.find { it.OutputKey == 'InstanceId' }.OutputValue
                        
                        // Get the EC2 instance public IP address
                        def describeInstance = sh(script: "aws ec2 describe-instances --instance-ids ${instanceId} --region ${params.AwsRegion}", returnStdout: true).trim()
                        def instanceDetails = readJSON(text: describeInstance)
                        def instanceIP = instanceDetails.Reservations[0].Instances[0].PublicIpAddress
                        
                       // Trigger the second job with the instance IP as a parameter
                        build job: 'sshEc2', parameters: [
                            string(name: 'INSTANCE_ID', value: instanceId),
                            string(name: 'EC2_IP', value: instanceIP)
                        ]
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
