set mypath=%cd%
cd %cd%
javac -d bin src/com/alpha/client/*.java
java -cp bin com.alpha.client.Client "192.168.1.86" 5000