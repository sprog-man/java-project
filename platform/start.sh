#!/bin/bash

# 启动脚本

# 检查是否存在 jar 文件
if [ ! -f "target/platform-0.0.1-SNAPSHOT.jar" ]; then
    echo "Error: platform-0.0.1-SNAPSHOT.jar not found!"
    echo "Please run 'mvn clean package' first."
    exit 1
fi

# 启动应用
echo "Starting pet companion platform..."
java -jar target/platform-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
