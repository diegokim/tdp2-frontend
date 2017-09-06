# tdp2-frontend

##Develop Key Hash Generation

Open a terminal inside $home/.android/ and run:

´keytool -exportcert -alias androiddebugkey -keystore debug.keystore | openssl sha1 -binary | openssl base64´

When asked for a password, type "android" and hit enter.

Copy the generated key hash into:
https://developers.facebook.com/apps/375444156206680/settings/

Thats it!
