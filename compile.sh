if [ ! -d ./out ] ; then mkdir ./out; fi
rm -f out/nemolib/*

javac ./src/nemolib/*.java -d out

cd ./out/

echo "Success! nemolib.jar has been created in ~/lib/"
if [ ! -d ~/lib ] ; then mkdir ~/lib; fi
jar cf ~/lib/nemolib.jar ./nemolib/*.class
