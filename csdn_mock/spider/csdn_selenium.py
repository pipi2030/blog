import time
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import StaleElementReferenceException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait
from time import sleep
import multiprocessing
import pymysql
from selenium.webdriver.common.action_chains import ActionChains


# 在这里使用selenium爬去csdn，速度太慢，放弃了

# 设置无头模式
options = webdriver.ChromeOptions()
options.add_argument('--headless')
# 设置全屏模式打开
options.add_argument("--start-maximized")
# 设置超时
TIME_OUT = 10
# 爬取的主页面
BASE_URL = 'https://so.csdn.net/so/search?spm=1000.2115.3001.4498&q={keyword}&t=blog&u='
# 爬取博客时采用的关键词
# KEY_WORDS = ('java','python','C')
KEY_WORDS = ('CMS','CRM','GIS','公海和私海','灰度发布','回归测试','冒烟测试','KA','LBS','OA','POI','NPE','PUSH','Rank','Code','Review','SDK','SEM','SEO','Tag','UGC','IM','EDM','SNS','BD','BDM','BP','BM','DBA','FM','GA','PM','PR','QA','RD','VD','UI','CAC','CPA','CPC','CPT','DAU','MAU','GMV','KPI','LTV','PV','UV','ROI','SPU','SKU','B端','M端','G端','H端','方法术语','5why分析法','Star法则')
browser = webdriver.Chrome(options=options)
wait = WebDriverWait(browser, TIME_OUT)


def scrapy_page(url, condition, loactor):
    # 一个通用的方法
    # 加载列表页，方便接下来解析列表页
    try:
        browser.get(url)
        wait.until(condition(loactor))
    except TimeoutException as e:
        print(e)


def scrapy_index(keyword):
    # 爬取首页
    url = BASE_URL.format(keyword=keyword)
    scrapy_page(url, EC.visibility_of_element_located, (By.XPATH, '//h3'))


def drop_down():
    # 进一步加载列表页，不断往下拉网页
    # 获取页面初始高度
    js = "return action=document.body.scrollHeight"
    height = browser.execute_script(js)

    # 将滚动条调整至页面底部
    browser.execute_script('window.scrollTo(0, document.body.scrollHeight)')
    time.sleep(5)

    # 定义初始时间戳（秒）
    t1 = int(time.time())

    # 定义循环标识，用于终止while循环
    status = True

    # 重试次数
    num = 0

    while status:
        # 获取当前时间戳（秒）
        t2 = int(time.time())
        # 判断时间初始时间戳和当前时间戳相差是否大于30秒，小于30秒则下拉滚动条
        if t2 - t1 < 30:
            new_height = browser.execute_script(js)
            if new_height > height:
                time.sleep(1)
                browser.execute_script('window.scrollTo(0, document.body.scrollHeight)')
                try:
                    load_btn = browser.find_element_by_xpath('//div[@class="so-load-data"]')
                    ActionChains(browser).move_to_element(load_btn).click().perform()
                except NoSuchElementException as e:
                    pass
                # 重置初始页面高度
                height = new_height
                # 重置初始时间戳，重新计时
                t1 = int(time.time())
        elif num < 3:  # 当超过30秒页面高度仍然没有更新时，进入重试逻辑，重试3次，每次等待30秒
            time.sleep(3)
            num = num + 1
        else:  # 超时并超过重试次数，程序结束跳出循环，并认为页面已经加载完毕！
            print("滚动条已经处于页面最下方！")
            status = False
            # 滚动条调整至页面顶部
            browser.execute_script('window.scrollTo(0, 0)')
            break


def parse_page():
    # 解析列表页 得到博客详情页url
    elements = browser.find_elements_by_xpath('//h3/a')
    for element in elements:
        url = element.get_attribute('href')
        yield url


def scrapy_detail(url):
    # 通过url加载博客详情页
    scrapy_page(url, EC.visibility_of_element_located, (By.XPATH, '//h1'))


def parse_detail():
    try:
        title = browser.find_element_by_xpath('//h1').text
    except NoSuchElementException:
        pass
    return {
        'title': title,
    }


# 获取到的数据插入数据库
def inert_db(blog):
    db = pymysql.connect(host='localhost', user='root',
                         password='1173389434xy', port=3306, db='csdnmock', charset='utf8mb4')
    # 游标，用来操作数据库
    cursor = db.cursor()
    columns = ','.join(blog.keys())
    values = ','.join(['%s'] * len(blog))
    sql = 'insert into s_post({columns}) values({values})'.format(columns=columns, values=values)
    try:
        cursor.execute(sql, tuple(blog.values()))
        print('插入成功')
        db.commit()
    except Exception as e:
        db.rollback()
        print(e)
    db.close()


def main(keyword):
    try:
        scrapy_index(keyword)
        # drop_down()
        urls = parse_page()
        try:
            for url in list(urls):
                scrapy_detail(url)
                details = parse_detail()
                print(details)
        except StaleElementReferenceException as e:
            print(e)
        finally:
            browser.close()
    finally:
        # browser.quit()
        pass


if __name__ == '__main__':
    # 主方法 爬取csdn关于python的博客帖子，并将其存到数据库里
    # 多线程加快爬取速度
    # pool = multiprocessing.Pool()
    # pool.map(main, KEY_WORDS)
    # pool.close()
    # pool.join()
    main('java')





