package com.krews.krews;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("charitysearch")
    Call<DataCharity> searchCharity(@Field("user_key") String user_key,
                                    @Field("searchTerm") String searchTerm);

    @FormUrlEncoded
    @POST("charityfinancial")
    Call<DataCharityDetails> charityDetails(@Field("user_key") String user_key,
                                            @Field("ein") String ein);
}

