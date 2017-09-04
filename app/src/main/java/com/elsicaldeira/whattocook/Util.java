package com.elsicaldeira.whattocook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Elsi on 23/08/2015.
 */
public class Util {
    public static String TITLE_TAG = "title";
    public static String PUBLISHER_TAG = "publisher";
    public static String PUBLISHER_URL_TAG = "publisher_url";
    public static String RECIPE_ID_TAG = "recipe_id";
    public static String F2F_URL_TAG = "f2f_url";
    public static String SOURCE_URL_TAG = "source_url";
    public static String IMAGE_URL_TAG = "image_url";
    public static String SOCIAL_RANK_TAG = "social_rank";
    public static String COUNT_TAG = "count";
    public static String RECIPES_TAG = "recipes";
    public static String RECIPE_TAG = "RECIPE";
    public static String INGREDIENTS_TAB_TAG = "INGREDIENTS";
    public static String DIRECTIONS_TAB_TAG = "DIRECTIONS";
    public static String INGREDIENTS_TAG = "ingredients";
    public static String FROM_FAV = "fromFavs";
    public static int ONE_COLUMN = 1;
    public static int TWO_COLUMN = 2;
    private static boolean mainListView = true;
    private static boolean favListView = true;

    private static Context appContext;

    public static Context getActContext() {
        return appContext;

    }

    public static void setActContext(Context context) {
        appContext = context;
    }

    public static void setMainListView(boolean listView) {
        mainListView = listView;
    }

    public static boolean getMainListView() {
        return mainListView;
    }

    public static void setFavListView(boolean listView) {
        favListView = listView;
    }

    public static boolean getFavListView() {
        return favListView;
    }

    /**
     * Verify Internet connection
     * @return
     */
    public static boolean checkInternetConnection(Context context) {
        // get Connectivity Manager object to check connection
       // ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};

        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
