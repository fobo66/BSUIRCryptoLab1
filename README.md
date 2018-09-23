# BSUIR Cryptography labs - DES algorithm implementation

To see this project in action, run ```./gradlew run``` or ```./gradlew.bat run```

To add options for app, pass args to gradle via: ```./gradlew run --args="-key value"```. 
You can see list of the available command line options below:

 * -key – encryption key, string
 * -m, or -mode – mode of operation, may be ECB, CBC, CFB or OFB
 * -file – path to file with clear text to encrypt