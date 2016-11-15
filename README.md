# FoBo-Demo

[![Stories in Ready](https://badge.waffle.io/karma4u101/FoBo-Demo.svg?label=ready&title=Ready)](http://waffle.io/karma4u101/FoBo-Demo) 

Sandbox projects for [FoBo](https://github.com/karma4u101/FoBo) demos/articles/tutorials.

- fobo-angular-lift-roundtrips -- A Lift 3 round trip tutorial with FoBo, AngularJS and Slick. 
- fobo-lift-template-demo -- The main introduction to FoBo pages.
- pimping-lift_advanced_bs3 -- A for demo perpose slightly pimped lift_advanced_bs3 
- pimping-lift_advanced_md -- A lift template app using Angular Material Design  
- fobo-angular-sandbox -- sandbox for FoBo angular stuff (status: just some randome unmaintaned stuff)
- fobo-bs3-sandbox -- A WIP FoBo boostrap 3 demo/tutorial (status: just some random unmaintaned stuff) 

Most of the demos above kan be seen running via links at [FoBo Template Demo](http://www.media4u101.se/fobo-lift-template-demo/) 

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
The following commands will update and fetch the projects dependencies then start the jetty server and load the lift application. 

	> update ~jetty:start

5) Launch Your Browser
Launch your favorite browser and type in the following address to bring up the application.
	
	http://localhost:8080/

6) Stop Jetty 
When you are done or wish to switch to another sub project (3 above) you can stop Jetty with

	> jetty:stop


Scala IDE Support 
-----------------

###Eclipse 

Sbteclipse provides SBT command to create Eclipse project files

1) Usage 

To create a eclipse project containing one of the sbt sub projects: 
 
	project$ ./sbt
        > project [sub project name as shown in list from the 'projects' command]
	> eclipse 

or if you wish to create a "one in all" eclispe project:

 	project$ ./sbt
	> eclipse 

2) In eclipse do: 

	File ==> Import...
	Select General ==> Existing Project into Workspace 
	Use "Browse" to look up the project root ....

### IDEA

sbt-idea provides a `gen-idea` command to SBT to generate IDEA project files

1) Usage

	project$ ./sbt
	> gen-idea no-classifiers

Or creating from a sub project
	project> ./sbt
        > project [sub project name as shown in list from the 'projects' command]
	> gen-idea no-classifiers

2) In Intellij / IDEA do:

	File ==> Open...
	Select project root directory

For further information, see both the plugin docs on github and stackoverflow responses:

	https://github.com/mpeltonen/sbt-idea
	http://stackoverflow.com/questions/4250318/how-to-create-sbt-project-with-intellij-idea

