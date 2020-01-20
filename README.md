# UTIL_H2INMEM_QUERY README #
UTIL_H2INMEM_QUERY:A utility for reviewing Jenkins job in-memory H2 databases
<div class="task">
  <a href="/job/DataSet_Review/build?delay=0sec">
  <img height="24" style="margin: 2px;" alt="" width="24" src="/static/41110db1/images/24x24/clock.png">
    <img height="50" style="margin: 2px;" id="nonclustered_index_scan_32x" alt="" src="/userContent/images/table_header.png" title="Review a CSV File" xmlns="">
  </a>&nbsp;
  <a href="/job/UTIL_H2INMEM_QUERY/build?delay=0sec">QUERY an H2 Jenkins instance Now</a>
</div>


### What is this repository for? ###

The repository provides an archive of the key artifacts required to setup (or update) the job on a Jenkins server. Artifacts include:

* Job configuration, and job-specific properties and scripts
* Shared Groovy Scriptlets
* Shared External scripts

### Job Dependencies ###

### Deployment Instructions ###

* Clone the repository ```git clone https://moutsio1@bitbucket.org/novartisnibr/qmic-UTIL_H2INMEM_QUERY.git```
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

1. 
2. 
3. 
4. 


### Who do I talk to? ###

* Ioannis K. Moutsatsos
