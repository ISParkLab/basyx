pipeline {
  agent {
    kubernetes {
      label 'basyx-' + env.BRANCH_NAME + '-' + env.BUILD_NUMBER
      yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: postgresql
    image: postgres:latest
    command: 
    - cat
    tty: true
    env:
    - name: POSTGRES_PASSWORD
      value: admin
    - name: PGDATA
      value: /run/postgresql/data
  - name: cpp
    image: gcc:latest
    command:
    - cat
    tty: true
  - name: maven
    image: maven:latest
    resources:
      requests:
        memory: "2Gi"
        cpu: "1"
      limits:
        memory: "2Gi"
        cpu: "1"
    command:
    - cat
    tty: true
    env:
    - name: MAVEN_CONFIG
      value: /home/jenkins/agent/.m2
"""
    }
  }
  stages {
      stage('Setup PostgreSQL') {
          steps {
              container('postgresql') {
                  sh '''
                      chmod +x ./ci/init_postgres.sh
                      ./ci/init_postgres.sh postgres
                      pg_ctl start
                      '''
              }
          }
      }
      stage('Java SDK Tests') {
          steps {
              container('maven') {
                  sh '''
                     mkdir /home/jenkins/agent/.m2
                     chmod +x ./ci/build_java.sh
                     ./ci/build_java.sh
                     '''
              }
          }
      }
      stage('C++ SDK Tests') {
          steps {
              container('cpp') {
                  sh '''
                     apt-get update && apt-get install cmake
                     chmod +x ./ci/build_cpp.sh
                     ./ci/build_cpp.sh
                     '''
              }
          }
      }
  }
}
