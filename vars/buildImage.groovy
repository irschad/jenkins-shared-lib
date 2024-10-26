#!/user/bin/env groovy

def call(String imageName) {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t irschad/java-app:2.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push irschad/java-app:2.0'
    }
}
