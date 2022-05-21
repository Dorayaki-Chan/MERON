
import pymysql.cursors
import statistics
import pykakasi

# .env ファイルをロードして環境変数へ反映
from dotenv import load_dotenv
load_dotenv()

# 環境変数を参照
import os
MYSQL_PASS = os.getenv('MYSQL_PASS')

def Search(FoodName):
    #Search(食材の名前)を行うとカロリー、タンパク質、脂質、炭水化物の順番で返す
    kks = pykakasi.kakasi()
    FoodName_kana = ''
    result = kks.convert(FoodName)
    for char in result:
        FoodName_kana += char['kana']
    Kcal_List, Protein_List, Lipids_List, Carbohydrate_List = [], [], [], []
    connection = pymysql.Connection(
        host = 'localhost',
        user = 'root',
        password = MYSQL_PASS,
        db = 'Hackathon',
        charset = 'utf8mb4',
        cursorclass=pymysql.cursors.DictCursor)#DictCursor = 辞書型でデータが帰ってくる
    
    try:
        with connection.cursor() as cursor:
            cursor = connection.cursor()
            # SQLの作成、定義
            sql = "select Kcal, Protein, Lipids, Carbohydrate\
                    from Food\
                    where FoodName_kana LIKE %s"
            para = ('%' + FoodName_kana + '%')
            cursor.execute(sql, (para))
            results = cursor.fetchall()
            for i, dict in enumerate(results):
                Kcal_List.append(dict['Kcal'])
                Protein_List.append(dict['Protein'])
                Lipids_List.append(dict['Lipids'])
                Carbohydrate_List.append(dict['Carbohydrate'])
            print("食材リスト", Kcal_List, Protein_List, Lipids_List, Carbohydrate_List)

            #if not Kcal_List:
            Kcal, Protein, Lipids, Carbohydrate = statistics.mean(Kcal_List), statistics.mean(Protein_List),statistics.mean(Lipids_List), statistics.mean(Carbohydrate_List)
    except Exception as e:
        print('error:', e)
        connection.rollback()
    finally:
        connection.close()
    print(Kcal, Protein, Lipids, Carbohydrate)
    return round(Kcal, 1), round(Protein, 1), round(Lipids), round(Carbohydrate, 1)

def Main():
    FoodName = input("料理名を入力:")
    Search(FoodName)

if __name__ == "__main__":
    Main()
