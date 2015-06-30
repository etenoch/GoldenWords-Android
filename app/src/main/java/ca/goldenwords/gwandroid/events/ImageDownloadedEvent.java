package ca.goldenwords.gwandroid.events;

import android.graphics.Bitmap;
import android.widget.ImageView;

import ca.goldenwords.gwandroid.data.DataSource;
import ca.goldenwords.gwandroid.model.Node;

public class ImageDownloadedEvent {
    private ImageView imageView;
    private Bitmap image;
    private String url;

    public ImageDownloadedEvent(ImageView imageView, Bitmap image,String url,boolean addToCache) {
        this(imageView,image,url);
        if(addToCache) DataSource.addToCache(this);
    }

    public ImageDownloadedEvent(ImageView imageView, Bitmap image,String url) {
        this.imageView = imageView;
        this.image = image;
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
