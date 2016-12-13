if [ ! -d ./out ] ; then mkdir ./out; fi
rm -f out/nemolib/*

if [ ! -d ./javadoc ] ; then mkdir ./javadoc; fi

javac ./src/nemolib/*.java -d out

cd ./out/

if [ ! -d ~/lib ] ; then mkdir ~/lib; fi
jar cf ~/lib/nemolib.jar ./nemolib/*.class

cd ../src/

javadoc -d ../javadoc nemolib
