from django.shortcuts import render
from django.shortcuts import redirect

# Create your views here.
def hello_world(request):
    return render(request, 'blog/helloWorld.html', {})

def index(request):
    return render(request,'blog/index.html',{})

def result(request):
    strT = request.POST["dish"]
    return render(request,'blog/result.html',{"result":strT})