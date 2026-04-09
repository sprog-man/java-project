import requests
from pyquery import PyQuery as pq
import json

def get_trending_repos():
    url = 'https://github.com/trending'
    response = requests.get(url, verify=False)
    doc = pq(response.text)
    
    repos = []
    # 找到所有项目条目
    items = doc('.Box-row').items()
    
    for i, item in enumerate(items):
        if i >= 5:  # 只取前5个
            break
        
        # 提取项目信息
        repo_info = item('.h3 a').text().strip()
        if repo_info:
            parts = repo_info.split('/')
            if len(parts) >= 2:
                owner = parts[0].strip()
                repo = parts[1].strip()
                desc = item('p').text() or '无描述'
                stars = item('.octicon-star + span').text() or '0'
                
                # 构建项目URL
                repo_url = f'https://github.com/{owner}/{repo}'
                readme_url = f'https://raw.githubusercontent.com/{owner}/{repo}/master/README.md'
                
                # 获取README内容
                try:
                    readme_response = requests.get(readme_url, verify=False)
                    readme_content = readme_response.text
                except:
                    readme_content = '无法获取README文件'
                
                repos.append({
                    'owner': owner,
                    'repo': repo,
                    'description': desc,
                    'stars': stars,
                    'url': repo_url,
                    'readme': readme_content
                })
    
    return repos

def main():
    print('开始爬取GitHub热门项目...')
    repos = get_trending_repos()
    
    # 保存到JSON文件
    with open('trending_repos.json', 'w', encoding='utf-8') as f:
        json.dump(repos, f, ensure_ascii=False, indent=2)
    
    print(f'爬取完成，共获取{len(repos)}个项目')
    print('结果已保存到trending_repos.json文件')

if __name__ == '__main__':
    main()