if [ ! -d ./out ] ; then mkdir ./out; fi
rm -f out/nemolib/*

javac ./src/nemolib/*.java -d out

cd ./out/

if [ ! -d ~/lib ] ; then mkdir ~/lib; fi
jar cf ~/lib/nemolib.jar ./nemolib/*.class
