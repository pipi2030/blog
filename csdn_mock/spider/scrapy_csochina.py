# -- coding: utf-8 --**
import datetime
import requests
import re
from lxml import etree
import pymysql
import multiprocessing
from requests.exceptions import HTTPError
import random
import time
from time import sleep

# 主要用来爬取coschina开源中国网站上的数据

BASE_URL = 'https://www.oschina.net/MjAyMi8xLzE2/v1/search/index'
data = {
    'type': 'BLOG',
    'keyword': 'python',
    'from': '0',
    'size': '20'
}

connect_info = {
    'host': 'localhost',
    'user': 'root',
    'password': '1173389434xy',
    'port': '3306',
    'db': 'csdnmock'
}
proxy = '221.197.56.176'
proxies = {
    'http': 'http://' + proxy
}
# headers = {
#     'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) '
#                   'AppleWebKit/537.36 (KHTML, like Gecko) '
#                   'Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3880.400 QQBrowser/10.8.4554.400',
# }

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 '
                  'Safari/537.36 Edg/113.0.1774.42 '
}

# KEY_WORDS = ('java',)
# KEY_WORDS = ('java', 'python', 'C++', '知识图谱', 'vue', 'SpringBoot')
# KEY_WORDS = ('前端', '后端', '移动开发', '人工智能', '大数据',
#              '数据结构')


