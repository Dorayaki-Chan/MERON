# MERON【Calorie Calculator】

料理の画像から料理名をAIが考え、その料理名をもとに栄養素・カロリーを算出するアプリ。

## 階層

src
 ├ MERON(バックエンド)
 ├ takephotoAPP(Androidアプリ)
 └ MakeDB.py(DBテーブルの作成)


## メモ
### 参考
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

## git関係
極力にmasterブランチで作業しないこと。
masterへマージする際はプルリクエスト作ってください。
ブランチはいくつでもテキトーに作っていいです。

リモートリポジトリの最新コードをローカルに反映
```cmd
git pull
```

ブランチの作成
```cmd
git branch (ブランチ名)
例) git branch mydevelop
```

ブランチの移動
```cmd
git checkout (ブランチ名)
例) git checkout mydevelop
```

```
git add *(若しくは * の代わりにファイル名)
git commit -m "コメント"
git push
```
