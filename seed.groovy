#!groovy
import groovy.io.FileType
import hudson.FilePath
import hudson.*
import groovy.json.JsonSlurper

def again = new JsonSlurper().parseText(readFileFromWorkspace('verai.json'))
def folderName = again.Namefolder
def giturl = again.gurl
// def cred = again.gcred


folder("${folderName}") {
    displayName("${folderName}")
    description("Folder for  ${folderName}")
}


pipelineJob("${folderName}/${folderName}-pipeline") {
    definition {
        cps {
            script(readFileFromWorkspace('jenkinsfile'))
            sandbox()
        }
    }
}


freeStyleJob("${folderName}/test") {
    logRotator(-1, 10)
    scm {
            git {
                remote {
                    url("${giturl}")
                    // credentials("${cred}")
                }
            }
        }
    triggers {
        scm('H/2 * * * *')
    }
    steps{
        shell(readFileFromWorkspace('stages/test.groovy'))
        // shell("echo test")
    }
}

freeStyleJob("${folderName}/build") {
    logRotator(-1, 10)

    triggers {
        scm('H/2 * * * *')
    }

    steps{
        shell(readFileFromWorkspace('stages/build.groovy'))
        // shell("echo build")
    }
}

freeStyleJob("${folderName}/deploy") {
    logRotator(-1, 10)

    steps{
        shell(readFileFromWorkspace('stages/deploy.groovy'))
        // shell("echo deploy")
    }

    triggers {
        // githubPush()
        // scm('H/2 * * * *')
    }
}
