/*** BEGIN META {
  "name" : "copyServerStaticArtifact",
  "comment" : "Copies an artifact from a folder (typically in JENKINS_HOME) to a folder in the executing job's WORKSPACE",
  "parameters" : [ 'artifactFilter','contentFolder','targetFolder'],
  "core": "2.100",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

/* Required parameters: 
artifactFilter, contentFolder
Copies static artifact(s) ( as defined by a filter) 
to target folder in the executing job's Workspace
*/
import hudson.model.*

// get current thread / Executor
def thr = Thread.currentThread()
// get current build
def build = thr?.executable
def options =new HashMap()
  
// store build/environmental variables into options
def envVarsMap = build.parent.builds[0].properties.get("envVars")
options.putAll(envVarsMap)
println "ArtifactFilter: $artifactFilter"  
println "JENKINS_HOME: ${options.JENKINS_HOME}"
  def jHome=options.JENKINS_HOME
  contentFolder=contentFolder.replace('\\','/')  

//check 2 alternative paths for contentFolder existence
testPath=new File("$jHome/$contentFolder/")

if (testPath.exists()){
  jPath="$jHome/$contentFolder/"
  println "Source Path: $jPath"
}else{
  testPath= new File ("$contentFolder")
  if(testPath.exists()){
	  jPath=testPath.canonicalPath
  println "Source Path: $jPath"
  }
}

//selective copy using ant
          def ant=new AntBuilder()
          fileFilter=artifactFilter.split(',')
          fileFilter.each{it->
            filter=it.trim()
          ant.copy(todir:"${options.WORKSPACE}/$targetFolder", overwrite:true){
             fileset(dir:jPath){
             include(name:"*$filter*") 
               }
            }
            
          }//end each