package com.elsicaldeira.whattocook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.elsicaldeira.whattocook.database.RecipesFavDbSchema;
import com.elsicaldeira.whattocook.database.FavRecipesCursor;
import com.elsicaldeira.whattocook.database.RecipeFavHelper;

import java.util.ArrayList;

/**
 * Created by Elsi on 13/09/2015.
 */
public class FavoritesUtils {
    private static FavoritesUtils favRecipe;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FavoritesUtils get(Context context) {
        if (favRecipe == null) {
            favRecipe = new FavoritesUtils(context);
        }
        return favRecipe;
    }

    private FavoritesUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RecipeFavHelper(mContext)
                .getWritableDatabase();
        Log.i(Util.RECIPE_TAG,"database");
    }

    private FavRecipesCursor queryRecipes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipesFavDbSchema.RecipeFavTable.Cols.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new FavRecipesCursor(cursor);
    }

    public boolean saveRecipe(String title, String publisher, String f2furl, String sourceurl, String recipeId,String imageurl,String socialRank,String publisherurl){
        //String columns[] = new String[]{RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID};
        String selection = RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID + " = ? ";//WHERE author = ?
        String selectionArgs[] = new String[]{recipeId};

        FavRecipesCursor cursor = queryRecipes(selection, selectionArgs);

        if (cursor.getCount() == 0) {
            //Nuestro contenedor de valores
            ContentValues values = new ContentValues();

            //Seteando datos receta
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.TITLE, title);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.PUBLISHER, publisher);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.PUBLISHER_URL, publisherurl);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.IMAGE, imageurl);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID, recipeId);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.SOURCE, sourceurl);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.F2F, f2furl);
            values.put(RecipesFavDbSchema.RecipeFavTable.Cols.SOCIAL, socialRank);

            //Insertando en la base de datos
            mDatabase.insert(RecipesFavDbSchema.RecipeFavTable.Cols.TABLE_NAME, null, values);
            Log.i(Util.RECIPE_TAG, "guardado en BD");
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteRecipe(String recipeid){
        String selection = RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID + " = ?";
        String[] selectionArgs = { recipeid };

        mDatabase.delete(RecipesFavDbSchema.RecipeFavTable.Cols.TABLE_NAME, selection, selectionArgs);
        return true;
    }


    public boolean verifyRecipe(String recipeId) {
        String selection = RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID + " = ? ";//WHERE author = ?
        String selectionArgs[] = new String[]{recipeId};
        FavRecipesCursor cursor = queryRecipes(selection, selectionArgs);
        if (cursor.getCount() > 0) {
            Log.i(Util.RECIPE_TAG,"existe");
            return true;
        } else {
            Log.i(Util.RECIPE_TAG,"no existe");
            return false;
        }
    }


    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        FavRecipesCursor cursor = queryRecipes(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recipes.add(cursor.getRecipe());
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(Util.RECIPE_TAG, "getRecipes");

        return recipes;
    }

}
