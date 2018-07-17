emailbrowser: src/Email.java src/Main.java JSON-java
	javac -cp lib/:lib/jars/*:src/ -d bin/ -Xlint:unchecked src/Main.java

clean:
	rm -r bin/*
gmvault: FORCE
	if [ ! -d "gmvault/" ]; then\
		virtualenv gmvault || (echo "Please install virtualenv" && exit 1);\
		mkdir gmvault/.config; \
	fi; \
	if [ ! -f "gmvault/bin/gmvault" ]; then\
		gmvault/bin/pip install gmvault; \
	fi;\
	if [ ! -d "appdata/emails" ]; then\
		mkdir -p appdata/emails ;\
	fi;\
	GMVAULT_DIR=gmvault/.config; \
	export GMVAULT_DIR; \
	gmvault/bin/gmvault sync --debug -d appdata/emails/ $(email)

lib:
	if [ ! -d "lib" ]; then\
		mkdir lib ;\
	fi;
JSON-java: lib
		mkdir -p lib/org || true; \
		if [ ! -d "lib/org/json" ]; then\
			git clone https://github.com/stleary/JSON-java lib/org/json ; \
		fi;

FORCE:
