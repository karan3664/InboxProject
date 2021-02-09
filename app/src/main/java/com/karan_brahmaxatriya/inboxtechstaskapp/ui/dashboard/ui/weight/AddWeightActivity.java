package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.weight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.AddCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.UpdateCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.add.AddWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.add.UpdateWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories.AddCategoriesActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWeightActivity extends AppCompatActivity {
    Button btnAdd;
    TextInputEditText etAddCategories;
    protected ViewDialog viewDialog;
    String name, id;
    TextView tvCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        viewDialog = new ViewDialog(this);
        viewDialog.setCancelable(false);
        btnAdd = findViewById(R.id.btnAdd);
        etAddCategories = findViewById(R.id.etAddCategories);
        tvCat = findViewById(R.id.tvCat);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        id = i.getStringExtra("id");
        if (name != null) {

            etAddCategories.setText(name);
            btnAdd.setText("Update");
            tvCat.setText("UPDATE WEIGHT");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddCategories.getText().toString().isEmpty()) {
                        etAddCategories.setError("Add Weight");
                        etAddCategories.requestFocus();
                        return;
                    } else {
                        UpdateCategories(id);
                    }
                }
            });
        } else {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddCategories.getText().toString().isEmpty()) {
                        etAddCategories.setError("Add Weight");
                        etAddCategories.requestFocus();
                        return;
                    } else {
                        AddCategories();
                    }
                }
            });

        }

    }
    protected void hideProgressDialog() {
        viewDialog.dismiss();
    }

    protected void showProgressDialog() {
        viewDialog.show();
    }

    public void AddCategories() {

        final String cate = etAddCategories.getText().toString().trim();


        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("weight", cate + "");


        showProgressDialog();
        Call<AddWeightModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddWeightModel(hashMap);
        loginModelCall.enqueue(new Callback<AddWeightModel>() {

            @Override
            public void onResponse(@NonNull Call<AddWeightModel> call, @NonNull Response<AddWeightModel> response) {
                AddWeightModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddWeightActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddWeightActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddWeightModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }

    public void UpdateCategories(String id) {

        final String cate = etAddCategories.getText().toString().trim();


        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id + "");
        hashMap.put("weight", cate + "");



        showProgressDialog();
        Call<UpdateWeightModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).UpdateWeightModel(hashMap);
        loginModelCall.enqueue(new Callback<UpdateWeightModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateWeightModel> call, @NonNull Response<UpdateWeightModel> response) {
                UpdateWeightModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddWeightActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddWeightActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateWeightModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }
}