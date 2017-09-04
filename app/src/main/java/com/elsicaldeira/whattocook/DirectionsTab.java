package com.elsicaldeira.whattocook;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Elsi on 19/08/2015.
 */
public class DirectionsTab extends Fragment {
    public static final String ARG_PAGE = "DIR";

    private String sourceUrl;
    public boolean loadingFinished = false;
    public boolean inLoadingProcess = false;
    WebView webPage;
    ProgressBar mProgressBar;

    public static DirectionsTab newInstance(String sourceUrl) {
        Log.i(Util.RECIPE_TAG, "NEWINstance " + sourceUrl);
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, sourceUrl);
        DirectionsTab fragment = new DirectionsTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourceUrl = getArguments().getString(ARG_PAGE);
        Log.i("RECIPE", "dircreated " + sourceUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.directions_tab, container, false);
        mProgressBar = (ProgressBar)view.findViewById(R.id.recipe_page_progress_bar);
        webPage = (WebView) view.findViewById(R.id.webPage);
        // Configure related browser settings
        webPage.getSettings().setLoadsImagesAutomatically(true);
       // webPage.getSettings().setJavaScriptEnabled(true);
        webPage.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        // Enable responsive layout
        webPage.getSettings().setUseWideViewPort(true);
// Zoom out if the content width is greater than the width of the veiwport
        webPage.getSettings().setLoadWithOverviewMode(true);

        return view;
    }

    public void cancelLoad() {
        if (!loadingFinished) {
            if (!inLoadingProcess) {
                webPage.loadUrl("about:blank");
            }
        }
    }

    public void loadDirections() {
        if (!loadingFinished) {
            mProgressBar.setMax(100); // WebChromeClient reports in range 0-100
            //webPage.getSettings().setJavaScriptEnabled(true);
            Log.i(Util.RECIPE_TAG, "info: " + webPage.getProgress());
            webPage.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView webView, int newProgress) {
                    if (newProgress == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    }
                }

            /*public void onReceivedTitle(WebView webView, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }*/
            });

            webPage.setWebViewClient(new WebViewClient() {
                /*public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }*/

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    super.onPageStarted(view, url, facIcon);
                    loadingFinished = false;
                    inLoadingProcess = true;
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    inLoadingProcess = false;
                    loadingFinished = true;
                }
            });
            if (!inLoadingProcess) {
                webPage.loadUrl(sourceUrl);
            }
            Log.i(Util.RECIPE_TAG, "web page" + sourceUrl);

        }
    }
}
