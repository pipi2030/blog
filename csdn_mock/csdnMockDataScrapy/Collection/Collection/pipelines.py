# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter


class CollectionPipeline:
    def process_item(self, item, spider):
        return item
from openpyxl import Workbook

class DoubanPipeline(object):
    def __init__(self):
        self.wb = Workbook() # 类实例化
        self.ws = self.wb.active # 激活工作表
        self.ws.append(['img', 'title', 'url']) # 添加表头
    def process_item(self, item, spider):
        data = [item["img"],item["title"],item["url"]]
        self.ws.append(data) # 将数据以行的形式添加到工作表中
        self.wb.save('test05.xlsx') # 保存
        return item

