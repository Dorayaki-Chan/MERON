package com.example.C_fruit_image_upload;

public interface HttpPostListener {
    abstract public void postCompletion(byte[] response);
    abstract public void postFialure();
}