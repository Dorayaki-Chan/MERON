# メモ
## 参考
[Django Girls](https://tutorial.djangogirls.org/ja/)

## 初めてpullしたとき
居場所を変更
```cmd
cd .\src\
```

仮想環境を作る

```cmd
python -m venv (仮想環境名)

例) python -m venv kasoEnv
```

仮想環境を起動

```cmd
myvenv\Scripts\activate
```

pipを最新版に
```cmd
python -m pip install --upgrade pip
```

requirements.txtで指定されたpythonのライブラリを仮想環境下にぶち込む
```cmd
pip install -r requirements.txt
```


## 何回かつかう
```cmd
cd .\src\
```

仮想環境の立ち上げ
```cmd
kasoEnv\Scripts\activate
```

仮想環境の終了
```cmd
deactivate
```

サイトの立ち上げ
```cmd
cd .\MERON\

python manage.py runserver
```
