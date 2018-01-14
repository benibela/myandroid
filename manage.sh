#!/bin/bash

if [ -z "$ANDROID_HOME" ]; then
  if [ -d ~/opt/android/sdk/platform-tools/ ]; then ANDROID_HOME=~/opt/android/sdk/
  else echo Failed to find Android SDK. Set ANDROID_HOME variable; exit
  fi
  if [ -d ~/opt/android/studio/jre ]; then JAVA_HOME=~/opt/android/studio/jre; fi
fi
if [ -z "$JAVA_HOME" ]; then
  if [ -d ~/opt/android/studio/jre ]; then JAVA_HOME=~/opt/android/studio/jre 
  else echo Failed to find java. Set JAVA_HOME variable; exit 2
  fi
fi

ADB=$ANDROID_HOME/platform-tools/adb

export ANDROID_HOME
export JAVA_HOME
export JRE_HOME="$JAVA_HOME"
export JDK_HOME="$JAVA_HOME"
export JAVA_ROOT="$JAVA_HOME"
export JAVA_BINDIR="$JAVA_HOME/bin"

 
case "$1" in
build)
  BUILDMODE="$2"
  if [[ -z "$BUILDMODE" ]]; then BUILDMODE=debug; fi
  case "$BUILDMODE" in
  debug) GRADLEMODE=assembleDebug;;
  release) GRADLEMODE=assembleRelease;;
  esac
  
  ./gradlew $GRADLEMODE || { echo "FAILED!"; exit 1; }

#  $ADB uninstall de.benibela.myandroid || (echo "FAILED!"; exit)
  $ADB install -r ./app/build/outputs/apk/app-$BUILDMODE.apk || (echo "FAILED!"; exit)
;;

install)
  if [[ $2 == "release" ]]; then BUILDMODE=release
  else BUILDMODE=debug;  fi


#  $ADB uninstall de.benibela.myandroid || (echo "FAILED!"; exit)
  $ADB install -r ./app/build/outputs/apk/app-$BUILDMODE.apk || (echo "FAILED!"; exit)
  
;;

clean)
  rm android/libs/armeabi/liblclapp.so; 
  rm android/libs/x86/liblclapp.so; 
  cd android
  ant clean
;;

clean-java)
  cd android
  ant clean
;;

mirror)
  BASE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
  source $BASE/../../../manageUtils.sh
  mirroredProject myandroid
  syncHg  
;; 
  
esac


