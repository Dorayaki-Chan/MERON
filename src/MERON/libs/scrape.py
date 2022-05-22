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
        print(url_recipe)
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
            a = self.changeAmount(amount)
            print(type(a), a)
            dic = {"zairyo": self.delateKakko(zairyo), "amount":amount}
            arry.append(dic)
            i = i + 1
        
        return arry
    
    # TODO:量をgに変換する
    def changeAmount(self, amount_moto):
        amountNunm = amount_moto

        if '大さじ' in amount_moto:
            henkan = amount_moto.replace('大さじ', '')
            amountNunm =  self.changefloat(henkan) * 15
        if '小さじ' in amount_moto:
            henkan = amount_moto.replace('小さじ', '')
            amountNunm = self.changefloat(henkan) * 5
        if '少々' in amount_moto:
            amountNunm = 0
        if 'ひとつまみ' in amount_moto:
            amountNunm = 0.5
        if 'ml' in amount_moto:
            henkan = amount_moto.replace('ml', '')
            amountNunm =  self.changefloat(henkan)
        if '適量' in amount_moto:
            amountNunm = 0
        if 'g' in amount_moto:
            henkan = amount_moto.replace('g', '')
            amountNunm =  self.changefloat(henkan)
        if '個' in amount_moto:
            amountNunm =  100
        return amountNunm
    
    def changefloat(self, str):
        try:
            num = float(str)
        except ValueError:
            print(type(str), str)
            return str
        else:
            print(type(num), num)
            return num

    
    # TODO:かっこ削除
    def delateKakko(self, sorce):
        syokuzai = sorce
        if '(' in sorce:
            k_start = sorce.find('(')
            k_end = sorce.find(')')
            if k_start > 0:
                syokuzai = sorce[:k_start]
            else:
                syokuzai = sorce[k_end+1:]
        return syokuzai


if __name__ == '__main__':
    x = DishMaked('ステーキ')
    print(x.syokuzais)
    print(x.number_of_people)