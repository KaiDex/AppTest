FROM openjdk
CMD javac ./AppTest/src/task02/*.java
ENTRYPOINT exec java -classpath ./AppTest/src task02.ConvexController