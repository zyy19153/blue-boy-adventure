#!/usr/bin/bash
#cd ./src/

# Method 1
rm -rf out
javac -encoding UTF-8 -d out src/**/*.java
java -cp "out;res" main.Main

# Method 2
# rm -rf out
# javac -d out src/**/*.java
# cp -r res/* out/*
# java -cp out main.Main
