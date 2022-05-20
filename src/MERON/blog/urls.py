from django.urls import path
from . import views

urlpatterns = [
    path('test', views.hello_world, name='helloWorld'),
    path('',views.index,name='index'),
    path('result',views.result,name='result'),
]