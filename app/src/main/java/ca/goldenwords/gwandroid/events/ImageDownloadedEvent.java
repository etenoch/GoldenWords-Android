package ca.goldenwords.gwandroid.events;

import android.graphics.Bitmap;
import android.widget.ImageView;

import ca.goldenwords.gwandroid.model.Node;

public class ImageDownloadedEvent {
    private ImageView imageView;
    private Bitmap image;

    public ImageDownloadedEvent(ImageView imageView, Bitmap image) {
        this.imageView = imageView;
        this.image = image;
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
}
