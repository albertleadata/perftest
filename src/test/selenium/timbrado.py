#!/usr/bin/python3
import datetime
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options

def nowTS():
	date_time = datetime.datetime(2022, 6, 3, 12, 0, 50)
	return(time.mktime(date_time.timetuple()))


def testTxnAlpha():
	driver.get("https://timbrado:8443/timbrado/portal")
#	title = driver.title
#	assert title == "Web form"
	driver.implicitly_wait(0.5)
#	text_box = driver.find_element(by=By.NAME, value="my-text")
#	submit_button = driver.find_element(by=By.CSS_SELECTOR, value="button")
#	text_box.send_keys("Selenium")
#	submit_button.click()
#	msg = driver.find_element(by=By.ID, value="message")
#	value = msg.text
#	assert value == "Received!"

if __name__ == '__main__':
	chromeopts = Options()
	chromeopts.add_argument("--disable-extensions")
	chromeopts.add_argument("--disable-gpu")
	chromeopts.add_argument("--no-sandbox")
	chromeopts.add_argument("--headless")
	driver = webdriver.Chrome(options=chromeopts)
	tStart  = datetime.datetime.now().timestamp()
	bDone = False
	while not bDone:
		testTxnAlpha()
		time.sleep(1.0)
		tNow = datetime.datetime.now().timestamp()
		if tNow - tStart > 120.0:
			bDone = True
	driver.quit()
