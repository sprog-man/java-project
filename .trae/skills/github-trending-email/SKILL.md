---
name: "github-trending-email"
description: "爬取GitHub今日热门项目并发送邮件。当用户需要获取GitHub热门项目信息并通过邮件接收时调用。"
---

# GitHub Trending Email

## 功能描述

该技能可以：
1. 爬取GitHub今日热门项目前五个
2. 获取每个项目的README文件
3. 生成中文简介摘要，包含项目概述、解决的问题、技术栈和Star数量
4. 发送总结邮件到指定邮箱

## 实现文件

### 脚本1：scraper.py
- 爬取GitHub热门项目
- 获取前五个项目的README
- 保存结果到JSON文件

### 脚本2：send_email.py
- 读取JSON文件
- 生成中文摘要
- 发送邮件到指定邮箱

## 使用方法

1. 安装依赖：`pip install requests pyquery`
2. 运行爬虫：`python scraper.py`
3. 配置邮箱信息
4. 发送邮件：`python send_email.py`