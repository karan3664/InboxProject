package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.units;

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
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.add.AddUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.add.UpdateUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories.AddCategoriesActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUnitsActivity extends AppCompatActivity {
    Button btnAdd;
    TextInputEditText etAddCategories;
    protected ViewDialog viewDialog;
    String name, id;
    TextView tvCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_units);
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
            tvCat.setText("UPDATE UNITS");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddCategories.getText().toString().isEmpty()) {
                        etAddCategories.setError("Add Unit");
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
                        etAddCategories.setError("Add Unit");
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
        hashMap.put("unit", cate + "");


        showProgressDialog();
        Call<AddUnitsModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddUnitsModel(hashMap);
        loginModelCall.enqueue(new Callback<AddUnitsModel>() {

            @Override
            public void onResponse(@NonNull Call<AddUnitsModel> call, @NonNull Response<AddUnitsModel> response) {
                AddUnitsModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddUnitsActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddUnitsActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddUnitsModel> call, @NonNull Throwable t) {
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
        hashMap.put("unit", cate + "");



        showProgressDialog();
        Call<UpdateUnitsModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).UpdateUnitsModel(hashMap);
        loginModelCall.enqueue(new Callback<UpdateUnitsModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateUnitsModel> call, @NonNull Response<UpdateUnitsModel> response) {
                UpdateUnitsModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddUnitsActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddUnitsActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateUnitsModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }
}