#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
邮件发送模块
功能：将热点分析文章发送到指定邮箱
"""

import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.utils import formataddr
from email.header import Header
import datetime
import sys

def send_email(article_content, subject="【热点分析】今日热点事件深度解读"):
    """
    发送邮件到指定QQ邮箱
    
    参数:
        article_content: HTML格式的文章内容
        subject: 邮件主题
    """
    
    # ==================== 邮箱配置信息 ====================
    # 【请在此处填写您的QQ邮箱地址】
    SENDER_EMAIL = "your_qq_email@qq.com"  
    
    # 【请在此处填写您的QQ邮箱授权码】
    # 获取方式：QQ邮箱 -> 设置 -> 账户 -> 开启SMTP服务 -> 生成授权码
    AUTH_CODE = "your_authorization_code"  
    
    # 【请在此处填写接收邮箱地址】
    RECEIVER_EMAIL = "target_email@qq.com"  
    
    # ==================== 邮件服务器配置 ====================
    SMTP_SERVER = "smtp.qq.com"
    SMTP_PORT = 465  # SSL端口
    
    # ==================== 邮件内容构建 ====================
    # 创建多部分邮件对象
    msg = MIMEMultipart('alternative')
    
    # 设置发件人和收件人（带中文名称）
    msg['From'] = formataddr(["热点分析助手", SENDER_EMAIL])
    msg['To'] = formataddr(["收件人", RECEIVER_EMAIL])
    msg['Subject'] = Header(subject, 'utf-8')
    
    # 添加HTML内容
    html_content = f"""
    <html>
    <head>
        <meta charset="UTF-8">
        <style>
            body {{ font-family: Arial, sans-serif; line-height: 1.6; }}
            .header {{ background-color: #f0f0f0; padding: 20px; text-align: center; }}
            .article {{ padding: 20px; }}
            .footer {{ background-color: #f9f9f9; padding: 10px; text-align: center; font-size: 12px; color: #666; }}
        </style>
    </head>
    <body>
        <div class="header">
            <h1>🔥 热点事件深度分析</h1>
            <p>基于社会心理学视角的专业解读</p>
        </div>
        <div class="article">
            {article_content}
        </div>
        <div class="footer">
            <p>本邮件由自动化系统生成 | {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}</p>
        </div>
    </body>
    </html>
    """
    
    msg.attach(MIMEText(html_content, 'html', 'utf-8'))
    
    # ==================== 发送邮件 ====================
    try:
        # 连接SMTP服务器
        server = smtplib.SMTP_SSL(SMTP_SERVER, SMTP_PORT)
        
        # 登录邮箱
        server.login(SENDER_EMAIL, AUTH_CODE)
        
        # 发送邮件
        server.sendmail(SENDER_EMAIL, [RECEIVER_EMAIL], msg.as_string())
        
        # 退出服务器
        server.quit()
        
        print("✅ 邮件发送成功！")
        return True
        
    except smtplib.SMTPAuthenticationError:
        print("❌ 邮箱认证失败，请检查邮箱地址和授权码是否正确")
        return False
    except smtplib.SMTPException as e:
        print(f"❌ SMTP错误: {e}")
        return False
    except Exception as e:
        print(f"❌ 发送邮件时发生未知错误: {e}")
        return False

if __name__ == "__main__":
    # 测试邮件发送（实际使用时由主程序调用）
    test_content = "<h2>测试邮件</h2><p>这是一封测试邮件</p>"
    send_email(test_content)
