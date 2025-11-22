#!/usr/bin/env sh
# Gradle wrapper launcher for UNIX
JAVA_HOME=${JAVA_HOME:-}
DIR="$( cd "$( dirname "$0" )" && pwd )"
exec "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
