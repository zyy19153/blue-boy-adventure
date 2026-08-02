#!/usr/bin/bash
#cd ./src/

# Method 1
rm -rf out
javac -J-Dfile.encoding=UTF-8 -encoding UTF-8 -d out src/**/*.java
java -cp "out;res" main.Main

# Method 2
# rm -rf out
# javac -d out src/**/*.java
# cp -r res/* out/*
# java -cp out main.Main

# emacs 的 javac 输出到 compilation 乱码问题解决：
# java -XshowSettings:properties -version 2>&1 | grep -E "file.encoding|native.encoding|sun.jnu.encoding"
# 这里会是 gbk
# 
# 因此需要指定 javac 的输出为 utf-8
# 加上：`-J-Dfile.encoding=UTF-8`
