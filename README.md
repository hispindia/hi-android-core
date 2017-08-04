# hi-android-core
Hisp India android core

Integrate module to project

```
git submodule add https://github.com/hispindia/hi-android-core.git libs/hi-core
```

Config version at top gradle file

```gradle
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    ext {
        // Config version for core
        hiCoreVersion = [
                buildGradle          : "2.3.3",
                androidApt           : "1.8",
                retrolambda          : "3.6.0",
                grgit                : "1.5.0",
                compileSdkVersion    : 26,
                buildToolsVersion    : "26.0.1",
                minSdkVersion        : 16,
                targetSdkVersion     : 26,
                gson                 : "2.8.0",
                okhttp               : "3.8.1",
                retrofit             : "2.1.0",
                retrofitConverterGson: "2.1.0",
                retrofitAdapterRxjava: "2.1.0",
                rxAndroid            : "1.2.0",
                rxJava               : "1.1.5",
                dagger               : "2.0",
                daggerCompiler       : "2.0",
                jsr250Api            : "1.0",
                joda                 : "2.9.3",
                hawk                 : "1.23",
                cicerone             : "1.2.1",
                logentries           : "logentries-android-4.4.1",
                eventbus             : "3.0.0"
        ]
        
    }
 
    repositories {
        jcenter()
    }
    dependencies {
         
    }
}
 
allprojects {
     
}
```
