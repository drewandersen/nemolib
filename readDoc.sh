cd ./src/

if [ ! -d ./javadoc ] ; then mkdir ./javadoc; fi

javadoc -d ../javadoc nemolib

