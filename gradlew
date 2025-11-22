#!/usr/bin/env sh

##############################################################################
# Gradle start-up script for UN*X
##############################################################################

# Add default JAVA_HOME if needed
JAVA_HOME=${JAVA_HOME:-}

DIR="$( cd "$( dirname "$0" )" && pwd )"
exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
