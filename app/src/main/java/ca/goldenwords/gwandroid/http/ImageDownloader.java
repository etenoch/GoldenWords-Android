package ca.goldenwords.gwandroid.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import de.greenrobot.event.EventBus;

public class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {

    private ImageView imageView;
    private String url;

    public ImageDownloader(ImageView view,String url){
        this.imageView = view;
        this.url = url;
    }

    @Override protected Bitmap doInBackground(Void... params) {
        if(url!=null && !url.isEmpty()){
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        ImageDownloadedEvent ide = new ImageDownloadedEvent(imageView,result,url,true);
        EventBus.getDefault().post(ide);
    }
}