# KEY_WORDS = ('音视频','云原生','云平台','前沿技术','开源','小程序','运维','服务器','用户体验设计')
# KEY_WORDS = ('操作系统',)
# KEY_WORDS = ('学习和成长','搜索','开发工具','游戏','HarmonyOS','区块链','数学','3C硬件',
# '资讯')
# KEY_WORDS = ('云计算智慧零售',)
# KEY_WORDS = ('DHCP','UDP','SMTP','CSN','SSL','以太网','SSH','ISDN','UEFI','路由','网络拓扑','RIA','脚本语言','Ogg','RAW',
# '库','开源软件','FLOSS','数据冗余','跨平台','解释器','Solaris','驱动程序','并行计算','OOP','API','递归函数','代码重构','SDK','UML','gzip','UGC',
# 'InnoDB','网站重构','URL重定向','伪静态','代理服务器','DDL','OpenGL')
# KEY_WORDS = ('ChatGPT','MySQL','AI','Matlab','Qt',
# '数学知识')
# KEY_WORDS = ['CRM','公海和私海','灰度发布','KA','LBS','OA','POI','NPE','PUSH','Rank','Code','Review','SDK','SEM',
# 'SEO','Tag','UGC','IM','EDM','SNS','BD','BDM','BP','BM','DBA','FM','GA','PM','PR','QA','RD','VD','UI','CAC','CPA',
# 'CPC','CPT','DAU','MAU','GMV','KPI','LTV','PV','UV','ROI','SPU','SKU','B端','M端','G端','H端','方法术语','5why分析法','Star法则']
# KEY_WORDS = (
# '网络安全', '云计算', '人工智能', '大数据', '物联网', '区块链', '数字化转型', '虚拟现实', '增强现实', '深度学习', '机器学习', '自然语言处理', '计算机视觉', '数据挖掘', '推荐算法',
# '搜索引擎优化', '社交媒体', '电子商务', '在线教育', '在线医疗', '金融科技', '共享经济', '智能家居', '智能穿戴', '智能交通', '智能制造', '智能农业', '智慧城市', '智慧医疗',
# '智慧旅游', '智慧物流', '智慧零售', '智慧能源', '智慧环保', '智慧政务', '智慧教育', '智慧交通', '智慧公共安全', '智慧金融', '云存储', '云数据库', '云服务器', '云安全', '云备份',
# '云计算平台', '云计算架构', '云计算技术', '云计算基础架构', '云计算应用', '云计算管理', '云计算安全', '云计算存储', '云计算网络', '云计算虚拟化', '云计算资源池', '云计算监控', '云计算性能',
# '云计算容器化', '云计算自动化', '云计算编排', '云计算开发', '云计算数据中心', '云计算数据分析', '云计算数据库', '云计算应用开发', '云计算操作系统', '云计算企业应用', '云计算解决方案',
# '云计算开源软件', '云计算市场', '云计算培训', '云计算咨询', '云计算管理软件', '云计算测试', '云计算高可用', '云计算容灾', '云计算负载均衡', '云计算弹性伸缩', '云计算安全认证', '云计算认证',
# '云计算视频', '云计算音频', '云计算图像', '云计算人脸识别', '云计算语音识别', '云计算自然语言理解', '云计算机器翻译', '云计算视频识别', '云计算广告', '云计算游戏', '云计算移动应用',
# '云计算桌面应用', '云计算虚拟桌面', '云计算物联网', '云计算智能家居', '云计算智能穿戴', '云计算智能交通', '云计算智能制造', '云计算智能农业', '云计算智慧城市', '云计算智慧医疗', '云计算智慧旅游',
# '云计算智慧物流', '云计算智慧零售', '云计算智慧能源', '云计算智慧环保', '云计算智慧政务', '云计算智慧教育', '云计算智慧交通', '云计算智慧公共安全', '云计算智慧金融', '云计算智慧家庭',
# '云计算智慧办公', '云计算智慧商业', '云计算智慧产业', '云计算智慧生活', '云计算智慧健康', '云计算智慧体育', '云计算智慧旅行', '云计算智慧媒体', '云计算智慧文化', '云计算智慧金融', '云计算智慧服务',
# '云计算智慧政务', '云计算智慧教育', '云计算智慧交通', '云计算智慧公共安全', '云计算智慧城市', '云计算智能投顾', '云计算智能客服', '云计算智能营销', '云计算智能推荐', '云计算智能财务',
# '云计算智能客户', '云计算智能商务', '云计算智能安防', '云计算智能物联网', '云计算智能支付', '云计算智能销售', '云计算智能采购', '云计算智能制造', '云计算智能农业', '云计算智能城市',
# '云计算智能医疗', '云计算智能交通', '云计算智能建筑', '云计算智能环保', '云计算智能能源', '云计算智能家居', '云计算智能生活', '云计算智能教育', '云计算智能媒体', '云计算智能金融',
# '云计算智能制造业', '云计算智能物流', '云计算智能交通系统', '云计算智能安全', '云计算智能医疗系统', '云计算智能家电', '云计算智能电力', '云计算智能制造工业4.0', '云计算智能制造技术',
# '云计算智能车联网', '云计算智能制造产业', '云计算智能制造解决方案', '云计算智能化', '云计算智能化工', '云计算智能化建筑', '云计算智能化交通', '云计算智能化医疗', '云计算智能化制造', '云计算智能化物流',
# '云计算智能化生活', '云计算智能化农业', '云计算智能化能源', '云计算智能化城市', '云计算智能化教育', '云计算智能化金融', '云计算智能化运动', '云计算智能化安防', '云计算智能化服务', '云计算智能化商业',
# '云计算智能化军事', '云计算智能化环保', '云计算智能化文化', '云计算智能化旅游', '云计算智能化媒体', '云计算智能化制造业', '云计算智能化物流业', '云计算智能化交通业', '云计算智能化医疗业',
# '云计算智能化教育业', '云计算智能化金融业', '云计算智能化能源业', '云计算智能化城市建设', '云计算智能化安防业', '云计算智能化服务业', '云计算智能化商业业', '云计算智能化旅游业', '云计算智能化媒体业',
# '云计算智能化文化业', '云计算智能化运动业', '云计算智能化军事业', '云计算智能化环保业', '云计算智能化制造企业', '云计算智能化制造业解决方案', '云计算智能化物流企业', '云计算智能化交通企业',
# '云计算智能化医疗企业', '云计算智能化教育企业', '云计算智能化金融企业', '云计算智能化能源企业', '云计算智能化城市建设企业', '云计算智能化安防企业', '云计算智能化服务企业', '云计算智能化商业企业',
# '云计算智能化旅游企业', '云计算智能化媒体企业', '云计算智能化文化企业', '云计算智能化运动企业', '云计算智能化军事企业', '云计算智能化环保企业', '云计算智能化制造业升级', '云计算智能化物流业升级',
# '云计算智能化交通业升级', '云计算智能化医疗业升级', '云计算智能化教育业升级', '云计算智能化金融业升级', '云计算智能化能源业升级', '云计算智能化城市建设业升级', '云计算智能化安防业升级',
# '云计算智能化服务业升级', '云计算智能化商业业升级', '云计算智能化旅游业升级', '云计算智能化媒体业升级', '云计算智能化文化业升级', '云计算智能化运动业升级', '云计算智能化军事业升级', '云计算智能化环保业升级',
# '云计算智能化应用', '云计算智能化平台', '云计算智能化技术', '云计算智能化服务', '云计算智能化产品', '云计算智能化解决方案', '云计算智能化系统', '云计算智能化管理', '云计算智能化工具',
# '云计算智能化应用场景', '云计算智能化发展', '云计算智能化市场', '云计算智能化趋势', '云计算智能化前景', '云计算智能化思维', '云计算智能化创新', '云计算智能化战略', '云计算智能化竞争',
# '云计算智能化合作', '云计算智能化发展模式', '云计算智能化人才', '云计算智能化教育', '云计算智能化课程', '云计算智能化培训', '云计算智能化认证', '云计算智能化考试', '云计算智能化资格证',
# '云计算智能化学习', '云计算智能化教程', '云计算智能化视频', '云计算智能化音频', '云计算智能化图像', '云计算智能化人脸识别', '云计算智能化语音识别', '云计算智能化自然语言理解', '云计算智能化机器翻译',
# '云计算智能化视频识别', '云计算智能化广告', '云计算智能化游戏', '云计算智能化移动应用', '云计算智能化桌面应用', '云计算智能化虚拟桌面', '云计算智能化物联网', '云计算智能化智能家居', '云计算智能化智能穿戴',
# '云计算智能化智能交通', '云计算智能化智能制造', '云计算智能化智能农业', '云计算智能化智慧城市', '云计算智能化智慧医疗', '云计算智能化智慧旅游', '云计算智能化智慧物流', '云计算智能化智慧零售',
# '云计算智能化智慧能源', '云计算智能化智慧环保', '云计算智能化智慧政务', '云计算智能化智慧教育', '云计算智能化智慧交通', '云计算智能化智慧公共安全', '云计算智能化智慧金融', '云计算智能化智慧家庭',
# '云计算智能化智慧办公', '云计算智能化智慧商业', '云计算智能化智慧产业', '云计算智能化智慧生活', '云计算智能化智慧健康', '云计算智能化智慧体育', '云计算智能化智慧旅行', '云计算智能化智慧媒体',
# '云计算智能化智慧文化', '云计算智能化智慧金融', '云计算智能化智慧服务', '云计算智能化智慧政务', '云计算智能化智慧教育', '云计算智能化智慧交通', '云计算智能化智慧公共安全', '云计算智能化智慧城市',
# '云计算智能化智能投顾', '云计算智能化智能客服', '云计算智能化智能营销', '云计算智能化智能推荐', '云计算智能化智能财务', '云计算智能化智能客户', '云计算智能化智能商')
# KEY_WORDS = ('网络安全', )
KEY_WORDS = ('网络安全', '云计算', '人工智能', '大数据', '物联网', '区块链', '数字化转型', '虚拟现实', '增强现实', '深度学习')


