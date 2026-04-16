from hot_event_analyzer import HotEventAnalyzer
from email_sender import send_email, get_email_subject

def main():
    """主函数"""
    print("===== 公众号人文社会探寻 ====")
    print("正在分析今日热点事件...")
    
    # 1. 分析热点事件
    analyzer = HotEventAnalyzer()
    article = analyzer.run()
    
    if not article:
        print("无法生成分析文章，程序结束")
        return
    
    # 2. 发送邮件
    # 直接设置收件人邮箱地址
    recipient_email = "2484511957@qq.com"
    print(f"\n收件人QQ邮箱：{recipient_email}")
    
    subject = get_email_subject()
    
    print("\n正在发送邮件...")
    success = send_email(recipient_email, subject, article)
    
    if success:
        print("邮件发送成功！")
    else:
        print("邮件发送失败，请检查配置后重试")

if __name__ == "__main__":
    main()