package com.elsicaldeira.whattocook.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.elsicaldeira.whattocook.Recipe;
import com.elsicaldeira.whattocook.database.RecipesFavDbSchema;

/**
 * Created by Elsi on 12/09/2015.
 */
public class FavRecipesCursor extends CursorWrapper {
    public FavRecipesCursor(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        int favId = getInt(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.FAV_ID));
        String title = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.TITLE));
        String recipeId = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.RECIPE_ID));
        String publisher = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.PUBLISHER));
        String publisherUrl = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.PUBLISHER_URL));
        String source = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.SOURCE));
        String imageUrl = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.IMAGE));
        String f2fUrl = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.F2F));
        String socialRank = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.SOCIAL));
        String dateSaved = getString(getColumnIndex(RecipesFavDbSchema.RecipeFavTable.Cols.DATE));

        Recipe recipe = new Recipe(title, publisher, f2fUrl, source, recipeId, imageUrl, socialRank, publisherUrl, null);
        recipe.setFavId(favId);
        recipe.setDate(dateSaved);

        return recipe;
    }

}
