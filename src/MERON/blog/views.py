from django.shortcuts import render
from django.shortcuts import redirect

from libs.hantei import WhatDishName
from libs.scrape import DishMaked

# Create your views here.
def hello_world(request):
    return render(request, 'blog/helloWorld.html', {})

def index(request):
    return render(request,'blog/index.html',{"flag":0})

def result(request):
    try:
        img = request.FILES["dish_pic"]
        print(type(img))
        dish = WhatDishName(img)
        print("\n\n\n"+"AI使ったぞ"+dish.dish_name+"\n\n\n\n")
        dishClass = DishMaked(dish.dish_name)

        return render(request,'blog/index.html',{"result":dish.dish_name, "dd_name":dish.dish_name, "zairyos":dishClass.syokuzais, "flag":1})
    except:
        try:
            dish_name = request.POST["input_dish_name"]
            dishClass = DishMaked(dish_name)
            print("\n\n\n"+"入力値観たぞ"+dish_name+"\n\n\n\n")
            return render(request,'blog/index.html',{"result":dish_name, "dd_name":dish_name, "zairyos":dishClass.syokuzais, "flag":1})

        except:
            return render(request,'blog/index.html',{"flag":0})