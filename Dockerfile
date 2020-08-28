FROM openjdk
VOLUME testv:/test
COPY . /test
CMD javac ./test/src/task02/*.java
ENTRYPOINT exec java -classpath ./test/src task02.ConvexController