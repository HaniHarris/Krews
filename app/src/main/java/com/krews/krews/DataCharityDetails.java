package com.krews.krews;

import com.google.gson.annotations.SerializedName;

public class DataCharityDetails {

    @SerializedName("code")
    public String code;

    @SerializedName("data")
    public Data data;

    public static class Data {

        @SerializedName("name")
        public String name;

        @SerializedName("street")
        public String street;

        @SerializedName("city")
        public String city;

        @SerializedName("state")
        public String state;

        @SerializedName("zipCode")
        public String zipCode;

        @SerializedName("country")
        public String country;

        @SerializedName("classification")
        public String classification;

        @SerializedName("affiliation")
        public String affiliation;
    }
}
