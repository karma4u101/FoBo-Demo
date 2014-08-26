FoBo-Demo
=========

Sandbox projects for [FoBo](https://github.com/karma4u101/FoBo) demos/tutorials. 

Quick Start
-----------
The only prerequisites for running a FoBo demo example is that you have Git and Java installed and configured on the target computer.
You don't need to use it but the project also includes Eclipse and IDEA plug-in for browsing and following/working with the code, see the [Scala IDE Support] section bellow.   
 
1) Clone this project 

	git clone https://github.com/karma4u101/FoBo-Demo.git
	cd FoBo-Demo

2) Start the sbt console
There is also a sbt.bat for windows users so if you are on a windows machine just substitute the ./sbt command with sbt.bat.

        ./sbt 

3) View and switch to sub project
        
        > projects  
        > project [sub project name as shown in list from the 'projects' command]

4) Update & Start Jetty  
The following commands will update and fetch the projects dependancys then start the jetty server and load the lift application. 

	> update ~container:start

5) Launch Your Browser
Launch your favorite browser and type in the following address to bring up the application.
	
	http://localhost:8080/

6) Stop Jetty 
When you are done or wish to switch to another sub project (3 above) you can stop Jetty with

	> container:stop


Scala IDE Support 
-----------------

###Eclipse 

Sbteclipse provides SBT command to create Eclipse project files

1) Usage

	project$ ./sbt
	> eclipse 

2) In eclipse do: 

	File ==> Import...
	Select General ==> Existing Project into Workspace 
	Use "Brows" to look up the project root ....

### IDEA

sbt-idea provides a `gen-idea` command to SBT to generate IDEA project files

1) Usage

	project$ ./sbt
	> gen-idea no-classifiers

2) In Intellij / IDEA do:

	File ==> Open...
	Select project root directory

For further information, see both the plugin docs on github and stackoverflow responses:

	https://github.com/mpeltonen/sbt-idea
	http://stackoverflow.com/questions/4250318/how-to-create-sbt-project-with-intellij-idea

