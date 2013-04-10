#!/bin/sh
#
# Depends on XMLStarlet, install it in a Debian like systems
# with
#
#   # apt-get install xmlstarlet

COMMIT_MESSAGE=""
REVISION="vX.X.X"

update_manifest() {
    xmlstarlet ed -L \
        --update /manifest/@android:versionName \
        --value $1 \
        AndroidManifest.xml
}

create_tag() {
    git tag $1
}

test -z "$1" && {
    cat <<EOF
usage: $0 [--message <commit message>] <revision>

update the manifest file, save the changes and create a new tag with git.
EOF
    exit
}

while [[ $1 ]]
do
    case $1 in
        --message)
            COMMIT_MESSAGE="$2"
            shift 2
            break
            ;;
        *)
            REVISION="$1"
            shift
            break
            ;;
    esac
done

update_manifest "$REVISION"
git commit --all --message="${COMMIT_MESSAGE:-Bump to revision ${REVISION}}"
git tag $REVISION
