import requests
from lxml import etree

BASE_URL = "http://zzfy.hncourt.gov.cn/public/search.php"
data = {
    'p': '1',
    'k_w': '河南省郑州市中级人民法院公告'
}


def get_index(index, page):
    data['p'] = page
    response = requests.get(index, json=data)
    response.encoding = "gb2312"
    return response

def parse_index(html):
    response = get_index(BASE_URL,)


def parse_html(response):
    html = etree.HTML(response.text)
    titles = html.xpath('//a/font/text()')
    contents = html.xpath('//p[@class="result_item"]/following-sibling::*[1]/text()')
    publish_time = html.xpath('//p[@class="result_item"]/text()[2]')
    data = [titles, contents, publish_time]
    return data


if __name__ == '__main__':
    page = 1
    get_index(BASE_URL, page)
