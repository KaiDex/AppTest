FROM openjdk
CMD javac *.java
ENTRYPOINT exec java -classpath . task02/ConvexController