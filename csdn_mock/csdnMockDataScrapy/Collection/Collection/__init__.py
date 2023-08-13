#方案1 可获取全部标题与对应url
import scrapy
# from items import secret
class InfoSpider(scrapy.Spider):
    #准备获取网站全部信息
    name = '0100'
    # start_urls = []
    # for i in range(1,8):
    #     start_urls.append(f'www.baidu.com')
    urls = ['http://www.baidu.com']
    def parse(self, response):
        # St1 爬取当前页面藏品url
        try:
            print(response.url);
            # urls =  response.css("a.stui-vodlist__thumb.lazyload::attr(href)").getall()#根据具体网站替换选择器选择方式
            # rooturl = "http://www.baidu.com"
            # for url in urls:
            #     url = rooturl + url
            #     yield scrapy.Request(url, callback=self.parse_collection)  # 对接到下一个函数，进行爬取
        except:
            pass
        # #St2 链接到下一页
        # try:
        #     nest_page = response.xpath("//a[text()='下一页']/@href").get()
        #     if nest_page is not None:#判断是否是最后一页了
        #         next_page = rooturl + nest_page
        #         yield response.follow(next_page, callback=self.parse)
        # except:
        #     pass


    # def parse_collection(self, response):
    #     # 输出本详情页的内容，利用xpath，这里我采用了枚举法，全部列了出来
    #     try:
    #         item = secret()
    #         try:
    #             item['title'] = response.css("h4.title::text").get()
    #         except:
    #             pass
    #         try:
    #             item['url'] = response.url
    #         except:
    #             pass
    #         try:
    #             item['img'] = response.url
    #         except:
    #             pass
    #         yield item#保存本藏品项所有信息
    #     except:#如果前面任意一个报错，让程序能继续执行，不至于一个也爬取不到
    #         pass





