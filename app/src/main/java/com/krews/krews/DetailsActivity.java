package com.krews.krews;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    LinearLayout layDetails;
    NestedScrollView nsvDetails;
    ImageView ivBack;
    TextView tvName,tvAddress,tvClassification,tvAffliation;

    Call<DataCharityDetails> callDetails;
    String ein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ein=getIntent().getStringExtra("ein");

        layDetails=findViewById(R.id.layDetails);
        nsvDetails=findViewById(R.id.nsvDetails);
        ivBack=findViewById(R.id.ivBack);
        tvName=findViewById(R.id.tvName);
        tvAddress=findViewById(R.id.tvAddress);
        tvClassification=findViewById(R.id.tvClassification);
        tvAffliation=findViewById(R.id.tvAffliation);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        nsvDetails.setVisibility(View.GONE);
        if(CheckNetwork.isAvailable(this))
        {
            callAPI();
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(layDetails, "No Network Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(callDetails!=null)
        {
            callDetails.cancel();
        }
    }

    private void callAPI() {

        APIInterface apiInterface= APIClient.getClient(getResources().getString(R.string.base_url),DetailsActivity.this).create(APIInterface.class);
        callDetails=apiInterface.charityDetails(getString(R.string.user_key),ein);

        callDetails.enqueue(new Callback<DataCharityDetails>() {
            @Override
            public void onResponse(Call<DataCharityDetails> call, Response<DataCharityDetails> response) {

                DataCharityDetails dataCharity=response.body();
                String apiResponse="";
                if(dataCharity!=null)
                {
                    apiResponse=dataCharity.code;
                    if(apiResponse!=null && apiResponse.equalsIgnoreCase("200"))
                    {
                        if(dataCharity.data!=null)
                        {
                            String name=dataCharity.data.name;
                            String street=dataCharity.data.street;
                            String city=dataCharity.data.city;
                            String state=dataCharity.data.state;
                            String zipCode=dataCharity.data.zipCode;
                            String country=dataCharity.data.country;
                            String classification=dataCharity.data.classification;
                            String affiliation=dataCharity.data.affiliation;

                            tvName.setText(name);
                            String textAddress=street+","+city+","+state+","+country+"-"+zipCode;
                            tvAddress.setText(textAddress);
                            if(classification!=null && classification.length()>0)
                            {
                                tvClassification.setText(classification);
                            }
                            if(affiliation!=null && affiliation.length()>0)
                            {

                                tvAffliation.setText(affiliation);
                            }

                            nsvDetails.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DataCharityDetails> call, Throwable t) {
            }
        });
    }
}