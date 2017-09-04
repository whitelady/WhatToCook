package com.elsicaldeira.whattocook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.elsicaldeira.whattocook.database.RecipesFavDbSchema.RecipeFavTable;

/**
 * Created by Elsi on 06/09/2015.
 */
public class RecipeFavHelper extends SQLiteOpenHelper {
    private static final String TAG = "RecipeFavHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recipeFavBase.db";

    public RecipeFavHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + RecipeFavTable.Cols.TABLE_NAME + "(" +
                        RecipeFavTable.Cols.FAV_ID + " INTEGER primary key autoincrement," +
                        RecipeFavTable.Cols.RECIPE_ID + ", " +
                        RecipeFavTable.Cols.TITLE + ", " +
                        RecipeFavTable.Cols.F2F + ", " +
                        RecipeFavTable.Cols.PUBLISHER + ", " +
                        RecipeFavTable.Cols.PUBLISHER_URL + ", " +
                        RecipeFavTable.Cols.IMAGE + ", " +
                        RecipeFavTable.Cols.SOCIAL + ", " +
                        RecipeFavTable.Cols.SOURCE + ", " +
                        RecipeFavTable.Cols.DATE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
