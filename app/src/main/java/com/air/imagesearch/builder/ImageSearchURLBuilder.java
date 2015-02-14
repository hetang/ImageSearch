package com.air.imagesearch.builder;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.air.imagesearch.adapters.ImageSearchAdaptor;
import com.air.imagesearch.models.BuilderDataModel;
import com.air.imagesearch.models.ImageModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by hetashah on 2/12/15.
 */
public class ImageSearchURLBuilder {
    private static final String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private StringBuilder url;

    private AsyncHttpClient client;
    private ArrayList<ImageModel> images;

    private static final int itemPerPage = 8;
    private BuilderDataModel dataModel;

    public ImageSearchURLBuilder(BuilderDataModel dataModel) {
        this.dataModel = dataModel;
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getImages(final String query) {
        try {
            url = new StringBuilder(getApiUrl("&q="));
            url.append(URLEncoder.encode(query, "utf-8"));

            dataModel.getImgSrhAdaptor().clear();

            client.get(url.toString(), new ImageSearchJSONResponseHandler());
            GridView imagesGridView = dataModel.getImagesGridView();
            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.GONE);
            }
            ProgressBar spinner = dataModel.getSpinner();
            if(spinner != null) {
                spinner.setVisibility(View.VISIBLE);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getNextPageImages(int page) {
        if(page > 0 && url.length() > 0) {
            client.get(url.toString() + "&start=" + (page * itemPerPage), new ImageSearchJSONResponseHandler());
        }
    }

    private class ImageSearchJSONResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            JSONArray imageResultJSON = null;
            try {
                if(response != null) {
                    Log.i("DEBUG", "response = " + response.toString());
                    imageResultJSON = response.getJSONObject("responseData").getJSONArray("results");
                    images = ImageModel.fromJson(imageResultJSON);
                    dataModel.getConnectionError().setVisibility(View.GONE);
                    dataModel.getImgSrhAdaptor().addAll(images);
                    dataModel.getImgSrhAdaptor().notifyDataSetChanged();
                }
            } catch (JSONException e) {
                // Invalid JSON format, show appropriate error.
                e.printStackTrace();
            }

            SwipeRefreshLayout swipeContainer = dataModel.getSwipeContainer();
            if(swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }

            ProgressBar spinner = dataModel.getSpinner();
            if(spinner != null) {
                spinner.setVisibility(View.GONE);
            }

            GridView imagesGridView = dataModel.getImagesGridView();
            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            SwipeRefreshLayout swipeContainer = dataModel.getSwipeContainer();
            if(swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }

            ProgressBar spinner = dataModel.getSpinner();
            if(spinner != null) {
                spinner.setVisibility(View.GONE);
            }

            GridView imagesGridView = dataModel.getImagesGridView();
            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.GONE);
            }
            dataModel.getConnectionError().setVisibility(View.VISIBLE);
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }
}
