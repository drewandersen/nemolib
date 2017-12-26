nemolib
=======

Description
-----------
The nemolib library includes tools specifically designed for executing the NemoProfile algorithm. Classes have also been designed with parallelization in mind.

Prerequisites
-------------
* A Linux operating environment.
* JDK version 8 or higher
* [Maven](https://maven.apache.org/) version 3.0 or higher

Installation and Use
--------------------
Use the `mvn package` command from the root directory to generate a jar file
in the `target` subdirectory containing the nemolib library. Any classes 
which implement nemolib should use the statement `import edu.uwb.nemolib;`.

Example Program
---------------
An example program can be found at github.com/drewandersen/nemoprofile.

Documentation
-------------
JavaDocs are auto-generated when Maven creates the project.

Future Project Ideas
--------------------
* Parallelize random graph analysis (embarassingly parallel).
* Complete parallelization using MPI.
* Implement SubgraphCollect by adding subgraph induction functionality.
* Implement the nauty algorithm in Java and include it as a dependency in nemolib. 
* Research other graph/network parallelziation libraries and implement NemoProfile to compare.
* Write unit tests.