# def get_headers():
#     '''
#     随机获取一个headers
#     '''
#     user_agents = ['Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1',
#                    'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) '
#                    'Version/5.1 Safari/534.50',
#                    'Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11']
#     headers = {'User-Agent': random.choice(user_agents)}
#     return headers


def get_xhr():
    # 解析响应得到的json格式的数据
    # 并从中获取blog的url地址
    num = 0
    response = requests.post(BASE_URL, json=data, headers=headers)
    # 如果当前暂时没找到，等一等，可能被封号了，一会再做一次查询
    while response.status_code == 404 and num < 3:
        sleep(10)
        num = num + 1
        response = requests.post(BASE_URL, json=data, headers=headers)
    search_results = {}
    # 将response对象转化为数据字典，取'search_result'部分 得到文章数据数组
    try:
        search_results = response.json()['articles']
    except HTTPError as e:
        print("页面没找到")
    except Exception as e:
        print(e)
    finally:
        return search_results


def get_blog_url(search_results):
    if not bool(search_results):
        print("search_result 为空")
        return
    for result in search_results:
        url = result['href']
        reading_count = result['viewCount']
        yield url, reading_count


def parse_blog(url, reading_count):
    # 获得博客的相关数据
    response = requests.post(url, headers=headers)
    html = etree.HTML(response.text)
    blog = {}
    try:
        title = html.xpath('//h1/a/text()')[0]
        content_array = html.xpath('//div[@class="article-box__content"]//p/text()')
        # 避免内容超过三千字节(自己数据库设置的最大值)
        content_str = ''.join(content_array)[:3000]
        content = ''.join(content_str.split())
        # tag_array = html.xpath('//code/text()')
        # tags = ' '.join(tag_array)
        update_time_str = html.xpath('//div[contains(text(),"阅读数")]/preceding-sibling::div[1]/text()')[0]
        update_time = datetime.datetime.now().strptime(update_time_str, '%Y/%m/%d %H:%M')
        create_time = update_time
        collections = html.xpath('//span[@data-collect-count]/text()')[0]
        likes = html.xpath('//span[@data-article-like-count]/text()')[0]
        # blog = {
        #     'title': title,
        #     'content': content,
        #     # 'tags': tags,
        #     'publisher_id': 2,
        #     'create_time': create_time,
        #     'update_time': update_time,
        #     'reading_count': reading_count,
        #     'collections': collections,
        #     'likes': likes,
        # }
        blog = (
            title,
            content,
            # 'tags': tags,
            create_time,
            update_time,
            2,
            reading_count,
            collections,
            likes,
        )
    except Exception:
        pass
    return blog


