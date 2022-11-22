#!/bin/bash

- chmod +x compile.sh

set -e -x

./mvnw -s ./.mvn/settings.xml clean install -DskipTests=true
