#!/bin/bash
#builds and installs jbsn on the system

# build the maven project
mvn clean install

# copy jar to jbsn/bin
mkdir ~/jbsn
mkdir ~/jbsn/bin
cp target/*.jar ~/jbsn/bin/jbsn.jar

# copy launch script to /usr/local/bin and make executable
cp jbsn.sh /usr/local/bin/jbsn
chmod +x /usr/local/bin/jbsn

# run it to install the files if ~/.jbsnInstalled doesnt exist
if [[ -f ~/.jbsnInstalled ]]; then
    echo "local files and folders seem to be installed"
else 
    jbsn
fi

echo "jbsn is now installed on your system"
echo "run it using the command 'jbsn'"
