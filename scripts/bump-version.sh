#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Invalid number of arguments"
    echo "USAGE: $0 <version>"
    exit 1
fi

version=$1
working_dir=$PWD

if ! [ -f $working_dir/pom.xml ]
then
    working_dir=$working_dir/..
fi

cd $working_dir

for file in "pom.xml" "README.md"
do
    if ! [ -f $file ]
    then
        echo "$PWD/$file not found, aborting bump"
        exit 1
    fi
done

echo "Updating version to $version in all poms"
mvn versions:set -DnewVersion=$version > /dev/null
mvn versions:commit > /dev/null

echo "Replacing version numbers in readme"
sed -i "s,<version>[^<]*</version>,<version>$version</version>,g" README.md

echo "Committing version changes"
git add pom.xml */pom.xml README.md
git commit -m "Updated to version $version."