# 获取到的数据插入数据库
# def inert_db(blog):
#     # 如果是none类型，停止运行
#     if blog is None:
#         return
#     # 如果数据字典为空，不再进行后续操作
#     if not bool(blog):
#         return
#     db = pymysql.connect(host='localhost', user='root',
#                          password='1173389434xy', port=3306, db='csdnmock', charset='utf8mb4')
#     # 游标，用来操作数据库
#     cursor = db.cursor()
#     columns = ','.join(blog.keys())
#     values = ','.join(['%s'] * len(blog))
#     sql = 'insert into s_post({columns}) values({values})'.format(columns=columns, values=values)
#     try:
#         cursor.execute(sql, tuple(blog.values()))
#         print('插入成功')
#         db.commit()
#     except Exception as e:
#         db.rollback()
#         print(e)
#     finally:
#         db.close()

# mysql批量插入
def inert_db(blogs):
    # 如果是none类型，停止运行
    if blogs is None:
        return
    # 如果数据字典为空，不再进行后续操作
    if len(blogs) <= 1:
        return
    db = pymysql.connect(host='localhost', user='root',
                         password='1173389434xy', port=3306, db='csdnmock', charset='utf8mb4')
    # 游标，用来操作数据库
    cursor = db.cursor()
    values = ','.join(['%s'] * 10)
    sql = 'insert into s_post(title,content,create_time,update_time,publisher_id,reading_count,likes,' \
          'collections) values(%s,%s,%s,%s,%s,%s,%s,%s)'
    cursor.executemany(sql, blogs)
    print('插入成功')


def main(keyword):
    # 爬取一个页面里对应的所有博客信息并将它传到数据库里
    # 随机暂停爬取一段时间
    time.sleep(random.randint(0, 10))
    page1 = 0
    while len(get_xhr()) != 0:
        data['keyword'] = keyword
        data['from'] = page1
        page1 = page1 + 20
        search_results = get_xhr()
        blog_tuples = get_blog_url(search_results)
        blogs = []
        for blog_tuple in blog_tuples:
            try:
                url = blog_tuple[0]
                reading_count = blog_tuple[1]
                blog = parse_blog(url, reading_count)
                # 如果部分数据没拿到，当前blog为空，跳过本数据，进行下一个循环
                if not bool(blog):
                    continue
                blogs.append(blog)
                # if len(blog) != 0:
                #     print(blog)
            except Exception as e:
                print(e)
        inert_db(blogs)


if __name__ == '__main__':
    # pool = multiprocessing.Pool(8)
    # pool.map(main, list(KEY_WORDS))
    # pool.close()
    # pool.join()
    main('java')
