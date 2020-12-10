#!/bin/bash
DIR=$(pwd)
# check if default jbsn configs and notes
# dir is a git repo and if so, pull and then push
# to update local and remote before starting the client
function syncGit {
    if [[ -d ~/jbsn/.git/ ]]; then
        echo "detected git repo at default location, updating..."
        cd ~/jbsn/ || exit
        git add .
        git commit -m "$1"
        git pull
        git push
    fi
}
syncGit "automated commit at jbsn startup"
java -cp ~/jbsn/bin/jbsn.jar de.sanj0.jbsn.Main
syncGit "automated commit at jbsn exit"
cd "${DIR}" || exit
