Search1: compiled
	java -cp "bin" main.Search1

Search2: compiled
	java -cp "bin" main.Search2

Search3: compiled
	java -cp "bin" main.Search3

compiled: bin
	javac -d bin src/*/*.java src/*/*/*.java
	touch compiled

bin:
	mkdir bin

clean:
	rm -rf bin
	rm -rf log.txt
	rm -rf compiled
