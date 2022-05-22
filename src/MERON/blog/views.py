from django.shortcuts import render
from django.shortcuts import redirect

from libs.hantei import WhatDishName
from libs.scrape import DishMaked
from libs.SearchDB import Search

import requests
import io

# Create your views here.
def hello_world(request):
    return render(request, 'blog/helloWorld.html', {})

def index(request):
    return render(request,'blog/index.html',{"flag":0})

def result(request):
    try:
        img = request.FILES["dish_pic"]
    except:
        try:
            dish_name = request.POST["input_dish_name"]
        except:
            return render(request,'blog/index.html',{"flag":0})

        dishClass = DishMaked(dish_name)
        print("\n\n\n"+"入力値観たぞ"+dish_name+"\n\n\n\n")
        Kcal, Protein, Lipids, Carbohydrate = eiyouSum(dishClass)
        return render(request,'blog/index.html',{"result":dish_name, "dd_name":dish_name, "zairyos":dishClass.syokuzais, "flag":1, "eiyouso":{'Kcal':Kcal, 'Protein':Protein, 'Lipids':Lipids, 'Carbohydrate':Carbohydrate}})
    
    # URL用
    # img = io.BytesIO(requests.get('https://cdn.discordapp.com/attachments/872060747079897088/977489461346701332/test.jpeg').content)
    dish = WhatDishName(img)
    print("\n\n\n"+"AI使ったぞ"+dish.dish_name+"\n\n\n\n")
    dishClass = DishMaked(dish.dish_name)
    Kcal, Protein, Lipids, Carbohydrate = eiyouSum(dishClass)

    return render(request,'blog/index.html',{"result":dish.dish_name, "dd_name":dish.dish_name, "zairyos":dishClass.syokuzais, "flag":1, "eiyouso":{'Kcal':Kcal, 'Protein':Protein, 'Lipids':Lipids, 'Carbohydrate':Carbohydrate}})

def android(request):
    img = request.FILES['upload_file1']["name"]
    return

def eiyouSum(dishClass):
    Kcal = 0
    Protein = 0
    Lipids = 0
    Carbohydrate = 0
    for syokuzai in dishClass.syokuzais:
        # print("食材", syokuzai['zairyo'])
        K, P, L, C = Search(syokuzai['zairyo'])
        Kcal = K + Kcal
        Protein = P + Protein
        Lipids = L +Lipids
        Carbohydrate = C + Carbohydrate
    return round(Kcal, 1), round(Protein, 1), round(Lipids, 1), round(Carbohydrate, 1)