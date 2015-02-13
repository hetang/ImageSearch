package com.air.imagesearch.models;

import com.air.imagesearch.helpers.ApplicationHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hetashah on 2/12/15.
 */
public class ImageModel {

    private String imageId;
    private String url;
    private String title;
    private String thumbURL;

    private int width;
    private int height;
    private int thumbWidth;
    private int thumbHeight;

    public ImageModel(JSONObject imgResponse) {
        if(imgResponse != null) {
            try {
                this.imageId = imgResponse.getString("imageId");
                this.url = imgResponse.getString("url");
                this.title = imgResponse.getString("title");
                this.thumbURL = imgResponse.getString("tbUrl");

                this.width = imgResponse.getInt("width");
                this.height = imgResponse.getInt("height");
                this.thumbWidth = imgResponse.getInt("tbWidth");
                this.thumbHeight = imgResponse.getInt("tbHeight");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<ImageModel> fromJson(JSONArray docs) {
        ArrayList<ImageModel> results = new ArrayList<ImageModel>();
        if(ApplicationHelpers.isNotEmpty(docs)) {
            for(int i=0;i<docs.length();i++) {
                try {
                    results.add(new ImageModel(docs.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }
}
