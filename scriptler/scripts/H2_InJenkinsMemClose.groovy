/*** BEGIN META {
  "name" : "H2_InJenkinsMemClose",
  "comment" : "Closes an in memory database instance",
  "parameters" : [ 'vDbInstance', 'shutdown'],
  "core": "2.100",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

/*
   Lists all in memory tables for a particular Jenkins Job
    Each Job creates a synonymous in memory H2 db instance that can include multiple database objects (tables, views etc)
    DB Objects can be cleaned by setting the flag 'cleanDB=true'
*/
import org.h2.Driver
import groovy.sql.Sql
import org.kohsuke.stapler.Stapler

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

