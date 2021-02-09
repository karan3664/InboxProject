package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.AddCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.UpdateCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.login.CheckLoginModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.DashboardActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.login.LoginActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.PrefUtils;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoriesActivity extends AppCompatActivity {

    Button btnAdd;
    TextInputEditText etAddCategories;
    protected ViewDialog viewDialog;
    String name, id;
    TextView tvCat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
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
            tvCat.setText("UPDATE CATEGORIES");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddCategories.getText().toString().isEmpty()) {
                        etAddCategories.setError("Add Categories");
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
                        etAddCategories.setError("Add Categories");
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
        hashMap.put("c_name", cate + "");


        showProgressDialog();
        Call<AddCategoriesModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddCategoriesModel(hashMap);
        loginModelCall.enqueue(new Callback<AddCategoriesModel>() {

            @Override
            public void onResponse(@NonNull Call<AddCategoriesModel> call, @NonNull Response<AddCategoriesModel> response) {
                AddCategoriesModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddCategoriesActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddCategoriesActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddCategoriesModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }

    public void UpdateCategories(String id) {

        final String cate = etAddCategories.getText().toString().trim();


        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("c_id", id + "");
        hashMap.put("c_name", cate + "");



        showProgressDialog();
        Call<UpdateCategoriesModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).UpdateCategoriesModel(hashMap);
        loginModelCall.enqueue(new Callback<UpdateCategoriesModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateCategoriesModel> call, @NonNull Response<UpdateCategoriesModel> response) {
                UpdateCategoriesModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddCategoriesActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddCategoriesActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateCategoriesModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }
}