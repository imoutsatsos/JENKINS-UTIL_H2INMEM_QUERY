/*** BEGIN META {
  "name" : "Delete_JobSessionWorkspace",
  "comment" : "Deletes folders and artifacts from an interactive job session",
  "parameters" : [ 'SESSIONS_WORKSPACE','JOB_SESSION_WORKSPACE'],
  "core": "2.100",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

/*
Clean up all files in the Job Sessions workspace recursively using the Antbuilder method

SESSIONS_WORKSPACE: is a global Jenkins variable and should be available to the scriptlet
Example
JOB_SESSIONS_WORKSPACE: TEST_4TSNE_node01a3plzmvsav3916lp9lmfybpmq330

*/

if (!binding.variables.containsKey("SESSIONS_WORKSPACE")||SESSIONS_WORKSPACE==''){
  jobSessionDir=new File("${JOB_SESSION_WORKSPACE}")
}else{
   jobSessionDir=new File("${SESSIONS_WORKSPACE}/${JOB_SESSION_WORKSPACE}")
}

if (jobSessionDir.deleteDir()){
  "Successfully Deleted Job Sessions Folder:   ${jobSessionDir.name}"
}else{
  "Failed to Clean UP:   ${jobSessionDir.name}"
}
