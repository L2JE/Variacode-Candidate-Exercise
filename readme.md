# Notification Rundeck Plugin - Variacode Candidate Execise
+ Author: Leonel Jesus Juarez Etchenique

This is a simple Rundeck Notification plugin implementation.

## Install

First, download the "Rundeck Launcher" [Rundeck](http://rundeck.org/downloads.html)

    mkdir rundeck
    cd rundeck
    cp ../rundeck-launcher-2.10.2.jar .
    mkdir libext

## Run rundeck

    java -jar rundeck-launcher-2.10.2.jar

## See the results every time you make a change while Rundeck is running
Building the project and copying the .jar to the Rundeck external libraries directory
    
    ./gradlew build
    ./cplib path/to/rundeck/libext

If you place rundeck directory right next to the project root you can run ./cplib without any parameters

