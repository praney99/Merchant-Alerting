#!/bin/bash

set -e -x

#Just runs Tests without IT/API in the name.
./mvnw -s ./.mvn/settings.xml test -Punit-tests

chmod 777 ./target/surefire-reports

mkdir -p test_output/unit_tests
cp -a ./target/surefire-reports/. test_output/unit_tests