package com.krews.krews;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    LinearLayout layMain;
    EditText etSearch;
    RecyclerView recyclerView;

    Call<DataCharity> callSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layMain=findViewById(R.id.layMain);
        etSearch=findViewById(R.id.etSearch);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager lmProducts=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lmProducts);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value=String.valueOf(s);
                if(CheckNetwork.isAvailable(MainActivity.this))
                {
                    callAPI(value);
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(layMain, "No Network Connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(CheckNetwork.isAvailable(this))
        {
            callAPI("");
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(layMain, "No Network Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(callSearch!=null)
        {
            callSearch.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    private void callAPI(String value) {

        APIInterface apiInterface= APIClient.getClient(getResources().getString(R.string.base_url),MainActivity.this).create(APIInterface.class);
        callSearch=apiInterface.searchCharity(getString(R.string.user_key),value);

        callSearch.enqueue(new Callback<DataCharity>() {
            @Override
            public void onResponse(Call<DataCharity> call, Response<DataCharity> response) {

                DataCharity dataCharity=response.body();
                String apiResponse="";
                if(dataCharity!=null)
                {
                    apiResponse=dataCharity.code;
                }

                if(apiResponse==null)
                {
                    apiResponse="";
                }


                ArrayList<ModelCharity> arrayCharity=new ArrayList<>();
                if(apiResponse.equalsIgnoreCase("200"))
                {
                    if (dataCharity.data.size() > 0)
                    {
                        List<DataCharity.Data> listCharity = dataCharity.data;
                        for(DataCharity.Data charityList:listCharity)
                        {
                            ModelCharity modelCharity = new ModelCharity();
                            modelCharity.setEin(charityList.ein);
                            modelCharity.setCharityName(charityList.charityName);
                            modelCharity.setCity(charityList.city);
                            modelCharity.setState(charityList.state);
                            modelCharity.setUrl(charityList.url);
                            modelCharity.setZipCode(charityList.zipCode);
                            arrayCharity.add(modelCharity);
                        }
                    }
                }

                if(!arrayCharity.isEmpty())
                {
                    recyclerView.setVisibility(View.VISIBLE);

                    AdapterCharity adapterCharity=new AdapterCharity(arrayCharity,MainActivity.this);
                    recyclerView.setAdapter(adapterCharity);
                    adapterCharity.notifyDataSetChanged();
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DataCharity> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}