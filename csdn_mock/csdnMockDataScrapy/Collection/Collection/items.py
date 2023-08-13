# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy
class CollectionItem(scrapy.Item):
    url = scrapy.Field()
class MyItem(scrapy.Item):
    url = scrapy.Field()
class Site14Item(scrapy.Item):
    Title = scrapy.Field()#将Title（s)修改成Title
    Measurements = scrapy.Field()
    ObjectType= scrapy.Field()
    ObjectNumber = scrapy.Field()
    Artist = scrapy.Field()
    Place = scrapy.Field()
    Dating = scrapy.Field()
    PhysicalFeatures = scrapy.Field() #有改动
    Material = scrapy.Field()
    CreditLine = scrapy.Field()
    Copyright = scrapy.Field()
    Description = scrapy.Field()
    When = scrapy.Field()
    What = scrapy.Field()
    Who = scrapy.Field()
    Where = scrapy.Field()

    # #还没有设计以下这几个
    # # Acquisition = scrapy.Field()
    # # Provenance = scrapy.Field()
    # # style = scrapy.Field()
    # # Related = scrapy.Field()
class theImperialPalace(scrapy.Item):
    Title = scrapy.Field()
    Paragraph = scrapy.Field()#大段文字
    Period = scrapy.Field()
    Medium = scrapy.Field()
    Format = scrapy.Field()
    Calligrapher = scrapy.Field()#Calligrapher(s)改成Calligrapher
    Dimensions = scrapy.Field()
    Artist = scrapy.Field()#Artist(s)改成Artist
    Origin = scrapy.Field()
class secret(scrapy.Item):
    title = scrapy.Field()
    url = scrapy.Field()
    img = scrapy.Field()
