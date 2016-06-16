all:
	javac ServerTest.java ServerAverage.java
	java ServerTest

clean:
	rm -rf *.class

cleanall:
	rm -rf *.class
	rm -rf data
