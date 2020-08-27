FROM openjdk
CMD javac *.java
ENTRYPOINT exec java task02/ConvexController