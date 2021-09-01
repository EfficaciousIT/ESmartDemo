package com.mobi.efficacious.ESmartDemo.entity;

public class Gallery {
    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    private String ImageName;
    private String ImageDescription;
    private String UplaoedFrom;
    private String ImageBase64;
    public String getImageDescription() {
        return ImageDescription;
    }

    public void setImageDescription(String imageDescription) {
        ImageDescription = imageDescription;
    }

    public String getUplaoedFrom() {
        return UplaoedFrom;
    }

    public void setUplaoedFrom(String uplaoedFrom) {
        UplaoedFrom = uplaoedFrom;
    }

    public String getImageBase64() {
        return ImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        ImageBase64 = imageBase64;
    }
}
