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


multibranchWorkflowJob("${folderName}/${folderName}-pipeline") {
    definition {
        cps {
            script(readFileFromWorkspace('jenkinsfile'))
            sandbox()
        }
    }
}


job("${folderName}/test") {
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
        shell(readFileFromWorkspace('test.groovy'))
        //shell("echo ${test}")
    }
}

job("${folderName}/build") {
    logRotator(-1, 10)

    triggers {
        scm('H/2 * * * *')
    }

    steps{
        shell(readFileFromWorkspace('build.groovy'))
    }
}

job("${folderName}/deploy") {
    logRotator(-1, 10)

    steps{
        shell(readFileFromWorkspace('deploy.groovy'))
    }

    triggers {
        // githubPush()
        scm('H/2 * * * *')
    }
}
