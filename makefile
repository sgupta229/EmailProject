emailbrowser: JSON-java src/Email.java src/Main.java
	javac -cp lib/:lib/mail-1.4.7.jar:src/ -d bin/ -Xlint:unchecked src/Main.java

clean:
	rm -r bin/*

lib:
	if [ ! -d "lib" ]; then\
		mkdir lib ;\
	fi;

JSON-java: lib
		mkdir -p lib/org || true; \
		if [ ! -d "lib/org/json" ]; then\
			git clone https://github.com/stleary/JSON-java lib/org/json ; \
		fi;
