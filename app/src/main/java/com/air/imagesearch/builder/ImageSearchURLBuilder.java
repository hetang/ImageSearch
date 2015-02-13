package com.air.imagesearch.builder;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.air.imagesearch.adapters.ImageSearchAdaptor;
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

    private ListView lvBooks;
    private ImageSearchAdaptor imgSrhAdaptor;
    private ProgressBar spinner;
    private GridView imagesGridView;
    private SwipeRefreshLayout swipeContainer;

    private static final int itemPerPage = 8;

    public ImageSearchURLBuilder(ImageSearchAdaptor imgSrhAdaptor, ProgressBar spinner, GridView imagesGridView, SwipeRefreshLayout swipeContainer) {
        this.imgSrhAdaptor = imgSrhAdaptor;
        this.spinner = spinner;
        this.imagesGridView = imagesGridView;
        this.swipeContainer = swipeContainer;

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

            imgSrhAdaptor.clear();

            client.get(url.toString(), new ImageSearchJSONResponseHandler());
            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.GONE);
            }
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
                    imageResultJSON = response.getJSONObject("responseData").getJSONArray("results");
                    images = ImageModel.fromJson(imageResultJSON);
                    imgSrhAdaptor.addAll(images);
                    imgSrhAdaptor.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                // Invalid JSON format, show appropriate error.
                e.printStackTrace();
            }

            if(swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
            if(spinner != null) {
                spinner.setVisibility(View.GONE);
            }

            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if(swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
            if(spinner != null) {
                spinner.setVisibility(View.GONE);
            }
            if(imagesGridView != null) {
                imagesGridView.setVisibility(View.VISIBLE);
            }
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }
}
