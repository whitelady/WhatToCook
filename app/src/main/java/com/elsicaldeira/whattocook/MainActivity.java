package com.elsicaldeira.whattocook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.GridLayoutManager;

import android.widget.Button;
import android.widget.ProgressBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String urlStr;
    private String apiKey;
    String recipeId;
    String publisher;
    String sourceUrl;
    String imageUrl;
    private String list = "";
    String title;
    JSONArray recipes = null;
    private Integer totalRecipes;
    private boolean isListView;

    private ArrayList<Recipe> recipesList;
    ProgressBar progressDialog;
    TextView resultsTxt;
    Button tryagainBtn;
    static View.OnClickListener myOnClickListener;
    private RecyclerView.Adapter adapter;
    private RecyclerView recipeListView;
    //private static StaggeredGridLayoutManager mStaggeredLayoutManager;
    private static StaggeredGridLayoutManager mStaggeredLayoutManager;
    private Menu menu;
    AsyncTask taskRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isListView = Util.getMainListView();
        myOnClickListener = new MyOnClickListener(this);
        recipeListView = (RecyclerView)findViewById(R.id.recipeList);
        progressDialog =(ProgressBar)findViewById(R.id.pbHeaderProgress);
        resultsTxt = (TextView)findViewById(R.id.resultsTxt);
        tryagainBtn = (Button)findViewById(R.id.reconnectBtn);
        tryagainBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                searchRecipes(list);
            }
        });
        recipeListView.setHasFixedSize(true);
        recipeListView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(this);

       /* if (isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(Util.ONE_COLUMN, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(Util.TWO_COLUMN, StaggeredGridLayoutManager.VERTICAL);
        }*/
        recipeListView.setLayoutManager(llm);
        urlStr = getResources().getString(R.string.api_url_search);
        apiKey = getResources().getString(R.string.api_key);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        list = sharedPref.getString(getString(R.string.last_search), "");
        Log.i(Util.RECIPE_TAG,"contenido list " + list);
        Log.i(Util.RECIPE_TAG,"onCreate");
        Util.setActContext(this.getApplicationContext());
        searchRecipes(list);
        handleIntent(getIntent());
    }

    /**
     * MyOnClickListener
     *  Listener for the RecyclerView
     *
     */

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
           seeRecipe(v);
        }

        /**
         * seeRecipe
         * get recipe details to pass to the activity
         * @param v view
         */

        private void seeRecipe(View v) {
            int selectedItemPosition = recipeListView.getChildAdapterPosition(v);
            imageUrl = recipesList.get(selectedItemPosition).getImageurl();
            Log.i(Util.RECIPE_TAG,"imageUrl " + imageUrl);
            RecyclerView.ViewHolder viewHolder
                    = recipeListView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView titleView = (TextView) viewHolder.itemView.findViewById(R.id.recipeTitle);
            recipeId = recipesList.get(selectedItemPosition).getRecipeId();
            title = (String) titleView.getText();
            sourceUrl = recipesList.get(selectedItemPosition).getSourceurl();
            Context context = getApplicationContext();
            goActivity(context);
            Log.i("RECIPE",recipeId);

        }

    }

    /**
     * goActivity
     * Execute activity of recipe selected
     * @param context app context
     */

    private void goActivity(Context context) {
        Intent intent;
        intent = new Intent(context,RecipeDetailActivity.class);
        intent.putExtra(Util.RECIPE_ID_TAG,recipeId);
        intent.putExtra(Util.TITLE_TAG,title);
        intent.putExtra(Util.IMAGE_URL_TAG,imageUrl);
        intent.putExtra(Util.SOURCE_URL_TAG,sourceUrl);
        intent.putExtra(Util.FROM_FAV,false);
        startActivity(intent);
    }

    /**
     * searhRecipes
     * Invoke task to connect to get results
     * @param list recipe list
     */
    private void searchRecipes(String list){
        if (Util.checkInternetConnection(getBaseContext())) {
            if (list.equalsIgnoreCase("")) {
                resultsTxt.setText(getResources().getString(R.string.results) + " " + list);
                String connectionStr = urlStr + "?key=" + apiKey;
                Log.i(Util.RECIPE_TAG, connectionStr);

                taskRecipe = new HttpAsyncTask() {
                    @Override
                    protected void onPreExecute() {
                        progressDialog.setVisibility(View.VISIBLE);
                        tryagainBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPostExecute(String result) {
                        // do whatever you want with result
                        if (!result.isEmpty()) {
                            readJSON(result);
                            progressDialog.setVisibility(View.GONE);
                            isListView = Util.getMainListView();
                            Log.i(Util.RECIPE_TAG, "bind completed search " + isListView);
                            adapter = new RecipeViewAdapter(recipesList);
                            recipeListView.setAdapter(adapter);
                            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                            /*if (isListView) {
                                mStaggeredLayoutManager.setSpanCount(Util.ONE_COLUMN);
                            } else {
                                mStaggeredLayoutManager.setSpanCount(Util.TWO_COLUMN);
                            }*/

                            Log.i(Util.RECIPE_TAG, "bind completed");
                        } else {
                            progressDialog.setVisibility(View.GONE);
                            tryagainBtn.setText(getResources().getString(R.string.error_feed));
                            tryagainBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }.execute(connectionStr);
            } else {
               // list = (list);
                resultsTxt.setText(getResources().getString(R.string.results_search) + " " + list);
                String connectionStr = Uri.parse(urlStr + "?key=" + apiKey)
                        .buildUpon()
                        .appendQueryParameter("q", list)
                        .build().toString();
                taskRecipe = new HttpAsyncTask() {
                    @Override
                    protected void onPreExecute() {
                        progressDialog.setVisibility(View.VISIBLE);
                        tryagainBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPostExecute(String result) {
                        // do whatever you want with result
                        if (!result.isEmpty()) {
                            readJSON(result);
                            progressDialog.setVisibility(View.GONE);
                            adapter = new RecipeViewAdapter(recipesList);
                            isListView = Util.getMainListView();
                            recipeListView.setAdapter(adapter);
                            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                            Log.i(Util.RECIPE_TAG, "bind completed" + isListView);
                           /* if (isListView) {
                                mStaggeredLayoutManager.setSpanCount(Util.ONE_COLUMN);
                            } else {
                                mStaggeredLayoutManager.setSpanCount(Util.TWO_COLUMN);
                            }*/

                            if (recipesList.size() == 0) {
                                resultsTxt.setText(getResources().getString(R.string.no_results));
                            }

                            Log.i(Util.RECIPE_TAG, "bind completed");
                        } else {
                            progressDialog.setVisibility(View.GONE);
                            tryagainBtn.setText(getResources().getString(R.string.error_feed));
                            tryagainBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }.execute(connectionStr);
            }
        } else {
            progressDialog.setVisibility(View.GONE);
            resultsTxt.setText("");
            tryagainBtn.setText(getResources().getString(R.string.no_internet));
            tryagainBtn.setVisibility(View.VISIBLE);
            Log.i(Util.RECIPE_TAG,"no connetion");
        }
    }


    /**
     * readJSON
     * Process string received
     * @param cadena JSON string
     */
    private void readJSON(String cadena){
        Log.i(Util.RECIPE_TAG, "leyendo JSON");
        try {
            JSONObject json = new JSONObject(cadena);
            totalRecipes = Integer.parseInt(json.getString(Util.COUNT_TAG));
            recipes = json.getJSONArray(Util.RECIPES_TAG);
            recipesList = new ArrayList<>();

            // looping through All recipes

            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeObj = recipes.getJSONObject(i);

                String title = Util.fromHtml(recipeObj.getString(Util.TITLE_TAG)).toString();
                String publisher = recipeObj.getString(Util.PUBLISHER_TAG);
                String publisherUrl = recipeObj.getString(Util.PUBLISHER_URL_TAG);
                String f2fUrl = recipeObj.getString(Util.F2F_URL_TAG);
                String imageUrl = recipeObj.getString(Util.IMAGE_URL_TAG);
                String recipeId = recipeObj.getString(Util.RECIPE_ID_TAG);
                String sourceUrl = recipeObj.getString(Util.SOURCE_URL_TAG);
                double social = recipeObj.getDouble(Util.SOCIAL_RANK_TAG);
                String socialRank = new DecimalFormat("#.##").format(social);
                recipesList.add(new Recipe(title, publisher, f2fUrl, sourceUrl, recipeId, imageUrl, socialRank, publisherUrl, null));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle search
     * @param intent intent to open
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            list = intent.getStringExtra(SearchManager.QUERY);
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.last_search), list);
            editor.apply();
            //use the query to search your data somehow
            searchRecipes(list);

        }
    }

    /**
     * loadPopular Reload popular recipes clearing variable
     */

    private void loadPopular() {
        list = "";
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.last_search), list);
        editor.apply();
        searchRecipes(list);
    }

    private void loadFavorites(){
        Intent intent;
        intent = new Intent(getBaseContext(),FavoritesActivity.class);
        startActivity(intent);
    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(Util.TWO_COLUMN);
            item.setIcon(R.drawable.ic_list_color);
            item.setTitle(R.string.action_list);
            Util.setMainListView(false);
        } else {
            mStaggeredLayoutManager.setSpanCount(Util.ONE_COLUMN);
            item.setIcon(R.drawable.ic_grid_color);
            item.setTitle(R.string.action_grid);
            Util.setMainListView(true);
        }
        isListView = Util.getMainListView();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(Util.RECIPE_TAG,"finishing: "  + this.isFinishing());
        if (this.isFinishing()) {
            Log.i(Util.RECIPE_TAG,"finishing");
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.last_search));
            editor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Util.RECIPE_TAG,"finishing: "  + this.isFinishing());
        if (this.isFinishing()) {
            Log.i(Util.RECIPE_TAG,"finishing");
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.last_search));
            editor.apply();
        }
    }


    ///Menu y opciones

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        //toggle();
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_reload:
                loadPopular();
                return true;
            case R.id.action_favorites:
                loadFavorites();
                return true;
            case R.id.action_toggle:
                toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
