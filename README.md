# Jenkins Shared Library

## Overview
This repository demonstrates the creation and use of a Jenkins Shared Library (JSL) to streamline and centralize build logic across Jenkins pipelines. The shared library is implemented using Groovy and stored in a Git repository for easy integration and reuse.

## Key Features
- Centralizes pipeline logic into reusable Groovy scripts.
- Supports common tasks such as building JAR files, creating and pushing Docker images.
- Enables seamless integration into Jenkins pipelines either globally or on a per-project basis.

---

## Technologies Used
- **Jenkins**
- **Groovy**
- **Docker**
- **Git**
- **Java**
- **Maven**

---

## Repository Structure
- **vars/**: Contains reusable shared library functions for use in Jenkins pipelines.
  - `buildImage.groovy`: Function to build Docker images.
  - `buildJar.groovy`: Function to build JAR files.
  - `dockerLogin.groovy`: Function to log in to Docker.
  - `dockerPush.groovy`: Function to push Docker images.

---

## Getting Started

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/irschad/jenkins-shared-lib.git
cd jenkins-shared-lib
```

---

### 2. Define Shared Library Functions

The `vars` folder contains the following reusable functions:

#### `buildImage.groovy`
```groovy
#!/user/bin/env groovy

import com.example.Docker
def call(String imageName) {
    return new Docker(this).buildDockerImage(imageName)
}
```

#### `buildJar.groovy`
```groovy
#!/user/bin/env groovy

def call() {
    echo "building the application for branch $BRANCH_NAME"
    sh 'mvn package'
}
```

#### `dockerLogin.groovy`
```groovy
#!/user/bin/env groovy

import com.example.Docker
def call() {
    return new Docker(this).dockerLogin()
}
```

#### `dockerPush.groovy`
```groovy
#!/user/bin/env groovy

import com.example.Docker
def call(String imageName) {
    return new Docker(this).dockerPush(imageName)
}
```

Commit and push the files to your repository if you make any modifications:

```bash
git add vars/*
git commit -m "Update shared library functions"
git push
```

---

### 3. Integrate the Shared Library into Jenkins

#### Step 1: Configure the Shared Library Globally
1. Go to **Manage Jenkins** > **Configure System** > **Global Pipeline Libraries**.
2. Add a new library configuration:
   - **Name:** `jenkins-shared-library`
   - **Default Version:** `main`
   - **Source Code Management:** Git.
   - **Repository URL:** `https://github.com/irschad/jenkins-shared-lib.git`
   - **Credentials:** Configure if required.

#### Step 2: Use the Shared Library in a Pipeline
In your project’s `Jenkinsfile`, include the shared library and call its functions:

```groovy
@Library('jenkins-shared-library')_

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    }
    stages {
        stage("Build Application JAR") {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("Build and Publish Docker Image") {
            steps {
                script {
                    buildImage('irschad/java-app:2.0')
                }
            }
        }
    }
}
```

---

### 4. Use the Shared Library for a Specific Project
If you prefer not to configure the shared library globally, reference it directly in your project’s `Jenkinsfile`:

```groovy
library identifier: 'jenkins-shared-library@main', retriever: modernSCM([
    $class: 'GitSCMSource',
    remote: 'https://github.com/irschad/jenkins-shared-lib.git',
    credentialsId: 'GitHub'
])

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    }
    stages {
        stage("Build Application JAR") {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("Build and Publish Docker Image") {
            steps {
                script {
                    buildImage('irschad/java-app:2.0')
                }
            }
        }
    }
}
```

---

## Example Reference
For further insights, refer to the repository: [Jenkins Shared Library](https://github.com/irschad/jenkins-shared-lib).

---

## License
This project is licensed under the [MIT License](LICENSE).

