build: src/Email.java src/Main.java JSON-java spark
	javac -cp lib/:lib/jar/*:src/ -d bin/ -Xlint:unchecked -Xlint:deprecation src/Main.java
start: FORCE
	java -cp bin/:lib/jar/* --add-modules java.activation Main appdata/emails/flat 1606243922741019288

clean:
	rm -r bin/* || true
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
	gmvault/bin/gmvault sync --debug -d appdata/emails/ --no-compression $(email)

lib:
	if [ ! -d "lib" ]; then\
		mkdir lib ;\
	fi;
JSON-java: lib
		mkdir -p lib/org || true; \
		if [ ! -d "lib/org/json" ]; then\
			git clone https://github.com/stleary/JSON-java lib/org/json ; \
		fi;
spark: lib
	if [ ! -d "lib/spark.git" ]; then\
		git clone --branch 2.7.1 https://github.com/perwendel/spark lib/spark.git ;\
		ln -s spark.git/src/main/java/spark lib/spark; \
	fi;

FORCE:
