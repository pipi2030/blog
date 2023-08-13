# import scrapy
# from ..items import Site14Item
# class InfoSpider(scrapy.Spider):
#     #准备获取14号网站全部的中国有关的藏品信息
#     name = 'Site14'
#     start_urls = ['https://www.rijksmuseum.nl/en/search?q=China&p=','https://www.rijksmuseum.nl/en/search?q=China&#x27;s%20collections&p=']
#     def parse(self, response):
#     #获取14号网站所有url链接
#         # St1 爬取当前页面藏品uerl
#         try:
#             urls = response.css("a.link-reverse::attr(href)").getall()
#             rooturl = "https://www.rijksmuseum.nl"
#             for url in urls:
#                 url = rooturl + url
#                 yield scrapy.Request(url, callback=self.parse_collection)  # 对接到下一个函数，进行爬取
#         except:
#             pass
#         #St2 链接到下一页
#         try:
#             next_page = response.css("a.bg-lighter.button-like::attr(href)").getall()[-1]
#             if next_page is not None:
#                 next_page = rooturl + next_page
#                 yield response.follow(next_page, callback=self.parse)
#         except:
#             pass
#
#
#     def parse_collection(self, response):
#         # 输出本详情页的内容，利用xpath，这里我采用了枚举法，全部列了出来
#         try:
#             item = Site14Item()
#             try:
#                 item['Measurements'] = response.xpath(
#                     "//div[h3='Measurements']/p/text()|//div[h3='Measurements']/ul/li/a/text()").get()
#             except:
#                 pass
#             try:
#                 item['Title'] = response.xpath(
#                     "//div[h3='Title(s)']/p/text()|//div[h3='Title(s)']/ul/li/a/text()").get()
#             except:
#                 pass
#             try:
#                 item['ObjectType'] = response.xpath(
#                     "//div[h3='Object type']/p/text()|//div[h3='Object type']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['ObjectNumber'] = response.xpath(
#                     "//div[h3='Object number']/p/text()|//div[h3='Object number']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Artist'] = response.xpath("//div[h3='Artist']/p/text()|//div[h3='Artist']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Place'] = response.xpath(
#                     "//div[h3='Place']/p/a/text()|//div[h3='Place']/p/text()|//div[h3='Place']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Dating'] = response.xpath("//div[h3='Dating']/p/text()|//div[h3='Dating']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['PhysicalFeatures'] = response.xpath(
#                     "//div[h3='Physical features']/p/text()|//div[h3='Physical features']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Material'] = response.xpath(
#                     "//div[h3='Material']/p/text()|//div[h3='Material']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['CreditLine'] = response.xpath(
#                     "//div[h3='CreditLine']/p/text()|//div[h3='CreditLine']/ul/li/a/text()|//div[h3='CreditLine']/ul/li/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Copyright'] = response.xpath(
#                     "//div[h3='Copyright']/p/text()|//div[h3='Copyright']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Description'] = response.xpath(
#                     "//div[h3='Description']/p/text()|//div[h3='Description']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['When'] = response.xpath(
#                     "//div[h3='When']/p/text()|//div[h3='When']/ul/li/a/text()|//div[h3='When']/ul/li/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['What'] = response.xpath("//div[h3='What']/p/text()|//div[h3='What']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Who'] = response.xpath("//div[h3='Who']/p/text()|//div[h3='Who']/ul/li/a/text()").get()
#             except:
#                 pass
#
#             try:
#                 item['Where'] = response.xpath("//div[h3='Where']/p/text()|//div[h3='Where']/ul/li/a/text()").get()
#             except:
#                 pass
#             yield item
#         except:
#             pass
#
#
#
#
#
