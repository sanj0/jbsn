#!/bin/bash
$DIR=${pwd}
# check if default jbsn configs and notes
# dir is a git repo and if so, pull and then push
# to update local and remote before starting the client
function syncGit {
    if [[ -d ~/jbsn/.git/ ]]; then
        echo "detected git repo at default location, updating..."
        cd ~/jbsn/
        git add .
        git commit -m "automated commit at jbsn startup"
        git pull
        git push
    fi
}
syncGit
java -cp ~/jbsn/bin/jbsn.jar de.edgelord.jbsn.Main
syncGit
