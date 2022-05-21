from django.shortcuts import render
from django.shortcuts import redirect

# Create your views here.
def hello_world(request):
    return render(request, 'blog/helloWorld.html', {})

def index(request):
    return render(request,'blog/index.html',{})

def result(request):
    strT = request.POST["dish"]
    # TODO: 料理名から食材を検索(スクレイピング)

    # TODO: 食材名から栄養素を検索(データベース)

    return render(request,'blog/result.html',{"result":strT})