package com.air.imagesearch.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.air.imagesearch.R;
import com.air.imagesearch.adapters.ImageSearchAdaptor;
import com.air.imagesearch.builder.ImageSearchURLBuilder;
import com.air.imagesearch.listners.EndlessScrollListener;
import com.air.imagesearch.listners.SettingsDialog;
import com.air.imagesearch.listners.SettingsDialog.SettingsDialogListener;
import com.air.imagesearch.models.BuilderDataModel;
import com.air.imagesearch.models.ImageModel;
import com.air.imagesearch.models.SettingsModel;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;


public class ImageSearch extends ActionBarActivity implements SettingsDialogListener {
    private String prevQuery;
    private String currentQuery = "android";

    private StaggeredGridView imagesGridView;
    private ProgressBar spinner;
    private ImageSearchAdaptor adaptor;

    private ArrayList<ImageModel> imageResults;
    private ImageSearchURLBuilder builder;
    private SwipeRefreshLayout swipeContainer;
    private TextView connectionError;
    private BuilderDataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupVariables();
        setupView();
    }

    private void setupVariables() {
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        imagesGridView = (StaggeredGridView) findViewById(R.id.grVwImages);
        imageResults = new ArrayList<ImageModel>();
        adaptor = new ImageSearchAdaptor(this, imageResults);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        connectionError = (TextView) findViewById(R.id.txtVwConnectErr);
    }

    private void setupView() {
        Typeface fontJamesFajardo = Typeface.createFromAsset(this.getAssets(), "fonts/JamesFajardo.ttf");
        Typeface fontRingm = Typeface.createFromAsset(this.getAssets(), "fonts/RINGM.ttf");

        connectionError.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        imagesGridView.setVisibility(View.GONE);
        imagesGridView.setAdapter(adaptor);
        connectionError.setTypeface(fontRingm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar.setLogo(R.drawable.ic_logo);
        mTitle.setTypeface(fontJamesFajardo);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                builder.getImages(currentQuery);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        SettingsModel settings = new SettingsModel();
        model = new BuilderDataModel();
        model.setImgSrhAdaptor(adaptor);
        model.setSpinner(spinner);
        model.setImagesGridView(imagesGridView);
        model.setSwipeContainer(swipeContainer);
        model.setConnectionError(connectionError);
        model.setSettings(settings);

        builder = new ImageSearchURLBuilder(model);
        builder.getImages(currentQuery);
        imagesGridView.setOnScrollListener(new EndlessScrollListener(builder));
        imagesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent imgDisplay = new Intent(ImageSearch.this, ImageDisplay.class);
                ImageModel model = imageResults.get(position);
                imgDisplay.putExtra("url", model.getUrl());
                startActivity(imgDisplay);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);*/

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                currentQuery = query;
                prevQuery = query;
                builder.getImages(query);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (prevQuery != null) {
                    EditText searchText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                    searchText.setText(prevQuery);
                    searchText.setSelection(searchText.getText().length());
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.muSettings:
                //Toast.makeText(this, "Settings Click", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getSupportFragmentManager();
                SettingsDialog editNameDialog = SettingsDialog.newInstance(model.getSettings());
                editNameDialog.show(fm, "fragment_settings_dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onSaveSettingsDialog(SettingsModel settings) {
        model.setSettings(settings);
        builder.getImages(currentQuery);
        //Toast.makeText(this, "Hi, " + settings.getImgSize(), Toast.LENGTH_SHORT).show();
    }
}
