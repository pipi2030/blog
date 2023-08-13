import scrapy
from ..items import theImperialPalace
class InfoSpider(scrapy.Spider):
    #准备获取故宫博物院英文网站全部的藏品信息
    name = 'theImperialPalace'
    start_urls = ['https://en.dpm.org.cn/cd/action/ListInfo/?classid=19']
    def parse(self, response):
    #获取故宫博物院英文网站全部的藏品详情页url链接
        # St1 爬取当前页面藏品uerl
        try:
            urls = response.css("ul#listhei li div h3 a::attr(href)").getall()#根据具体网站替换选择器选择方式
            rooturl = "https://en.dpm.org.cn"
            for url in urls:
                url = rooturl + url
                yield scrapy.Request(url, callback=self.parse_collection)  # 对接到下一个函数，进行爬取
        except:
            pass
        #St2 链接到下一页
        try:
            nest_pages = response.xpath("//a[text()='>']/@href").get()
            if nest_pages is not None:#判断是否是最后一页了
                next_page = rooturl + response.xpath("//a[text()='>']/@href").get()
                yield response.follow(next_page, callback=self.parse)
        except:
            pass


    def parse_collection(self, response):
        # 输出本详情页的内容，利用xpath，这里我采用了枚举法，全部列了出来
        try:
            item = theImperialPalace()
            try:

                item['Title'] = response.css("h1.titlebox em::text,h1.titlebox::text").getall()

            except:
                pass
            try:
                item['Paragraph'] = response.xpath("//div[@class='mobileobg']/p/text()|//div[@class='mobileobg']/p/em/text()").getall()
            except:
                pass
            try:
                item['Period'] = response.xpath("//b[contains(text(),'Period')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Medium'] = response.xpath("//b[contains(text(),'Medium')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Format'] = response.xpath("//b[contains(text(),'Format')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Calligrapher'] = response.xpath("//b[contains(text(),'Calligrapher')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Dimensions'] = response.xpath("//b[contains(text(),'Dimensions')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Artist'] = response.xpath("//b[contains(text(),'Artist')]/following-sibling::text()[1]").getall()
            except:
                pass
            try:
                item['Origin'] = response.xpath("//b[contains(text(),'Origin')]/following-sibling::text()[1]").getall()
            except:
                pass
            yield item#保存本藏品项所有信息
        except:#如果前面任意一个报错，让程序能继续执行，不至于一个也爬取不到
            pass





