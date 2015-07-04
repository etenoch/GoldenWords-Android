package ca.goldenwords.gwandroid.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GWUtils {

    private static Context context;

    public static boolean hasInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static Sections parseCategoryMachineName(String str){
        switch (str){
            case "news_of_the_world":
                return Sections.NEWS;
            case "stories":
                return Sections.RANDOM;
            case "news_in_pictures":
                return Sections.PICTURES;
            case "editorials":
                return Sections.EDITORIALS;
            case "videos":
                return Sections.VIDEOS;
            default:
                return Sections.NEWS;
        }
    }

    public static Sections parseCategoryShortname(String str){  // local machine name from xml
        switch (str){
            case "news":
                return Sections.NEWS;
            case "random":
                return Sections.RANDOM;
            case "pictures":
                return Sections.PICTURES;
            case "editorials":
                return Sections.EDITORIALS;
            case "videos":
                return Sections.VIDEOS;
            default:
                return Sections.NEWS;
        }
    }


    public static void setContext(Context context) {
        GWUtils.context = context;
    }
}
