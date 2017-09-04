package com.elsicaldeira.whattocook.database;

import android.provider.BaseColumns;

/**
 * Created by Elsi on 06/09/2015.
 */
public class RecipesFavDbSchema {
    public static final class RecipeFavTable {

        public void RecipesFavDbSchema() {}

        public static final class Cols {
            public static final String TABLE_NAME = "recipes";
            public static final String FAV_ID = BaseColumns._ID;
            public static final String RECIPE_ID = "recipe_id";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String PUBLISHER = "publisher";
            public static final String SOURCE = "source_url";
            public static final String IMAGE = "image_url";
            public static final String SOCIAL = "social_rank";
            public static final String PUBLISHER_URL = "publisher_url";
            public static final String F2F = "f2f_url";
        }
    }
}
