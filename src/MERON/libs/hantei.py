from keras.models import load_model
from PIL import Image, ImageOps
import numpy as np


class WhatDishName:
    def __init__(self, img):
        model = load_model('./libs/model/keras_model.h5')

        data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)

        image = Image.open(img)

        size = (224, 224)
        image = ImageOps.fit(image, size, Image.ANTIALIAS)

        image_array = np.asarray(image)
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        data[0] = normalized_image_array

        prediction = model.predict(data)
        # print(prediction)

        max_val = max(prediction)

        # print(max_val)
        max_val = max(prediction[0])

        predict_index = list(prediction[0]).index(max_val)

        with open('./libs/model/labels.txt','r',encoding="utf-8") as tf:
            lines = tf.read().split()

        labels = []
        for i in range(len(lines)):
            if i%2 == 1:
                labels.append(lines[i]) 

        self.dish_name = labels[predict_index]