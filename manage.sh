#!/bin/bash

#export JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre 
export JAVA_HOME=/usr/lib/jvm/java-7-oracle/jre 
export SDK_HOME=/home/benito/opt/android-sdk-linux/platform-tools/
 
case "$1" in
build)
  BUILDMODE="$2"
  if [[ -z "$BUILDMODE" ]]; then BUILDMODE=debug; fi
  ant $BUILDMODE || (echo "FAILED!"; exit)

  $SDK_HOME/adb uninstall de.benibela.myandroid || (echo "FAILED!"; exit)
  $SDK_HOME/adb install bin/myandroid-$BUILDMODE.apk || (echo "FAILED!"; exit)
;;

install)
  if [[ $2 == "release" ]]; then BUILDMODE=release
  else BUILDMODE=debug;  fi


#  $SDK_HOME/adb uninstall de.benibela.myandroid || (echo "FAILED!"; exit)
  $SDK_HOME/adb install -r bin/myandroid-$BUILDMODE.apk || (echo "FAILED!"; exit)
  
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


