# -- coding: utf-8 --**
import datetime
import requests
import re
from lxml import etree
import pymysql
import multiprocessing
from requests.exceptions import HTTPError
from json.decoder import JSONDecodeError
import random
import time
from time import time
from time import sleep

# 主要用来爬取coschina开源中国网站上的数据
COUNT = 0
BASE_URL = 'https://www.oschina.net/MjAyMi8xLzE2/v1/search/index'
data = {
    "type": "BLOG",
    "keyword": "python",
    "from": "0",
    "size": "20"
}

connect_info = {
    'host': 'localhost',
    'user': 'root',
    'password': '1173389434xy',
    'port': '3306',
    'db': 'csdnmock'
}
proxy = '221.197.66.105'

proxies = {
    "http": "http://%(proxy)s/" % {'proxy': proxy},
    "https": "http://%(proxy)s/" % {'proxy': proxy}
}


headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 "
                  "Safari/537.36 Edg/113.0.1774.42 "
}

KEY_WORDS = ('网络安全', '云计算', '人工智能', '大数据', '物联网', '区块链', '数字化转型', '虚拟现实', '增强现实', '深度学习')


def get_xhr():
    # 解析响应得到的json格式的数据
    num = 0
    response = requests.post(BASE_URL, json=data, headers=headers)
    # 如果当前网络不好暂时没找到，等一等，一会再做一次查询
    while response.status_code == 404 and num < 3:
        sleep(10)
        num = num + 1
        response = requests.post(BASE_URL, json=data, headers=headers)
    search_results = {}
    # 将response对象转化为数据字典，取'articles'部分 得到文章数据数组
    # try:
    #     search_results = response.json()['articles']
    # except HTTPError as e:
    #     print("页面没找到")
    # except Exception as e:
    #     print(e)
    # finally:
    #     return search_results
    search_results = response.json()['articles']
    return search_results


def get_blog_url(search_results):
    # 从json数据中获取blog的url地址和阅读量
    if not bool(search_results):
        print("本关键词相关博客保存完毕")
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
        try:
            update_time_str = html.xpath('//div[contains(text(),"阅读数")]/preceding-sibling::div[1]/text()')[0]
            update_time = datetime.datetime.strptime(update_time_str, '%Y/%m/%d %H:%M')
        except ValueError as e:
            update_time = datetime.datetime.now().strftime('%Y/%m/%d %H:%M')
            pass
        create_time = update_time
        collections = html.xpath('//span[@data-collect-count]/text()')[0]
        likes = html.xpath('//span[@data-article-like-count]/text()')[0]
        # 解析得到的博客数据
        blog = (title,
                content,
                # 'tags': tags,
                3,
                create_time,
                update_time,
                reading_count,
                collections,
                likes)
    except IndexError as e:
        pass
    return blog


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
    sql = 'insert into s_post(title,content,publisher_id,create_time,update_time,reading_count,collections,' \
          'likes) values(%s,%s,%s,%s,%s,%s,%s,%s)'
    cursor.executemany(sql, blogs)
    db.commit()
    print('插入成功')
    db.close()


# 查询当前数据库里数据的个数
def count():
    db = pymysql.connect(host='localhost', user='root',
                         password='1173389434xy', port=3306, db='csdnmock', charset='utf8mb4')
    # 游标，用来操作数据库
    cursor = db.cursor()
    sql = 'select count(*)from s_post'
    cursor.execute(sql)
    return int(cursor.fetchone()[0])


def main(keyword):
    # 爬取一个页面里对应的所有博客信息并将它传到数据库里
    # 随机暂停爬取一段时间
    sleep(random.randint(0, 10))
    page1 = 0
    if not bool(get_xhr()):
        # 有的时候没解析到东西，这个就不解析了
        return
    while len(get_xhr()) != 0:
        data['keyword'] = keyword
        data['from'] = page1
        page1 = page1 + 20
        search_results = get_xhr()
        blog_tuples = get_blog_url(search_results)
        # 准备往blogs里放所有的博客数据
        blogs = []
        for blog_tuple in blog_tuples:
            url = blog_tuple[0]
            reading_count = blog_tuple[1]
            blog = parse_blog(url, reading_count)
            # 如果部分数据没拿到，当前blog为空，跳过本数据，进行下一个循环
            if not bool(blog):
                continue
            blogs.append(blog)
        inert_db(blogs)


if __name__ == '__main__':
    # 获取当前数据表里数据的个数
    count1 = count()
    # 获取当前时间
    start_time = time()
    # pool = multiprocessing.Pool(8)
    # pool.map(main, list(KEY_WORDS))
    # pool.close()
    # pool.join()

    # for keyword in KEY_WORDS:
    #     main(keyword)

    main('java')
    # 获取当前数据表数据的个数
    count2 = count()
    # 获取当前时间
    end_time = time()
    minute = (end_time - start_time) / 60
    print("本次运行成功爬取了 {count} 个数据".format(count=count2 - count1))
    print("本次运行每分钟爬取了 {count} 个数据".format(count=(count2 - count1) / minute))
