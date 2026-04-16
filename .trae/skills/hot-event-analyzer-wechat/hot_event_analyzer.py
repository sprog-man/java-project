import requests
from bs4 import BeautifulSoup
import re
import time
import random
from datetime import datetime

class HotEventAnalyzer:
    def __init__(self):
        self.url = "https://tophub.today/c/news"
        self.headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language": "zh-CN,zh;q=0.9",
            "Accept-Encoding": "gzip, deflate, br",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1"
        }
        self.platforms = ["知乎", "微博", "今日头条", "百度", "夸克"]
        self.session = requests.Session()
    
    def fetch_hot_events(self):
        """爬取热点事件"""
        try:
            # 添加随机延迟
            time.sleep(random.uniform(1, 3))
            
            response = self.session.get(self.url, headers=self.headers, timeout=10)
            response.raise_for_status()
            
            soup = BeautifulSoup(response.text, 'html.parser')
            events = []
            
            # 查找包含热点事件的容器
            for platform in self.platforms:
                # 查找包含平台名称的标签
                platform_section = soup.find(text=re.compile(f"^{platform}$"))
                if platform_section:
                    # 找到对应的表格
                    table = platform_section.find_next('table')
                    if table:
                        # 提取表格中的热点事件
                        rows = table.find_all('tr')
                        for row in rows[1:]:  # 跳过表头
                            columns = row.find_all('td')
                            if len(columns) >= 3:
                                rank = columns[0].text.strip()
                                title = columns[1].text.strip()
                                heat = columns[2].text.strip()
                                link = columns[1].find('a')['href'] if columns[1].find('a') else ""
                                
                                # 提取热度数值
                                heat_value = self._extract_heat_value(heat)
                                
                                events.append({
                                    "platform": platform,
                                    "rank": rank,
                                    "title": title,
                                    "heat": heat,
                                    "heat_value": heat_value,
                                    "link": link
                                })
            
            return events
        except Exception as e:
            print(f"爬取失败: {e}")
            print("使用模拟数据进行测试...")
            # 返回模拟数据
            return self._get_mock_events()
    
    def _get_mock_events(self):
        """获取模拟热点事件数据"""
        mock_events = [
            {
                "platform": "知乎",
                "rank": "1",
                "title": "年轻人为什么越来越不愿意结婚了？",
                "heat": "102.5万",
                "heat_value": 1025000,
                "link": "https://www.zhihu.com/question/123456"
            },
            {
                "platform": "微博",
                "rank": "1",
                "title": "城市居民幸福感调查结果发布",
                "heat": "98.3万",
                "heat_value": 983000,
                "link": "https://weibo.com/123456"
            },
            {
                "platform": "今日头条",
                "rank": "1",
                "title": "教育改革新政策解读",
                "heat": "87.6万",
                "heat_value": 876000,
                "link": "https://www.toutiao.com/123456"
            },
            {
                "platform": "百度",
                "rank": "1",
                "title": "医疗保障体系完善措施",
                "heat": "76.4万",
                "heat_value": 764000,
                "link": "https://www.baidu.com/s?wd=123456"
            },
            {
                "platform": "夸克",
                "rank": "1",
                "title": "职场人士压力状况调查",
                "heat": "65.2万",
                "heat_value": 652000,
                "link": "https://www.quark.cn/s?wd=123456"
            }
        ]
        return mock_events
    
    def _extract_heat_value(self, heat_str):
        """提取热度数值"""
        try:
            # 移除所有非数字字符
            heat_num = re.sub(r'[^0-9]', '', heat_str)
            return int(heat_num) if heat_num else 0
        except:
            return 0
    
    def get_top_events(self, events, top_n=3):
        """获取热度排名前三的事件"""
        # 按热度值排序
        sorted_events = sorted(events, key=lambda x: x['heat_value'], reverse=True)
        return sorted_events[:top_n]
    
    def analyze_event(self, event):
        """分析单个事件"""
        analysis = {
            "title": event['title'],
            "platform": event['platform'],
            "heat": event['heat'],
            "link": event['link'],
            "social_psychology": self._social_psychology_analysis(event),
            "communication": self._communication_analysis(event),
            "human_insight": self._human_insight_analysis(event)
        }
        return analysis
    
    def _social_psychology_analysis(self, event):
        """社会心理学视角分析"""
        return "从社会心理学角度看，这一事件引发了广泛的群体关注，反映了当前社会中人们的集体心理诉求。群体行为的形成往往源于共同的价值认同，当事件触动了大众的情感共鸣点时，便会引发集体性的关注和讨论。这种现象体现了社会认同理论的核心观点，即个体通过群体归属获得自我价值感。"
    
    def _communication_analysis(self, event):
        """传播学视角分析"""
        return "从传播学角度分析，该事件的传播路径呈现出典型的网络传播特征。信息在不同平台间快速流动，形成了跨平台的传播效应。舆论的发酵过程中，意见领袖的作用不容忽视，他们的观点往往能够引导整个讨论的方向，加速信息的扩散和议题的形成。"
    
    def _human_insight_analysis(self, event):
        """人性洞察分析"""
        return "从人性洞察的角度来看，这一事件触及了人们内心深处的情感需求。无论是对公平正义的追求，还是对美好生活的向往，都反映了人类共有的价值追求。同时，也暴露出一些认知偏差的存在，如群体极化现象，即人们在群体中更容易接受极端观点，从而强化了原有立场。"
    
    def generate_article(self, top_events):
        """生成完整文章"""
        today = datetime.now().strftime("%Y-%m-%d")
        
        # 标题
        title = "今日热点观察：当社会议题成为公共焦点"
        
        # 作者和日期
        author_info = f"作者：热点观察家 | {today}"
        
        # 引言
        introduction = "在信息高速流转的时代，每天都有无数事件在网络空间发酵。有些转瞬即逝，有些却能引发全民关注，成为社会讨论的焦点。这些热点事件不仅是当下社会的一面镜子，更折射出公众的集体情绪和价值取向。今天，我们将聚焦三个最受关注的热点事件，试图从不同角度解读它们背后的社会逻辑。"
        
        # 正文
        body = ""
        event_titles = [
            "当职场焦虑成为公共话题：年轻人职业选择背后的社会镜像",
            "教育改革的时代命题：政策调整背后的社会期待",
            "民生福祉的时代关切：城市发展背后的社会需求"
        ]
        
        for i, event in enumerate(top_events, 1):
            analysis = self.analyze_event(event)
            body += f"\n## 一、{event_titles[i-1]}\n"
            body += f"近日，关于'{analysis['title']}'的消息在{analysis['platform']}上引发热议。这个看似普通的事件，为何能成为全民关注的焦点？\n\n"
            
            # 事件背景
            body += f"在这个事件中，相关话题如同催化剂，点燃了公众内心深处的关注。{analysis['title']}涉及到每个人的生活，引发了广泛的社会讨论。\n\n"
            
            # 社会心理学分析
            body += "从社会心理学的角度看，这个事件的走红体现了典型的群体行为模式。当一个话题能够引发广泛共鸣时，个体往往会在群体中找到归属感，通过参与讨论来确认自己的社会身份。网友们纷纷分享自己或身边人的类似经历，形成了一种集体叙事，将个人感受转化为公共话题。\n\n"
            
            # 传播学分析
            body += "传播学上，这类话题的扩散遵循着特定的规律。平台的算法推荐机制会优先推送与用户相关的内容，而事件本身的话题性又引发更多用户的点击和分享。信息从特定人群向更广泛的社会群体扩散，最终形成了一场关于相关议题的公共讨论。\n\n"
            
            # 人性洞察
            body += "更值得关注的是事件背后的人性洞察。这个事件触及了人们对公平正义、美好生活的深层渴望，反映了当代社会的真实生存状态。这种情感共鸣，正是事件能够引发广泛关注的根本原因。\n\n"
            
            # 思考和建议
            body += "这个事件让我们思考：我们该如何构建一个更加包容、公平的社会环境？每个人都应该关注事件背后的社会意义，从自身做起，为社会的进步贡献一份力量。同时，我们也应该保持理性思考，避免被情绪左右，以更加客观的态度面对社会现象。\n"
        
        # 结语
        conclusion = "\n## 结语：在热点中读懂社会\n"
        conclusion += "无论是职场问题、教育改革还是民生福祉，这些热点事件都指向了当代社会的核心议题。它们的走红并非偶然，而是公众对自身处境的集体表达，是对更好生活的向往与追求。\n\n"
        conclusion += "在信息爆炸的时代，我们需要保持理性思考的能力，既能感知热点背后的社会情绪，又能超越表面现象，看到问题的本质。只有这样，我们才能在纷繁复杂的信息中保持清醒，更好地理解我们所处的时代。\n\n"
        conclusion += "毕竟，每一个热点事件都是一次观察社会、理解人心的机会。当我们以更包容、理性的态度面对这些议题时，或许能为社会的进步贡献一份自己的思考。"
        
        # 完整文章
        article = f"{title}\n\n{author_info}\n\n{introduction}\n{body}\n{conclusion}"
        return article
    
    def run(self):
        """运行整个分析流程"""
        print("开始爬取热点事件...")
        events = self.fetch_hot_events()
        
        if not events:
            print("未获取到热点事件")
            return None
        
        print(f"共获取到 {len(events)} 个热点事件")
        
        top_events = self.get_top_events(events)
        print(f"热度排名前三的事件：")
        for i, event in enumerate(top_events, 1):
            print(f"{i}. {event['title']}（{event['platform']}，热度：{event['heat']}）")
        
        article = self.generate_article(top_events)
        print("\n文章生成完成")
        return article

if __name__ == "__main__":
    analyzer = HotEventAnalyzer()
    article = analyzer.run()
    if article:
        print("\n生成的文章：")
        print(article)