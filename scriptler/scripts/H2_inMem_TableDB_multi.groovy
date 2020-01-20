/*** BEGIN META {
  "name" : "H2_inMem_TableDB_multi",
  "comment" : "Creates in memory H2 database tables from one or more input files (csv or sql)",
  "parameters" : ['vDataSetPath','vTableNameAfter','dbNameHelper'],
  "core": "1.596",
  "authors" : [
    { name : "Ioannis K. Moutsatsos" }
  ]
} END META**/

import org.h2.Driver
import groovy.sql.Sql
import org.kohsuke.stapler.Stapler
import net.bull.javamelody.*;
import net.bull.javamelody.internal.model.*;

def sessionID= Stapler.getCurrentRequest().getSession().getId() 
def datasetPaths=vDatasetPath.replace('\n',',').tokenize(',').collect{it.trim()}
def splitAt=vTableNameAfter

def cs="caseSensitiveColumnNames=true"

if (binding.hasVariable('dbNameHelper') && binding.dbNameHelper!='' ){
  println 'dbNameHelper Provided:'+dbNameHelper
  populator=new DatabasePopulator(dbNameHelper)  
}else{
  println 'dbName FromProject:'+jenkinsProject.name
  populator=new DatabasePopulator("${jenkinsProject.name}_$sessionID")
}



datasetPaths.each{
    tableName= it.split(splitAt)[1].replace('/','_').replace('.','_').replace('-','_')
    if (it.endsWith('csv')){
    stmt= "CREATE TABLE IF NOT EXISTS $tableName as SELECT* FROM csvread('$it',null,'$cs')"
    println "<input name=\"value\" value=\"${tableName}\" class=\"setting-input\" type=\"text\">"
    }else{
        sqlUrl=it.toURL()
        stmt=  sqlUrl.text  
      println "<input name=\"value\" value=\"Executed ${it}\" class=\"setting-input\" type=\"text\">"
    }
    populator.addScript(stmt)
}


/*
..and now we execute them
 */
populator.populate()

return null

class DatabaseFactory{
    private type, name
    public DatabaseFactory(){

    }
    public build(name='builddb'){
        /* Create an h2 in-memory db, calling it what you like, here db1 */
        def db = Sql.newInstance("jdbc:h2:mem:$name", "org.h2.Driver")
        return db
    }
}
/**
 * Populates, initializes, or cleans up a database using SQL scripts defined in
 * string scripts.
 */
class DatabasePopulator {
    private List<String> scripts = new ArrayList<>();
    private separator=';', commentPrefix='--'
    private db

    public DatabasePopulator() {
        def dbFactory= new DatabaseFactory()
        this.db=dbFactory.build()
    }
  
      public DatabasePopulator(String dbName) {
        def dbFactory= new DatabaseFactory()
        this.db=dbFactory.build(dbName)
    }
    /**
     * Add default SQL scripts to execute to populate the database.
     * <p>The default scripts are {@code "schema.sql"} to create the database
     * schema and {@code "data.sql"} to populate the database with data.
     * @return {@code this}, to facilitate method chaining
     */
    public addDefaultScripts() {
        return addScripts("schema.sql", "data.sql");
    }

    /**
     * Add multiple SQL scripts to execute to initialize or populate the database.
     * @param scripts the scripts to execute
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public addScripts(String[] scripts) {
        scripts.each {
            addScript(it)
        }
        return this;
    }
    /**
     * Add a script to execute to initialize or clean up the database.
     * @param script the path to an SQL script (never {@code null})
     */
    public void addScript(String script) {
        assert script!=null
        this.scripts.add(script);
    }

    public void populate(){
        scripts.each{
            this.db.execute(it)
            println "Executed $it"
        }
    }

}
