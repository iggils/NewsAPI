package com.example.android.newsapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import  java.util.Collection;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsArticle>> {
    public static final String API_REQUEST_URL = "http://content.guardianapis.com/search?";
    public static final String LOG_TAG = NewsArticle.class.getName();
    //http://content.guardianapis.com/search?from-date=2018-09-01&to-date=2018-11-12&q=japan&api-key=73666014-ee3d-468e-8dea-4b25dd05d1f5&show-tags=contributor&page-size=10
    private final static String SHOW_TAGS = "show-tags";
    private final static String ShowTags = "contributor";

    private final static String API_KEY = "api-key";
    private final static  String ApiKey = "73666014-ee3d-468e-8dea-4b25dd05d1f5";

    private final static String FROM_DATE = "from-date";
    private final static String FromDate="2018-09-01";

    private final static String TO_DATE="to-date";
    private final static String ToDate = "2018-11-12";

    private final static String Q ="politics";

    private final static String PAGE_SIZE = "page-size";
    private final static String PageSize = "10";

    private NewsAdapter mArticle ;
    private static final  int  NEWS_LOADER_ID = 1;
    private TextView no_news_message;

    ListView newsListView;
    RelativeLayout emptyLayout;
    RelativeLayout noNewsLayout;
    RelativeLayout noNetworkLayout;
    RelativeLayout loadingLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       ListView newsArticleListView = findViewById(R.id.news_list);
        no_news_message = findViewById(R.id.no_news_message);

       mArticle = new NewsAdapter(this, new ArrayList<NewsArticle>());
       newsArticleListView.setAdapter(mArticle);

       newsArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               NewsArticle currentNews = mArticle.getItem(position);
               Uri newsUri = Uri.parse(currentNews.getArticleURL());
               Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
               startActivity(websiteIntent);
           }
       });
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(NetInformation(netInfo)){
            LoaderManager loadMgr = getLoaderManager();
            loadMgr.initLoader(0, null,this );
        }
        else {
            View loadingInd = findViewById(R.id.news_loading_progress);
            loadingInd.setVisibility(View.GONE);
            no_news_message.setText(R.string.no_news);
        }

    }

    public Boolean NetInformation(NetworkInfo netInfo)
    {
        boolean net = false;
        if(netInfo != null && netInfo.isConnected()) {
            net = true;
        }

        return  net;
    }

    private void initView() {
        newsListView = findViewById(R.id.news_list);
        emptyLayout = findViewById(R.id.empty_layout);
        noNewsLayout = findViewById(R.id.news_no_news_layout);
        noNetworkLayout = findViewById(R.id.news_no_network_layout);
        loadingLayout = findViewById(R.id.news_loading_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this   );

        String country = shared.getString(getString(R.string.settings_country), getString(R.string.settings_country_default));

        String newsType = shared.getString(getString(R.string.settings_news_type), getString(R.string.settings_news_type_default));

        Uri base = Uri.parse(API_REQUEST_URL);

        Uri.Builder builder = base.buildUpon();

        builder.appendQueryParameter(PAGE_SIZE,PageSize);
        builder.appendQueryParameter(SHOW_TAGS, ShowTags);
        builder.appendQueryParameter(FROM_DATE,FromDate);
        builder.appendQueryParameter(TO_DATE,ToDate);
        builder.appendQueryParameter(Q, country);
        builder.appendQueryParameter(Q, newsType);
        builder.appendQueryParameter(API_KEY, ApiKey);
        return  new NewsLoader(this, builder.toString());
    }

    @Override
    public  void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles)
    {

        View loadInd = findViewById(R.id.news_loading_progress);
        loadInd.setVisibility(View.GONE);
        no_news_message.setText(R.string.no_news);
        mArticle.clear();
        if(newsArticles != null && !newsArticles.isEmpty())
        {
            mArticle.addAll(newsArticles);
        }
    }

    @Override
    public  void onLoaderReset(Loader<List<NewsArticle>> loader){
        mArticle.clear();
    }





}
