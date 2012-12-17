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

for file in "pom.xml" "README.md"
do
    if ! [ -f $working_dir/$file ]
    then
        echo "$working_dir/$file not found, aborting bump"
        exit 1
    fi
done

echo "Setting version to $version in all poms"
mvn versions:set -DnewVersion=$version
mvn versions:commit

echo "Replacing version numbers in readme"
sed -i.bak 's,<version>[^<]*</version>,<version>$version</version>,g' $working_dir/README.md
rm $working_dir/README.md.bak
