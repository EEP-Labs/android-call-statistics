#!/bin/sh
#
# Depends on XMLStarlet, install it in a Debian like systems
# with
#
#   # apt-get install xmlstarlet

COMMIT_MESSAGE=""
REVISION="vX.X.X"

update_manifest_version_name() {
    xmlstarlet ed -L \
        --update /manifest/@android:versionName \
        --value $1 \
        AndroidManifest.xml

}
update_manifest_version_code() {
    CODE=$(get_version_code)
    xmlstarlet ed -L \
        --update /manifest/@android:versionCode \
        --value $(($CODE + 1)) \
        AndroidManifest.xml
}

get_version_code() {
    xmlstarlet sel \
        --template --value-of /manifest/@android:versionCode \
        AndroidManifest.xml
}

create_tag() {
    git tag $1
}

usage() {
    cat <<EOF
usage: $0 <cmd> [options]
usage: $0 tag <revision> [<commit message>]
usage: $0 release <revision> [<commit message>]

where <cmd> is one of

    tag:     update the version inside the AndroidManifest and create a commit
    release: as tag but update also the versionCode

update the manifest file, save the changes and create a new tag with git.
EOF
    exit
}

die() {
    echo "fatal: $1"
    usage
}

test -z "$1" && usage

case $1 in
    release)
        update_manifest_version_code
        ;&
    tag)
        shift
        update_manifest_version_name "$1"
        git commit --all \
            --message="${2:-Bump to revision ${1}}"
        create_tag "$1"
        break;;
    get_version_code)
        get_version_code
        exit
        break;;
    *)
        die "command not found"
        break;;
esac
