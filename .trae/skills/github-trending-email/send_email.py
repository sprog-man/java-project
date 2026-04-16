import json
import smtplib
from email.mime.text import MIMEText
from email.utils import formataddr

def generate_summary(repos):
    summary = "# GitHub今日热门项目前5名\n\n"
    
    for i, repo in enumerate(repos, 1):
        summary += f"## {i}. {repo['owner']}/{repo['repo']}\n"
        summary += f"### 项目概述\n{repo['description']}\n\n"
        summary += f"### 技术栈\n从README中提取的技术栈信息\n\n"
        summary += f"### Star数量\n{repo['stars']}\n\n"
        summary += f"### 项目链接\n{repo['url']}\n\n"
        summary += "---\n\n"
    
    return summary

def send_email(subject, content):
    # 发件人邮箱（请替换为你的QQ邮箱）
    sender = '2770554403@qq.com'  # 请填入你的QQ邮箱
    # QQ邮箱授权码（请替换为你的授权码）
    auth_code = 'tljhhmsjochjdeee'  # 请填入你的QQ邮箱授权码
    # 收件人邮箱（请替换为目标邮箱）
    receiver = '2484511957@qq.com'  # 请填入目标邮箱
    
    msg = MIMEText(content, 'plain', 'utf-8')
    msg['From'] = formataddr(["GitHub Trending", sender])
    msg['To'] = formataddr(["Receiver", receiver])
    msg['Subject'] = subject
    
    try:
        server = smtplib.SMTP_SSL("smtp.qq.com", 465)
        server.login(sender, auth_code)
        server.sendmail(sender, [receiver], msg.as_string())
        server.quit()
        print("Email sent successfully")
    except Exception as e:
        print("Failed to send email:", e)

def main():
    print('读取项目数据...')
    
    # 读取JSON文件
    with open('trending_repos.json', 'r', encoding='utf-8') as f:
        repos = json.load(f)
    
    # 生成中文摘要
    summary = generate_summary(repos)
    print('生成摘要完成')
    
    # 发送邮件
    send_email("GitHub今日热门项目摘要", summary)

if __name__ == '__main__':
    main()