from bs4 import BeautifulSoup
from urllib import request

import urllib.parse

class DishMaked:
    def __init__(self, dish_name):
        s_quote = urllib.parse.quote(dish_name)

        url_search = 'https://www.kurashiru.com/search?query=' + s_quote
        
        response = request.urlopen(url_search)
        soup_search = BeautifulSoup(response, 'html.parser')
        response.close()

        link = soup_search.find('a', class_='DlyLink thumbnail-wrapper dly-video-item-thumbnail-root small')
        url_recipe = 'https://www.kurashiru.com' + link.get('href')
        response_zairyo = request.urlopen(url_recipe)
        soup_zairyo = BeautifulSoup(response_zairyo, 'html.parser')
        response_zairyo.close()

        self.number_of_people = soup_zairyo.find('span', class_='servings').get_text().replace('(', '').replace(')', '').replace('人前', '')

        zairyos = soup_zairyo.findAll('a', class_='DlyLink ingredient-name')

        amounts = soup_zairyo.findAll('span', class_='ingredient-quantity-amount')

        self.syokuzais = self.createDic(zairyos, amounts)
    

    def createDic(self, zairyos, amounts):
        arry = []
        i  = 0
        for zairyo  in zairyos:
            zairyo = zairyos[i].get_text().replace('\n', '').replace(' ', '')
            amount = amounts[i].get_text().replace('\n', '').replace(' ', '')
            dic = {"zairyo": zairyo, "amount":amount}
            arry.append(dic)
            i = i + 1
        
        return arry
    
    # TODO:量をgに変換する
    def changeAmount(self, amount_moto):
        if '大さじ' in amount_moto:
            amountNunm = amount_moto.replace('大さじ') * 15 
        if '子さじ' in amount_moto:
            amountNunm = amount_moto.replace('小さじ') * 5
        pass
    
    # TODO:かっこ削除
    def delateKakko(self, sorce):
        pass


if __name__ == '__main__':
    x = DishMaked('ステーキ')
    print(x.syokuzais)
    print(x.number_of_people)