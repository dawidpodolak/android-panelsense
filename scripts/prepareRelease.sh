#!/bin/bash

versionCode=$(aapt dump badging app/build/outputs/apk/release/app-release.apk | grep versionCode | awk -F " " '{print $3}' | awk -F "'" '{print $2}')
versionName=$(aapt dump badging app/build/outputs/apk/release/app-release.apk | grep versionName | awk -F " " '{print $4}' | awk -F "'" '{print $2}')

mv app/build/outputs/apk/release/app-release.apk PanelSense_${versionName}_${versionCode}.apk
