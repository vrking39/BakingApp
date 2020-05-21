package com.example.bakingappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StepsModel implements Parcelable {

    public static final Creator<StepsModel> CREATOR = new Creator<StepsModel>() {
        @Override
        public StepsModel createFromParcel(Parcel in) {
            return new StepsModel(in);
        }

        @Override
        public StepsModel[] newArray(int size) {
            return new StepsModel[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("description")
    private String description;
    @SerializedName("videoURL")
    private String videoURL;
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public StepsModel() {
        this.id = 0;
        this.shortDescription = "";
        this.description = "";
        this.videoURL = "";
        this.thumbnailURL = "";
    }

    protected StepsModel(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }


    @Override
    public String toString() {
        return "Step{" +
                "videoURL='" + videoURL + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
