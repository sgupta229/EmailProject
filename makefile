build: 
	# ./gradlew clean build # I generally use this to clean and build in one line but you already have a clean rule in make, so just using build below.
	./gradlew build
start: FORCE
	java -cp java -cp "build/libs/*" Main appdata/emails/flat 

clean:
	./gradlew clean	
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

FORCE:
