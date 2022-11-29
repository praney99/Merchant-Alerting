#!/bin/bash

set -e -x

./mvnw -s ./.mvn/settings.xml clean install -DskipTests=true
