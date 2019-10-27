package com.elsicaldeira.whattocook;

import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ShareActionProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;


public class RecipeDetailActivity extends AppCompatActivity {
    String recipeId;
    String title;
    String sourceUrl;
    String source;
    String f2furl;
    String publisher;
    String publisherUrl;
    String socialRank;
    String imageUrl;
    boolean fromFavorites;
    boolean recipeIsFav;

    private String urlStr;
    private String apiKey;
    JSONArray ingredients;
    ArrayList<String> ingredientsList;
    ProgressBar progressDialog;
    ImageView recipeImg;
    TextView titleRecipe;
    FloatingActionButton favButton;
    Button tryagainBtn;
    SampleFragmentPagerAdapter adapter;
    private ShareActionProvider mShareActionProvider;
    private AsyncTask taskRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        tryagainBtn = (Button)findViewById(R.id.reconnectBtn);
        tryagainBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                searchRecipe(recipeId);
            }
        });
        Bundle extras= getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getString(Util.RECIPE_ID_TAG);
            title = extras.getString(Util.TITLE_TAG);
            sourceUrl = extras.getString(Util.SOURCE_URL_TAG);
            imageUrl = extras.getString(Util.IMAGE_URL_TAG);
            fromFavorites = extras.getBoolean(Util.FROM_FAV);
        }
        if (!fromFavorites) {
            FavoritesUtils favoritesRecipes = FavoritesUtils.get(this);
            recipeIsFav = favoritesRecipes.verifyRecipe(recipeId);
        } else {
            recipeIsFav = true;
        }

        urlStr = getResources().getString(R.string.api_url_get);
        apiKey = getResources().getString(R.string.api_key);
        searchRecipe(recipeId);
        recipeImg = (ImageView) findViewById(R.id.imageView);

        Glide.with(this)
                .load(imageUrl)
                .apply(centerCropTransform()
                        .placeholder(R.drawable.placeholder_loading)
                        .error(R.drawable.placeholder_error)
                        .priority(Priority.HIGH))
                .into(recipeImg);

        titleRecipe = (TextView) findViewById(R.id.recipeTitle);
        favButton = (FloatingActionButton) findViewById(R.id.favBtn);
        if(recipeIsFav){
            favButton.setImageResource(R.drawable.ic_favorite_selected);
        }
        favButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.i(Util.RECIPE_TAG,"click en favorite");
                if(!recipeIsFav) {
                    addFavorite();
                } else {
                    removeFavorite();
                }
            }
        });
        progressDialog = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        titleRecipe.setText(title);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(Util.INGREDIENTS_TAB_TAG));
        tabLayout.addTab(tabLayout.newTab().setText(Util.DIRECTIONS_TAB_TAG));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new SampleFragmentPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),recipeId,sourceUrl){
            @Override
            // To update fragment in ViewPager, we should override getItemPosition() method,
            // in this method, we call the fragment's public updating method.
            public int getItemPosition(Object object) {
                Log.d("RECIPE", "getItemPosition(" + object.getClass().getSimpleName() + ")");
                if (object instanceof IngredientsTab) {
                    ((IngredientsTab) object).updateList(ingredientsList);
                }

                return super.getItemPosition(object);
            }

        };
        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // based on the current position you can then cast the page to the correct
                // class and call the method:
                if (viewPager.getCurrentItem() == 1) {
                    DirectionsTab dirTab = (DirectionsTab)adapter.getCurrentFragment();
                    dirTab.loadDirections();
                }
                Log.i(Util.RECIPE_TAG, "on tab selected");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(Util.RECIPE_TAG, "on tab unselected");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(Util.RECIPE_TAG, "on tab reselected");

            }
        });
    }

    /**
     * searhRecipes
     * Invoke task to connect to get results
     * @param recipeId
     */
    private void searchRecipe(String recipeId){
        if ( Util.checkInternetConnection(getBaseContext())) {
            String connectionStr = urlStr + "?key=" + apiKey + "&rId=" + recipeId;;
            Log.i(Util.RECIPE_TAG, connectionStr);
            taskRecipe = new HttpAsyncTask()
            {
                @Override
                protected void onPreExecute() {
                    //progressDialog.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(String result)
                {
                    // do whatever you want with result
                    if (!result.isEmpty()) {
                        readJSONDetail(result);
                        progressDialog.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        mShareActionProvider.setShareIntent(createShareIntent());
                    }
                }
            }.execute(connectionStr);
        }  else {
            tryagainBtn.setText(getResources().getString(R.string.no_internet));
            tryagainBtn.setVisibility(View.VISIBLE);
            //progressDialog.setVisibility(View.GONE);
            Log.i(Util.RECIPE_TAG, "no connetion");
        }
    }

    /**
     * readJSON
     * Process string received
     * @param cadena
     */
    private void readJSONDetail(String cadena){
        Log.i(Util.RECIPE_TAG, "leyendo JSON");
        try {
            JSONObject json = new JSONObject(cadena);
            JSONObject recipeObj = json.getJSONObject("recipe");
            sourceUrl = recipeObj.getString(Util.SOURCE_URL_TAG);
            imageUrl = recipeObj.getString(Util.IMAGE_URL_TAG);

            title = Util.fromHtml(recipeObj.getString(Util.TITLE_TAG)).toString();
            publisher = recipeObj.getString(Util.PUBLISHER_TAG);
            publisherUrl = recipeObj.getString(Util.PUBLISHER_URL_TAG);
            f2furl = recipeObj.getString(Util.F2F_URL_TAG);

            sourceUrl = recipeObj.getString(Util.SOURCE_URL_TAG);
            double social = recipeObj.getDouble(Util.SOCIAL_RANK_TAG);
            socialRank = new DecimalFormat("#.##").format(social);

            titleRecipe.setText(title);
            Log.i(Util.RECIPE_TAG,sourceUrl);
            ingredients = recipeObj.getJSONArray(Util.INGREDIENTS_TAG);
            ingredientsList = new ArrayList<String>();
            for (int i=0;i < ingredients.length();i++) {
                ingredientsList.add(ingredients.get(i).toString());
                Log.i(Util.RECIPE_TAG,ingredients.get(i).toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFavorites(){
        Intent intent;
        intent = new Intent(getBaseContext(),FavoritesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_detail, menu);

        // Get the menu item.
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        // Set share Intent.
        // Note: You can set the share Intent afterwords if you don't want to set it right now.

        return true;
    }

    // Create and return the Share Intent
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String header= getResources().getString(R.string.share_title);
        String title= titleRecipe.getText() + "\n\n";
        String ingredientes = getResources().getString(R.string.share_ingredients);

        for (int i=0;i<ingredientsList.size();i++) {
            ingredientes = ingredientes + ingredientsList.get(i) +"\n";
        }
        String shareText = header + title +ingredientes + "\n\n" + getResources().getString(R.string.share_source) + sourceUrl + "\n\n" + getResources().getString(R.string.share_app);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return shareIntent;
    }

    // Sets new share Intent.
    // Use this method to change or set Share Intent in your Activity Lifecycle.
    private void changeShareIntent(Intent shareIntent) {
        mShareActionProvider.setShareIntent(shareIntent);
    }

    private void addFavorite() {
        FavoritesUtils favoritesRecipes = FavoritesUtils.get(Util.getActContext());
        boolean result = favoritesRecipes.saveRecipe(title,publisher,f2furl,sourceUrl,recipeId,imageUrl,socialRank,publisherUrl);
        if (result) {
            recipeIsFav = true;
            favButton.setImageResource(R.drawable.ic_favorite_selected);
            Snackbar
                    .make(getCurrentFocus(), R.string.recipe_added, Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            Snackbar
                    .make(getCurrentFocus(), R.string.recipe_exist, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void removeFavorite(){
        FavoritesUtils favoritesRecipes = FavoritesUtils.get(Util.getActContext());
        boolean result = favoritesRecipes.deleteRecipe(recipeId);
        if (result) {
            recipeIsFav = false;
            favButton.setImageResource(R.drawable.ic_favorite);
            Snackbar
                    .make(getCurrentFocus(), R.string.recipe_removed, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.action_favorites:
                loadFavorites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
