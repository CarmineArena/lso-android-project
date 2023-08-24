package com.example.history4fun;

import android.os.Build;

import java.util.Base64;

public class ImageInfo {
    private String imageName;
    private byte[] imageData;

    public ImageInfo(String imageName, String imageBase64Data) {
        this.imageName = imageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.imageData = Base64.getDecoder().decode(imageBase64Data);
        }
    }

    public String getImageName() { return imageName; }
    public byte[] getImageData() { return imageData; }
}
