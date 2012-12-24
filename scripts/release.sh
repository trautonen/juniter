#!/bin/bash

if ! [ -f $PWD/pom.xml ]
then
    cd ..
fi

if ! [ -f $PWD/pom.xml ]
then
    echo "pom.xml not found, probably in wrong directory"
    exit 1
fi

branch=$(git rev-parse --abbrev-ref HEAD)
echo $branch
type=$(echo $branch | awk 'match($0, "/") { print substr($0, 0, RSTART) }')
echo $type
version=$(echo $branch | awk 'match($0, "/") { print substr($0, RSTART + 1) }')
echo $version

if [ $type != "release" ] && [ $type != "hotfix" ]
then
    echo "Not in release or hotfix branch"
    exit 1
fi

echo -n "Enter GPG passphrase: "
stty_orig=$(stty -g)
stty -echo 
read passphrase
stty $stty_orig

echo "$passphrase" | gpg --passphrase-fd 0 --armor --output pom.xml.asc --detach-sig pom.xml > /dev/null
gpg --verify pom.xml.asc > /dev/null
if [ $? -ne 0 ]; then
    echo "Seems that the GPG passphrase was invalid"
    exit $?
fi
rm pom.xml.asc

echo ""
echo "Starting release process for $type/$version"
echo "Working in $PWD"
echo "Press return to continue or CTRL-C to abort"
read

git flow $type start $version

"Cleaning project"
mvn clean > /dev/null

"Updating license information"
mvn license:update-project-license license:update-file-header > /dev/null
git commit -am "Updated license information."

./scripts/bumb-version.sh $version

echo ""
echo "Update and commit CHANGELOG before continuing!"
echo "Press return to continue or CTRL-C to abort"
read

git flow $type finish -pm "juniter version $version" $version
git checkout v$version

echo ""
echo "If everything went fine artifacts can be deployed to staging repository"
echo "After artifacts are deployed, login to https://oss.sonatype.org/ and complete the release"
echo "Press return to continue deploying or CTRL-C to abort"
read

mvn -Pprepare-deploy,prepare-release -Dgpg.passphrase=$passphrase clean deploy
