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

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 "
                  "Safari/537.36 Edg/113.0.1774.42 "
}
KEYWORD = "音响"
BASE_URL = "https://search.suning.com/"
PROTOCOL = "https://m.suning.com/product/"


def get_index_html():
    response = requests.get(BASE_URL + KEYWORD + "/cp=" + str(page), headers=headers)
    return response


def get_product_url():
    response = get_index_html()
    html = etree.HTML(response.text)
    urls = html.xpath('//div[@class="product-box "]//div[@class="img-block"]/a/@href')
    for url in urls:
        url = PROTOCOL + url.split(".com/")[1]
        yield url


def get_detail_html(url):
    return requests.get(url,headers=headers)


def parse_detail_html(url):
    response = get_detail_html(url)
    html = etree.HTML(response.text)
    type_id = 8
    product_name = html.xpath('//*[@id="product-name"]/text()')[0].strip()
    # 由于价格不容易爬取，所以暂时给它一个随机数
    # 其他网站没有提供的数据也给出相应的随机值
    product_price = random.randint(100, 100000)/20
    product_image = "https:" + html.xpath('//*[@data-name="prodDetail_none_main_image"][1]/img[1]/@src[1]')[0]
    product_stock = random.randint(10,1000)
    sell_count = random.randint(20,50)
    is_recommend = 1
    create_time = "2023-06-03 06:04:55"
    product = {
        "type_id": type_id,
        "product_name": product_name,
        "product_price": product_price,
        "product_image": product_image,
        "product_stock": product_stock,
        "sell_count": sell_count,
        "is_recommend": is_recommend,
        "create_time": create_time
    }
    return product


# 查询当前数据库里数据的个数
def count():
    db = pymysql.connect(host='localhost', user='root',
                         password='1173389434xy', port=3306, db='csdnmock', charset='utf8mb4')
    # 游标，用来操作数据库
    cursor = db.cursor()
    sql = 'select count(*)from s_post'
    cursor.execute(sql)
    return int(cursor.fetchone()[0])


def inert_db(entity):
    # 如果是none类型，停止运行
    if entity is None:
        return
    # 如果数据字典为空，不再进行后续操作
    if len(entity) <= 1:
        return
    db = pymysql.connect(host='localhost', user='root',
                         password='1173389434xy', port=3306, db='shop', charset='utf8mb4')
    # 游标，用来操作数据库
    cursor = db.cursor()
    keys = ','.join(entity.keys())
    values = ','.join(['%s'] * len(entity))
    sql = 'insert into product({keys}) values({values})'.format(keys=keys,values=values)
    cursor.execute(sql, tuple(entity.values()))
    db.commit()
    print('插入成功')
    db.close()


if __name__ == '__main__':
    page = 0
    for i in range(30):
        urls = get_product_url()
        for url in urls:
            product = parse_detail_html(url)
            inert_db(product)
        page = page + 1






