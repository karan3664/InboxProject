package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.subcategories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.AddCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.UpdateCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.Category;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.GetCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.AddSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.UpdateSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories.AddCategoriesActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories.AddCategoriesFragment;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSubCategoriesActivity extends AppCompatActivity {
    Button btnAdd;
    TextInputEditText etAddCategories;
    protected ViewDialog viewDialog;
    String name, id;
    TextView tvCat;
    Spinner spinner;
    ArrayAdapter arrayAdapter;
    ArrayList<Category> categoryArrayList = new ArrayList<>();
    View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_categories);
        viewDialog = new ViewDialog(this);
        viewDialog.setCancelable(false);
        btnAdd = findViewById(R.id.btnAdd);
        spinner = findViewById(R.id.spinner);
        etAddCategories = findViewById(R.id.etAddCategories);
        tvCat = findViewById(R.id.tvCat);
        view1 = findViewById(R.id.view1);

        showProgressDialog();
        Call<GetCategoriesModel> marqueCall = RetrofitHelper.createService(RetrofitHelper.Service.class).GetCategoriesModel();
        marqueCall.enqueue(new Callback<GetCategoriesModel>() {
            @Override
            public void onResponse(@NonNull Call<GetCategoriesModel> call, @NonNull Response<GetCategoriesModel> response) {
                hideProgressDialog();
                GetCategoriesModel object = response.body();

                if (response.isSuccessful()) {

                    Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));

                    assert object != null;
                    categoryArrayList = new ArrayList<Category>();
//                    categoryArrayList = object.getCategory();
                    categoryArrayList.add(new Category("Select Category"));
                    try {
                        for (int i = 0; i < categoryArrayList.size(); i++) {
                            categoryArrayList.add(object.getCategory().get(i));
                            initCustomSpinner();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCategoriesModel> call, @NonNull Throwable t) {

                hideProgressDialog();
                t.printStackTrace();
                Log.e("Category_Response", t.getMessage() + "");
            }
        });
        Intent i = getIntent();
        name = i.getStringExtra("name");
        id = i.getStringExtra("id");
        if (name != null) {

            etAddCategories.setText(name);
            btnAdd.setText("Update");
            view1.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
////            tvCat.setText("UPDATE CATEGORIES");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddCategories.getText().toString().isEmpty()) {
                        etAddCategories.setError("Add Sub Categories");
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
                        etAddCategories.setError("Add Sub Categories");
                        etAddCategories.requestFocus();
                        return;
                    } else {
                        AddCategories();
                    }
                }
            });
        }



    }

    private void initCustomSpinner() {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(AddSubCategoriesActivity.this, categoryArrayList);
        spinner.setAdapter(customSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String insurSpinner = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<Category> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<Category> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(AddSubCategoriesActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER);
            txt.setText(asr.get(position).getCName());
            txt.setTextColor(Color.parseColor("#2196F3"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddSubCategoriesActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            txt.setText(asr.get(i).getCName());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
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
        hashMap.put("s_name", cate + "");
        hashMap.put("c_id", categoryArrayList.get(spinner.getSelectedItemPosition()).getCId() + "");
        Log.e("Params", hashMap + "");

        showProgressDialog();
        Call<AddSubCategoriesModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddSubCategoriesModel(hashMap);
        loginModelCall.enqueue(new Callback<AddSubCategoriesModel>() {

            @Override
            public void onResponse(@NonNull Call<AddSubCategoriesModel> call, @NonNull Response<AddSubCategoriesModel> response) {
                AddSubCategoriesModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddSubCategoriesActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddSubCategoriesActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddSubCategoriesModel> call, @NonNull Throwable t) {
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
        hashMap.put("s_name", cate + "");

        Log.e("Params", hashMap + "");
        showProgressDialog();
        Call<UpdateSubCategoriesModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).UpdateSubCategoriesModel(hashMap);
        loginModelCall.enqueue(new Callback<UpdateSubCategoriesModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateSubCategoriesModel> call, @NonNull Response<UpdateSubCategoriesModel> response) {
                UpdateSubCategoriesModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddSubCategoriesActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddSubCategoriesActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateSubCategoriesModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }
}