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
                        
                        // Fetch the EC2 instance ID
                        def instanceId = sh(script: "aws cloudformation describe-stack-resources --stack-name ${params.STACK_NAME} --query 'StackResources[?LogicalResourceId==`WebAppInstance`].PhysicalResourceId' --output text --region ${params.AwsRegion}", returnStdout: true).trim()
                        echo "EC2 Instance ID: ${instanceId}"
                        
                        // Fetch the public IP of the EC2 instance
                        def publicIp = sh(script: "aws ec2 describe-instances --instance-ids ${instanceId} --query 'Reservations[0].Instances[0].PublicIpAddress' --output text --region ${params.AwsRegion}", returnStdout: true).trim()
                        echo "EC2 Public IP: ${publicIp}"
                        
                        // Store the public IP in a file
                        writeFile file: 'instance_details.txt', text: "EC2_IP=${publicIp}\nINSTANCE_ID=${instanceId}"
                        
                        // Archive the file for the second job to access
                        archiveArtifacts artifacts: 'instance_details.txt'
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
