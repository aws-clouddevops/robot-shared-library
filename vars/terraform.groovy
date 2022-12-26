def call() {
    properties([
        parameters([
            choice(name: "ENV" ,choices: 'dev\nprod' , description: "Choose Environment to build"),
            choice(name: "ACTION" ,choices: 'apply\ndestroy' , description: "Choose apply to destroy"),
    ]) ,
])

    node {
        sh "rm -rf *"
        git branch: 'main', url: "https://github.com/aws-clouddevops/${REPONAME}.git"

        stage('Terraform Init'){
            sh '''
                terrafile -f env-${ENV}/Terrafile
                terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
            '''
        }

        stage('Terraform Plan'){
            sh '''
                terraform plan -var-file=env-${ENV}/${ENV}.tfvars
            '''
        }
        stage('Terraform Plan'){
            sh '''
                terraform ${ACTION} -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
            '''
        }
    }
}