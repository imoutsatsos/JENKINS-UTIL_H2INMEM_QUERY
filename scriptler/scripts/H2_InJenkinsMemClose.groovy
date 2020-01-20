/*** BEGIN META {
  "name" : "H2_InJenkinsMemClose",
  "comment" : "Closes an in memory database instance",
  "parameters" : [ 'vDbInstance'],
  "core": "2.100",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

/*
	Lists all in memory tables for a particular Jenkins Job
	Each Job creates a synonymous in memory db that can include multiple database objects (tables, views etc)
    DB Objects can be cleaned by setting the flag 'cleanDB=true'
	
    For creating a table by direct loading of a data set use this:
    http://nrusca-sd189.nibr.novartis.net:8080/scriptler/runScript?id=H2_inMem_TableDB.groovy
    
    For executing any series of SQL commands in script files use this:
    http://nrusca-sd189.nibr.novartis.net:8080/scriptler/runScript?id=CreateSchema_inMem_TableDB.groovy
*/
import org.h2.Driver
import groovy.sql.Sql
import org.kohsuke.stapler.Stapler
/*
def sessionID= Stapler.getCurrentRequest().getSession().getId() 
println "Session ID: $sessionID"
*/

dbs=vDbInstance.tokenize(',').collect{it.trim()}
dbs.each{dbInstance->
println "DB Instance: $dbInstance"
def sql = Sql.newInstance("jdbc:h2:mem:$dbInstance", "org.h2.Driver")

stm0='SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=\'PUBLIC\' ORDER BY TABLE_NAME ASC'
println "In-Memory Tables:$dbInstance"

sql.eachRow(stm0) { row ->
  println row
} 

if (shutdown=='true'){
   stmx='SHUTDOWN IMMEDIATELY'
   sql.execute(stmx)
   println "DbInstance Shutdown:$dbInstance"
}
} //end each dbs

