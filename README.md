# Gmail Browser
 
The Gmail browser is a project to view locally archived Gmail messages using [gmvault](https://github.com/gaubert/gmvault).

## Environment setup
Before you can run the code, make sure you have the following things installed:
 - make to leverage the makefile.
 - virtualenv (tested with v15) - for installing the virtual python environment to run gmvault.  This is not required if you want to install and run gmvault yourself.

The preferred environment is Linux, and the packages can be easily installed with your distribution's package manager.  After installing the above programs, run the following setup steps.  All these steps should also work on Windows or a Mac but will have to adapted.
 1. Run `make gmvault email=youremail@gmail.com` to prepare the virtualenv for gmvault and sync emails for the account to the folder `appdata/emails/`
 2. Run 
	  - `mkdir appdata/emails/flat`
	  - `cd appdata/emails`
	  - `find ./ -type f -name "*.eml" -print0 | xargs -0I {} ln -s "../{}" flat/`
	  - `find ./ -type f -name "*.meta" -print0 | xargs -0I {} ln -s "../{}" flat/`
 3. `make build` to compile the code.
 4. `make start` to run the webserver.
