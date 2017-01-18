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
* JDK version 8 or higher
* [Maven](https://maven.apache.org/) version 3.0 or higher
* A copy of the Nauty Traces `labelg` binary for your execution environment.
You can download labelg from http://pallini.di.uniroma1.it/. 

Installation and Use
--------------------
Use the `mvn -package` command from the root directory to generate a jar file
in the `target` subdirectory containing the nemolib library. Any classes 
which implement nemolib should use the statement `import edu.uwb.nemolib;`.

Example Programs
----------------
Example programs can be found at https://github.com/drewandersen/nemoprofile/

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
