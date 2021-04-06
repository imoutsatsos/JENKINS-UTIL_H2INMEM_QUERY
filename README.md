# UTIL_H2INMEM_QUERY README #
A utility for reviewing Jenkins job in-memory H2 databases

### What is this repository for? ###

The repository provides an archive of the key artifacts required to setup (or update) the job on a Jenkins server. Artifacts include:

* Job configuration, and job-specific properties and scripts
* Shared Groovy Scriptlets
* Shared External scripts

### Job Dependencies ###

The [h2 Database](https://h2database.com) jar file should be placed in a Java external directory scanned by Jenkins. To identify a proper location run the following command in the Jenkins script console ``` println System.getProperty("java.ext.dirs")```. This will print all external locations scanned by Jenkins for java libraries.

Then place the h2 jar in one of the reported java external directories and restart your server for this to take effect.

### Deployment Instructions ###

* Clone the repository ```git clone https://github.com/imoutsatsos/JENKINS-UTIL_H2INMEM_QUERY.git```
* Deploy artifacts with [gradle](https://gradle.org/)
	* Open console in repository folder and execute command ```gradle deploy```
	* Deployment creates a **backup of all original files** (if they exist) in **qmic-UTIL_H2INMEM_QUERY/backup** folder
	* Project configuration, scripts and properties are deployed to **$JENKINS_HOME/jobs/UTIL_H2INMEM_QUERY** folder
	* Scriptlets are deployed to **$JENKINS_HOME/scriptlet/scripts** folder

* Review project plugins (shown below with latest version tested) and install as needed
 	* scriptler@2.9
  	* uno-choice@2.1
  	* ws-cleanup@0.34
 

### How do I build this job? ###

1. Select a Job with an H2 in Memory database
2. (Optional) Provide one or more URLs to CSV files. This creates additional DB tables for this session
3. Select a database table to display the available TABLE_COLUMNS
4. Create and edit SQL column expressions that you can use to build a full SQL query
5. Execute the custom query 
6. Manage the query results as needed
7. Build to archive desired query results
8. When the build is complete the temporary in-memory DB tables are closed 


### Who do I talk to? ###

* Please, file an issue if you have questions or problems using this job
