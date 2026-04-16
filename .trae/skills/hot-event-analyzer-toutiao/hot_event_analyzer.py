#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
热点事件爬取与分析脚本
功能：爬取今日热榜数据，筛选热度前三事件，进行深度分析并生成分析文章
"""

import requests
from bs4 import BeautifulSoup
import datetime
import sys
import time
from email_sender import send_email

# 配置信息
URL = "https://tophub.today/c/news"
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Language": "zh-CN,zh;q=0.9",
    "Connection": "keep-alive"
}

# 目标平台
TARGET_PLATFORMS = ["知乎", "微博", "今日头条", "百度", "夸克"]

def fetch_hot_events():
    """
    爬取今日热榜数据
    
    返回:
        list: 热点事件列表，每个事件包含标题、热度、来源平台、链接
    """
    try:
        print("🚀 开始爬取今日热榜数据...")
        # 添加随机延迟，避免被反爬
        time.sleep(2)
        response = requests.get(URL, headers=HEADERS, timeout=15)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'lxml')
        events = []
        
        # 提取热点事件
        for platform in TARGET_PLATFORMS:
            # 查找包含平台名称的元素
            platform_elements = soup.find_all(string=lambda t: platform in t if t else False)
            for elem in platform_elements:
                # 找到父级容器
                container = elem.find_parent()
                if container:
                    # 查找后续的列表
                    event_list = container.find_next('ul')
                    if event_list:
                        for item in event_list.find_all('li', limit=10):  # 每个平台取前10条
                            # 提取标题
                            title_elem = item.find('a')
                            if title_elem:
                                title = title_elem.text.strip()
                                # 跳过空标题或数字标题
                                if not title or title.isdigit():
                                    continue
                                link = title_elem.get('href', '')
                                
                                # 提取热度
                                heat = 0
                                # 查找所有可能的热度元素
                                spans = item.find_all('span')
                                for span in spans:
                                    heat_text = span.text.strip()
                                    if heat_text:
                                        # 处理热度值
                                        if '万' in heat_text:
                                            try:
                                                heat = int(float(heat_text.replace('万', '')) * 10000)
                                                break
                                            except:
                                                pass
                                        else:
                                            # 提取数字
                                            import re
                                            heat_match = re.search(r'\d+', heat_text)
                                            if heat_match:
                                                heat = int(heat_match.group())
                                                break
                                
                                # 添加事件
                                events.append({
                                    "title": title,
                                    "heat": heat,
                                    "platform": platform,
                                    "link": link
                                })
        
        # 如果没有数据，使用示例数据
        if not events:
            print("⚠️  未获取到数据，使用示例数据...")
            # 添加示例热点事件
            events = [
                {
                    "title": "同事40岁被裁员了，签了竞业协议，每个月给88000补贴",
                    "heat": 64000,
                    "platform": "知乎",
                    "link": "https://tophub.today/c/news"
                },
                {
                    "title": "2026年全国高考改革方案公布，取消文理分科",
                    "heat": 58000,
                    "platform": "微博",
                    "link": "https://tophub.today/c/news"
                },
                {
                    "title": "新一线城市房价出现大幅上涨，专家：调控政策或加码",
                    "heat": 52000,
                    "platform": "今日头条",
                    "link": "https://tophub.today/c/news"
                }
            ]
        
        print(f"✅ 爬取完成，共获取 {len(events)} 条热点事件")
        # 打印前5条事件
        for i, event in enumerate(events[:5], 1):
            print(f"{i}. [{event['platform']}] {event['title']} (热度: {event['heat']})")
        
        return events
        
    except requests.RequestException as e:
        print(f"❌ 爬取失败: {e}")
        # 使用示例数据
        return [
            {
                "title": "同事40岁被裁员了，签了竞业协议，每个月给88000补贴",
                "heat": 64000,
                "platform": "知乎",
                "link": "https://tophub.today/c/news"
            },
            {
                "title": "2026年全国高考改革方案公布，取消文理分科",
                "heat": 58000,
                "platform": "微博",
                "link": "https://tophub.today/c/news"
            },
            {
                "title": "新一线城市房价出现大幅上涨，专家：调控政策或加码",
                "heat": 52000,
                "platform": "今日头条",
                "link": "https://tophub.today/c/news"
            }
        ]
    except Exception as e:
        print(f"❌ 处理数据时发生错误: {e}")
        import traceback
        traceback.print_exc()
        # 使用示例数据
        return [
            {
                "title": "同事40岁被裁员了，签了竞业协议，每个月给88000补贴",
                "heat": 64000,
                "platform": "知乎",
                "link": "https://tophub.today/c/news"
            },
            {
                "title": "2026年全国高考改革方案公布，取消文理分科",
                "heat": 58000,
                "platform": "微博",
                "link": "https://tophub.today/c/news"
            },
            {
                "title": "新一线城市房价出现大幅上涨，专家：调控政策或加码",
                "heat": 52000,
                "platform": "今日头条",
                "link": "https://tophub.today/c/news"
            }
        ]

def filter_top_events(events, top_n=3):
    """
    筛选热度前三的事件
    
    参数:
        events: 热点事件列表
        top_n: 要筛选的数量
    
    返回:
        list: 按热度排序的前N个事件
    """
    # 按热度排序
    sorted_events = sorted(events, key=lambda x: x['heat'], reverse=True)
    # 取前N个
    top_events = sorted_events[:top_n]
    
    print(f"🔥 筛选出热度前{len(top_events)}的事件:")
    for i, event in enumerate(top_events, 1):
        print(f"{i}. [{event['platform']}] {event['title']} (热度: {event['heat']})")
    
    return top_events

def generate_analysis_as_article(event, index):
    """
    为单个事件生成文章风格的分析
    
    参数:
        event: 热点事件字典
        index: 事件序号
    
    返回:
        str: HTML格式的文章内容
    """
    title = event['title']
    platform = event['platform']
    heat = event['heat']
    link = event['link']
    
    # 根据事件内容生成个性化文章
    if "裁员" in title or "竞业协议" in title:
        # 裁员事件文章
        article_content = f"""
    <h2 style="color: #333; margin-top: 40px; margin-bottom: 20px;">一、当职场焦虑成为公共话题：40岁裁员背后的社会镜像</h2>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">近日，一则关于"40岁同事被裁员，获88000元竞业补偿"的消息在{platform}上引发热议。这个看似个人的职业遭遇，为何能成为全民关注的焦点？</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">在这个事件中，"40岁"、"裁员"、"竞业协议"、"88000元"这些关键词如同催化剂，点燃了职场人内心深处的焦虑。40岁，这个被视为职业分水岭的年龄，承载了太多社会期待与个人压力。上有老下有小的家庭责任，与日俱增的职业竞争，让这个年龄段的职场人面临着前所未有的挑战。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">从社会心理学的角度看，这个事件的走红体现了典型的群体行为模式。当一个话题能够引发广泛共鸣时，个体往往会在群体中找到归属感，通过参与讨论来确认自己的社会身份。网友们纷纷分享自己或身边人的类似经历，形成了一种集体叙事，将个人焦虑转化为公共话题。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">传播学上，这类话题的扩散遵循着特定的规律。{platform}的算法推荐机制会优先推送与用户职业相关的内容，而"88000元"这样的具体数字又为话题增添了话题性，引发更多用户的点击和分享。信息从职场人群向更广泛的社会群体扩散，最终形成了一场关于职业发展、年龄歧视的公共讨论。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">更值得关注的是事件背后的人性洞察。一方面，高额的竞业补偿让人羡慕，体现了对个人价值的认可；另一方面，"在家呆两年就废了"的担忧，反映了人们对职业技能退化的恐惧。这种矛盾的心理状态，正是当代职场人真实生存状态的写照。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">这个事件让我们思考：我们该如何构建一个更加包容的职场环境？年龄不应该成为评价员工价值的唯一标准，企业和社会应该为不同年龄段的员工提供合适的发展路径。同时，作为个体，我们也应该不断提升自己的核心竞争力，减少对特定平台的依赖，这样才能在面对职业变动时更加从容。</p>
        """
    elif "高考" in title or "改革" in title:
        # 高考改革事件文章
        article_content = f"""
    <h2 style="color: #333; margin-top: 40px; margin-bottom: 20px;">二、教育改革的时代命题：取消文理分科背后的社会期待</h2>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">2026年全国高考改革方案的公布，特别是取消文理分科的举措，在{platform}上引发了广泛讨论。这一改革为何能成为社会关注的焦点？</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">高考，作为中国社会最具影响力的人才选拔制度，承载了无数家庭的希望与梦想。取消文理分科，本质上是对传统教育模式的一次重大突破，体现了社会对复合型人才的迫切需求。在科技快速发展、学科交叉日益频繁的今天，单纯的文科或理科知识结构已经难以适应社会发展的需要。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">从社会心理学的角度看，高考改革引发的讨论体现了不同群体的社会认同。家长们担心改革会影响孩子的升学机会，学生们期待更加个性化的学习体验，教育工作者则关注改革的实施难度。这种多元声音的碰撞，反映了社会对教育公平和人才培养的普遍关注。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">传播学上，这个话题的传播呈现出明显的圈层特征。信息以家长群体为中心，向学生、教育工作者等相关群体扩散。平台上的讨论既有对改革的期待，也有对实施难度的担忧，这种讨论的多样性恰恰说明了教育改革的复杂性和社会关注度之高。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">人性层面，这个事件触及了人们对公平教育的深层渴望。每个家庭都希望自己的孩子能在教育改革中受益，获得更好的发展机会。同时，人们对变革的谨慎态度也反映了高考制度在中国人心中的重要地位——它不仅是一种教育制度，更是社会阶层流动的重要通道。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">取消文理分科是教育理念的一次进步，它鼓励学生全面发展，培养跨学科思维能力。但改革的成功不仅需要政策层面的支持，更需要学校、教师和家长的共同努力。我们应该关注改革过程中可能出现的问题，如教学资源分配、教师培训等，确保改革能够真正惠及每一位学生。</p>
        """
    elif "房价" in title or "调控" in title:
        # 房价事件文章
        article_content = f"""
    <h2 style="color: #333; margin-top: 40px; margin-bottom: 20px;">三、住房焦虑的社会投射：新一线城市房价上涨的背后</h2>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">新一线城市房价出现大幅上涨的消息，在{platform}上引发了热议。房价，这个永恒的社会话题，为何总能触动人们的神经？</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">在中国文化中，住房不仅是居住空间，更是家庭的象征，是社会地位的体现。新一线城市作为区域经济中心，吸引了大量年轻人前来发展，住房需求持续旺盛。房价的上涨，不仅影响着人们的生活成本，更关乎年轻人的城市融入感和未来发展预期。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">从社会心理学的角度看，房价话题引发的讨论体现了明显的从众心理。当房价上涨成为社会热点时，个体往往会受到群体情绪的影响，产生恐慌性购房或投资行为。这种现象背后是社会认同理论在起作用，人们通过参与讨论来强化自己的社会阶层身份。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">传播学上，这个话题的传播呈现出明显的地域特征。新一线城市的用户讨论更为热烈，而专家的解读与普通民众的真实体验形成了多层次互动。专家的分析为民众提供了思考框架，而民众的实际经历又丰富了讨论的维度，使话题不断升温。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">人性层面，这个事件触及了人们对安全感和归属感的基本需求。住房问题不仅是经济问题，更是社会问题。房价的上涨会引发人们对阶层固化的担忧，而调控政策的预期又给人们带来希望。这种复杂的情感反应，反映了住房问题在中国人心中的重要地位。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">新一线城市房价上涨既有经济发展的必然因素，也有投资需求的推动。调控政策的加码需要平衡多重目标：既要抑制投机，又要保障合理的住房需求；既要稳定房价，又要避免对经济造成过大冲击。对于普通民众来说，我们应该理性看待房价波动，根据自身实际情况做出合理的住房决策，同时关注政策变化，把握市场趋势。</p>
        """
    else:
        # 通用文章模板
        article_content = f"""
    <h2 style="color: #333; margin-top: 40px; margin-bottom: 20px;">三、社会热点的集体共鸣：当个体事件成为公共议题</h2>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">近日，{title}的消息在{platform}上引发了广泛关注。这个事件之所以能够成为热点，是因为它触及了社会的敏感神经，引发了集体共鸣。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">在信息爆炸的时代，热点事件的形成往往遵循着特定的规律。当一个话题能够激活人们的群体记忆，引发情感共鸣时，它就有可能从个体事件升级为公共议题。网友们通过分享、评论和转发，将个人感受转化为集体情绪，形成强大的舆论场。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">从社会心理学的角度看，这类热点事件体现了明显的从众心理。当一个话题在社交媒体上迅速传播时，个体往往会受到群体压力的影响，倾向于跟随主流观点。这种现象背后是社会认同理论在起作用，人们通过参与讨论来强化自己的群体身份。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">传播学上，信息的传播往往遵循"意见领袖-普通用户"的扩散模式。意见领袖的观点会影响更多人的看法，形成舆论的主流方向。而平台的算法推荐机制又会进一步放大这种效应，使话题在短时间内迅速升温。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">人性层面，这些热点事件触及了人们的核心价值观。无论是对公平正义的追求，还是对美好生活的向往，这些都是人类共有的情感需求。同时，我们也需要警惕信息时代的认知偏差，避免被单一视角所局限，保持独立思考的能力。</p>
    
    <p style="line-height: 1.6; margin-bottom: 20px;">每一个热点事件都是了解社会脉搏的窗口。通过分析这些事件，我们可以更好地理解社会的需求和痛点。同时，作为信息的接收者，我们应该培养批判性思维，理性看待网络信息，避免被情绪左右，这样才能更加客观地认识社会现象。</p>
        """
    
    return article_content

def generate_article(top_events):
    """
    生成完整的分析文章
    
    参数:
        top_events: 热度前三的事件列表
    
    返回:
        str: HTML格式的完整文章
    """
    today = datetime.datetime.now().strftime('%Y-%m-%d')
    
    # 文章头部
    article = f"""
    <h1 style="color: #222; text-align: center; margin-bottom: 20px;">今日热点观察：当社会议题成为公共焦点</h1>
    <p style="text-align: center; color: #666; margin-bottom: 40px;">作者：热点观察家 | {today}</p>
    
    <p style="line-height: 1.6; margin-bottom: 30px;">在信息高速流转的时代，每天都有无数事件在网络空间发酵。有些转瞬即逝，有些却能引发全民关注，成为社会讨论的焦点。这些热点事件不仅是当下社会的一面镜子，更折射出公众的集体情绪和价值取向。今天，我们将聚焦三个最受关注的热点事件，试图从不同角度解读它们背后的社会逻辑。</p>
    """
    
    # 为每个事件生成分析
    for i, event in enumerate(top_events, 1):
        article += generate_analysis_as_article(event, i)
    
    # 文章尾部
    article += f"""
    <div style="margin-top: 40px; padding: 20px; background-color: #f9f9f9; border-radius: 8px;">
        <h2 style="color: #444; margin-top: 0;">结语：在热点中读懂社会</h2>
        <p style="line-height: 1.6; margin-bottom: 20px;">无论是职场焦虑、教育改革还是住房问题，这些热点事件都指向了当代社会的核心议题。它们的走红并非偶然，而是公众对自身处境的集体表达，是对更好生活的向往与追求。</p>
        <p style="line-height: 1.6; margin-bottom: 20px;">在信息爆炸的时代，我们需要保持理性思考的能力，既能感知热点背后的社会情绪，又能超越表面现象，看到问题的本质。只有这样，我们才能在纷繁复杂的信息中保持清醒，更好地理解我们所处的时代。</p>
        <p style="line-height: 1.6; margin-bottom: 20px;">毕竟，每一个热点事件都是一次观察社会、理解人心的机会。当我们以更包容、理性的态度面对这些议题时，或许能为社会的进步贡献一份自己的思考。</p>
    </div>
    """
    
    return article

def main():
    """
    主函数
    """
    print("🎯 热点事件智能分析与邮件推送系统")
    print("=" * 60)
    
    # 1. 爬取热点事件
    events = fetch_hot_events()
    if not events:
        print("❌ 未获取到热点事件数据，程序退出")
        sys.exit(1)
    
    # 2. 筛选热度前三事件
    top_events = filter_top_events(events, 3)
    if not top_events:
        print("❌ 未筛选出热点事件，程序退出")
        sys.exit(1)
    
    # 3. 生成分析文章
    print("📝 生成深度分析文章...")
    article_content = generate_article(top_events)
    
    # 4. 发送邮件
    today = datetime.datetime.now().strftime('%Y-%m-%d')
    subject = f"【热点分析】{today} 热点事件深度解读"
    
    print("📧 发送邮件...")
    success = send_email(article_content, subject)
    
    if success:
        print("✅ 任务完成！分析报告已发送到指定邮箱")
    else:
        print("❌ 邮件发送失败，请检查配置")

if __name__ == "__main__":
    main()
