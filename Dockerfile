FROM openjdk
CMD cd AppTest
CMD javac ./src/task02/*.java
ENTRYPOINT exec java -classpath ./src task02.ConvexController