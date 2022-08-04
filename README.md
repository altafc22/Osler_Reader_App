
#e Osler Reader App

Created Osler Reader demo project which reads the record from files and show device user status

## Feature
- Developed in Kotlin and used Courourine to download data from internet
- File Handeling in external Storage
- Minimal UI

## Android Prject Details
- how project works?
  1. Once we installed the app in device it will ask for storage permissions to store status files in ExternalStorage/Documents/Osler files 
  2. If files are not present directory then it will copy deviceUserLisr.txt from asset folder to Osler files directory and if internet is available on internet app will download portalUserList.txt from https://gist.githubusercontent.com/altafc22/8d3ae42989532514bcbd042bdf7ee6d6/raw/5568c450cbc72e54134de2756adeeede14eb9eaf/PortalUserList.txt"
  3. Every time app will try to fetch data from internet. Internet is not there then app will display data from recently updated device user list 
  4. If app downloaded portal user list then it will compare all device list records with newly downloaded portal User list and update all the records in UpdatedDevice list to show on the app  
  5. App is parsing hex values of each record to show status of the user.

- Standalone Java project
  1. This projects purely developed in Java. just created to get idea of logic which is used in will work in app 
  2. Created DeviceUserManager class to consume records from the files and created updated file.
  3. To run this project it only need JDK in system and the the main class of Java prject is OslerReader.java 

## APK
- Download Android APK : <a href="https://github.com/altafc22/Osler_Reader_App/raw/master/app-debug.apk">Osler Reader App</a> 

## Screenshots
|<img src="https://github.com/altafc22/Osler_Reader_App/blob/master/screenshots/ss1.png" width="150" height="300"/>|<img src="https://github.com/altafc22/Osler_Reader_App/blob/master/screenshots/ss2.png" width="150" height="300"/>|<br/>
