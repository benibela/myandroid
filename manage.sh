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

brokenServers)
   export PASSWORD=password
   export KEYSTORE=android/res/raw/keystore.bks 
   export SERVERLIST=../data/libraries/brokenServers.list
   export RESSERVERLIST=android/res/values/brokenServers.xml
   export TMPFILE=__vl__certificate.pem
   export KEYTOOL=keytool
   #/usr/lib/jvm/java-6-sun/jre/bin/keytool
   export BOUNCYCASTLE=/usr/share/java/bcprov-1.46.jar
   FINGERPRINTFILE=keystore.bks.fingerprints
   TEMPKEYSTORE=__vl__keystore.bks 


   echo '<?xml version="1.0" encoding="utf-8"?>' > $RESSERVERLIST
   echo "<resources>" >> $RESSERVERLIST
   echo '<string-array name="broken_servers">' >> $RESSERVERLIST
   
   rm $KEYSTORE
   rm $FINGERPRINTFILE
   i=0
   while read server; do
     if [[ -n "$server" ]]; then      
       echo
       echo
       echo =====================================================================
       echo ==========================$server==========================
       echo =====================================================================
       echo "<item>CN=$server</item>" >> $RESSERVERLIST
       echo something | openssl s_client -connect $server:443 > $TMPFILE
       if grep -qv "BEGIN CERTIFICATE" $TMPFILE; then 
         #openssl fails to negotiate protocol version for some servers. only tls1 prints certificate data
         echo something | openssl s_client -connect $server:443 -tls1 > $TMPFILE 
       fi
       
       cp $KEYSTORE $TEMPKEYSTORE
       yes | $KEYTOOL       -import       -v       -trustcacerts       -alias $i       -file <(openssl x509 -in $TMPFILE)       -keystore $KEYSTORE       -storetype BKS       -provider org.bouncycastle.jce.provider.BouncyCastleProvider       -providerpath $BOUNCYCASTLE       -storepass $PASSWORD
       
       echo -en "$server\t" >> $FINGERPRINTFILE
       if diff -q $KEYSTORE $TEMPKEYSTORE; then
         echo FAIL >> $FINGERPRINTFILE
       else
         keytool -list -keystore $KEYSTORE -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath $BOUNCYCASTLE -storetype BKS -storepass $PASSWORD  | grep -E "trusted|fingerprint" | while read line1; do read line2; echo "$line1: $line2"; done | sort -n | tail -1  | sed -Ee 's/,[^:]+,//' >> $FINGERPRINTFILE
       fi
       
       ((i=i+1))
     fi
   done <  $SERVERLIST

   echo '</string-array>' >> $RESSERVERLIST
   echo "</resources>" >> $RESSERVERLIST
   
   echo 
   echo
   echo
   
    $KEYTOOL -list -keystore $KEYSTORE -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath $BOUNCYCASTLE -storetype BKS -storepass $PASSWORD
   
   echo 
   echo
   echo
   echo 
   echo
   echo
   rm $TEMPKEYSTORE

   #keytool -list -keystore $KEYSTORE -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath $BOUNCYCASTLE -storetype BKS -storepass $PASSWORD  | grep -E "trusted|fingerprint" | while read line1; do read line2; echo "$line1: $line2"; done | sort -n | paste ../data/libraries/brokenServers.list - | sed -Ee 's/, *[A-Za-z]{3} *[0-9]+, *[0-9]{4},//' | tee keystore.bks.fingerprints
   cat $FINGERPRINTFILE

   
   rm $TMPFILE
;;
  
esac


