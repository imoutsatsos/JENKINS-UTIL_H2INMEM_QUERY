/*** BEGIN META {
  "name" : "JSON_CopyFolder_DBObject",
  "comment" : "Uses JSON in-MEM DB records to copy required artifact folders",
  "parameters" : ['vSESSION_PARAMDB', 'vOUTPUT_FOLDER','vDBNAME_HELPER','vJSON_PARAM_KEY'],
  "core": "1.596",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

import java.net.URLEncoder;
import org.h2.Driver
import groovy.sql.Sql
import java.util.logging.Level
import java.util.logging.Logger

/*Comment for testing as script*/
def LOGGER= Logger.getLogger('org.biouno.unochoice')
def thr = Thread.currentThread()
def build = thr?.executable
def envVarsMap = build.parent.builds[0].properties.get("envVars")

JOB_NAME=envVarsMap.JOB_NAME


/*For testing Scriptlet
JOB_NAME="not needed if using vDBNAME_HELPER"
vSESSION_PARAMDB="PARAMS_node01g0d0eh9pje111x7eckw26s1ei23960"
vOUTPUT_FOLDER="D:/TEMP/JSONCOPY"
vDBNAME_HELPER="UTIL_H2INMEM_QUERY_node01g0d0eh9pje111x7eckw26s1ei23960"
vJSON_PARAM_KEY='JSON_RESULTSET_PARAM'
*/

def jsonObjects=[:]
def slurper = new groovy.json.JsonSlurper()
def sql=null
if (vDBNAME_HELPER!=null || vDBNAME_HELPER!=''){
sql = Sql.newInstance("jdbc:h2:mem:${vDBNAME_HELPER}", "org.h2.Driver")
}else{
sql = Sql.newInstance("jdbc:h2:mem:${JOB_NAME}", "org.h2.Driver")
}

stm="""SELECT * FROM $vSESSION_PARAMDB WHERE PARAM LIKE '${vJSON_PARAM_KEY}%\' AND NOT PARAMVALUE= ''""" as String

sql.eachRow(stm) { row ->
    jsonObjects.put(row.PARAM.toString(),"${row.PARAMVALUE.toString()}")
}
artifactPaths2Copy=[:]

jsonObjects.each{k,v->
    JSON_MULTIPARAM=v
    formParams=slurper.parseText(JSON_MULTIPARAM)
    if (formParams.artifactPath!=null || formParams.artifactPath!='' ){
    artifactPaths2Copy.put("${formParams.artifactPath}","${formParams.artifactLabel}_${formParams.TSTAMP}")
    }    

} //end check for JSON_MULTIPARAM existence

copyFolders(vOUTPUT_FOLDER,artifactPaths2Copy)


/* 
 a method to copy specified folders 
 @vFolders2Copy: a map of folders to copy to folder human readable labels

*/
def copyFolders(vDestination, vFolders2Copy){

destination=vDestination
toCopy=vFolders2Copy

def ant= new AntBuilder()

toCopy.each{k,v->
	dirName="${k.trim()}"
	folder= new File(dirName)
	if (folder.exists()){
	ant.copy(todir: "$destination/$v"){
	fileset(dir: dirName )
	}
	println "Copied $dirName"
	}else{
	println "Skipping missing folder: $dirName"
	}

}//end each
}