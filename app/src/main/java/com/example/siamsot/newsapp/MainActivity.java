package com.example.siamsot.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    private static final String REQUEST_URL = "https://content.guardianapis.com/search";
    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check the internet connection. If not connected, display No Internet connection
        if (!isNetworkAvailable()) {
            ProgressBar progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(ProgressBar.GONE);
            TextView noNewsText = findViewById(R.id.no_connection);
            noNewsText.setText(R.string.no_connection);
        } else {
            // get the Loadermanager
            LoaderManager lm = getLoaderManager();
            /**
             *  Initialize the loader. Pass in the int ID constant defined above and pass in null for
             * the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
             * because this activity implements the LoaderCallbacks interface).
             */
            lm.initLoader(NEWS_LOADER, null, this);
        }
    }
    /**
     * Needed to add the options menu and run it properly
     */
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String keyword = sharedPrefs.getString(
                getString(R.string.settings_keyword_key),
                getString(R.string.settings_keyword_default));
        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("from-date", "2017-08-01");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("orderby", orderBy);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        updateUI(news);
    }

    // required override
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        return;
    }

    /**
     * Helper methods
     */

    private void updateUI(List<News> news) {
        // if ListView is empty, display no news found
        if (news == null || news.isEmpty()) {
            TextView noNewsText = findViewById(R.id.no_connection);
            noNewsText.setText(R.string.no_news);
        }
        // hide the progressbar when the download finished
        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(ProgressBar.GONE);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.news_list);

        // Create a new {@link ArrayAdapter} of news
        final NewsAdapter adapter = new NewsAdapter(
                this, R.layout.news_list, news);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        // Add onItemClickListener to open website of each earthquake after click
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = adapter.getItem(position);
                String url = currentNews.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private boolean isNetworkAvailable() {
        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

/**
 * End Helper methods
 */
}
