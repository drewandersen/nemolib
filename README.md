nemolib
=======

Description
-----------
The nemolib library includes tools specifically designed for executing the 
NemoProfile algorithm. Classes have also been designed with parallelization in 
mind.

Prerequisites
-------------
* A Linux operating environment.
* Java Developers Kit (jdk) version 8 or higher
* A copy of the Nauty Traces `labelg` binary for your execution environment. 
You can download labelg from http://pallini.di.uniroma1.it/. 

Installation
------------
In most Linux environments, you can simply compile nemolib using the 
included bash script `compile.sh`. Doing so will create a jar file called 
`nemolib.jar` and place it in the users' `~/lib/` directory.

Example Programs
----------------
Example programs can be found at https://bitbucket.org/drewda81/nemoprofile/

Documentation
-------------
You can view the documentation by running the `readDoc.sh` script.

Future Project Ideas
--------------------
* Complete parallelization using MPI.
* Implement SubgraphCollect by adding subgraph induction functionality.
* Implement the nauty algorithm in Java and include it as a dependency in 
nemolib. 
* Research other graph/network parallelziation libraries and implement 
NemoProfile to compare.
* Create thorough unit tests
