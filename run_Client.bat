set mypath=%cd%
cd %cd%
javac -d bin src/client_package/*.java
java -cp bin client_package.Client "192.168.1.86" 5000