package ca.goldenwords.gwandroid.model;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
    public int nid;

    public ImageItem(Bitmap image, String title,int nid) {
        super();
        this.image = image;
        this.title = title;
        this.nid = nid;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString(){
        return title;
    }
}
