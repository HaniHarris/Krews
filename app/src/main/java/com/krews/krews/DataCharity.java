package com.krews.krews;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataCharity {

    @SerializedName("code")
    public String code;

    public List<Data> data;

    public static class Data {

        @SerializedName("ein")
        public String ein;

        @SerializedName("charityName")
        public String charityName;

        @SerializedName("url")
        public String url;

        @SerializedName("city")
        public String city;

        @SerializedName("state")
        public String state;

        @SerializedName("zipCode")
        public String zipCode;

    }
}
