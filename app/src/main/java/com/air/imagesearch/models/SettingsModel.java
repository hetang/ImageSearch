package com.air.imagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hetashah on 2/14/15.
 */
public class SettingsModel implements Parcelable {
    private String imgSize = "medium" ;
    private String imgColor = "";
    private String imgType = "photo";

    // Constructor
    public SettingsModel(String imgSize, String imgColor, String imgType){
        this.imgSize = imgSize;
        this.imgColor = imgColor;
        this.imgType = imgType;
    }

    // Parcelling part
    public SettingsModel(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.imgSize = data[0];
        this.imgColor = data[1];
        this.imgType = data[2];
    }

    public SettingsModel() {

    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgColor() {
        return imgColor;
    }

    public void setImgColor(String imgColor) {
        this.imgColor = imgColor;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.imgSize,
                this.imgColor,
                this.imgType});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SettingsModel createFromParcel(Parcel in) {
            return new SettingsModel(in);
        }

        public SettingsModel[] newArray(int size) {
            return new SettingsModel[size];
        }
    };
}
