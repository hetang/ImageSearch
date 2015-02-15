package com.air.imagesearch.models;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.air.imagesearch.adapters.ImageSearchAdaptor;
import com.etsy.android.grid.StaggeredGridView;

/**
 * Created by hetashah on 2/13/15.
 */
public class BuilderDataModel {
    private TextView connectionError;
    private ImageSearchAdaptor imgSrhAdaptor;
    private ProgressBar spinner;
    private StaggeredGridView imagesGridView;
    private SwipeRefreshLayout swipeContainer;
    private SettingsModel settings;

    public SettingsModel getSettings() {
        return settings;
    }

    public void setSettings(SettingsModel settings) {
        this.settings = settings;
    }

    public TextView getConnectionError() {
        return connectionError;
    }

    public void setConnectionError(TextView connectionError) {
        this.connectionError = connectionError;
    }

    public ImageSearchAdaptor getImgSrhAdaptor() {
        return imgSrhAdaptor;
    }

    public void setImgSrhAdaptor(ImageSearchAdaptor imgSrhAdaptor) {
        this.imgSrhAdaptor = imgSrhAdaptor;
    }

    public ProgressBar getSpinner() {
        return spinner;
    }

    public void setSpinner(ProgressBar spinner) {
        this.spinner = spinner;
    }

    public StaggeredGridView getImagesGridView() {
        return imagesGridView;
    }

    public void setImagesGridView(StaggeredGridView imagesGridView) {
        this.imagesGridView = imagesGridView;
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public void setSwipeContainer(SwipeRefreshLayout swipeContainer) {
        this.swipeContainer = swipeContainer;
    }
}
