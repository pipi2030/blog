import requests
from lxml import etree


def get_index():
    # 获取网页公告列表html
    BASE_URL = "http://zzfy.hncourt.gov.cn/public/search.php?p=2&locationID=&k_w=%BA%D3%C4%CF%CA%A1%D6%A3%D6%DD%CA%D0%D6%D0%BC%B6%C8%CB%C3%F1%B7%A8%D4%BA%B9%AB%B8%E6"
    response = requests.get(BASE_URL)
    response.encoding = "gb2312"
    return response.text


def get_url(response):
    # 解析网页公告列表，获得公告详情页url
    html = etree.HTML(response)
    urls = html.xpath('//p[@class="result_item"]//a/@href')
    for url in urls:
        yield 'http://zzfy.hncourt.gov.cn/' + url


def get_detail(url):
    # 通过url获得详情页url
    response = requests.get(url)
    response.encoding = 'gb2312'
    return response.text

def parse_detail(response):
    html = etree.HTML(response)
    urls = html.xpath('//p[@class="result_item"]//a/@href')


if __name__ == '__main__':
    response = get_index()
    urls = get_url(response)





