package com.mobi.efficacious.ESmartDemo.entity;

public class dialog_box {
    private String text;
    private int image;
public dialog_box()
{

}

    public dialog_box(String text, int image)
    {
        super();
        this.text = text;
        this.image = image;

    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

