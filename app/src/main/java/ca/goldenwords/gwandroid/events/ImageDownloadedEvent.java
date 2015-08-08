package ca.goldenwords.gwandroid.events;

import android.graphics.Bitmap;
import android.widget.ImageView;

import ca.goldenwords.gwandroid.data.DataCache;

public class ImageDownloadedEvent {
    private ImageView imageView;
    private Bitmap image;
    private String url;

    public ImageDownloadedEvent(ImageView imageView, Bitmap image,String url,boolean addToCache) {
        this(imageView,image,url);
        if(addToCache) DataCache.addToCache(this);
    }

    public ImageDownloadedEvent(ImageView imageView, Bitmap image,String url) {
        this.imageView = imageView;
        setImage(image);
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        /*
        final int maxSize = 850;
        int outWidth;
        int outHeight;
        int inWidth = image.getWidth();
        int inHeight = image.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        this.image = Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
        */
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
