package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.subcategories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.DeleteCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.Category;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.GetCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.DeleteSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub.GetSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub.Subcategory;
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

public class SubCategoriesFragment extends Fragment {


    RecyclerView rvCategories;
    FloatingActionButton fabAdd;
    ArrayList<Subcategory> subcategoryArrayList = new ArrayList<>();
    private TopCategoryCustomAdapter topCategoryCustomAdapter;
    protected ViewDialog viewDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        rvCategories = root.findViewById(R.id.rvSubCategories);
        viewDialog = new ViewDialog(getContext());
        viewDialog.setCancelable(false);
        fabAdd = root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSubCategoriesActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        LinearLayoutManager mLayoutManager11 = new LinearLayoutManager(getContext());
        mLayoutManager11.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategories.setLayoutManager(mLayoutManager11);
        rvCategories.setHasFixedSize(true);
        HomeCateogry();
        return root;
    }
    protected void hideProgressDialog() {
        viewDialog.dismiss();
    }

    protected void showProgressDialog() {
        viewDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            HomeCateogry();
        }
    }

    private void HomeCateogry() {
        showProgressDialog();
        Call<GetSubCategoriesModel> marqueCall = RetrofitHelper.createService(RetrofitHelper.Service.class).GetSubCategoriesModel();
        marqueCall.enqueue(new Callback<GetSubCategoriesModel>() {
            @Override
            public void onResponse(@NonNull Call<GetSubCategoriesModel> call, @NonNull Response<GetSubCategoriesModel> response) {
                hideProgressDialog();
                GetSubCategoriesModel object = response.body();

                if (response.isSuccessful()) {

                    Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));

                    assert object != null;


                    subcategoryArrayList = object.getSubcategory();
                    Collections.reverse(subcategoryArrayList);
                    topCategoryCustomAdapter = new TopCategoryCustomAdapter(subcategoryArrayList);
                    rvCategories.setAdapter(topCategoryCustomAdapter);


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetSubCategoriesModel> call, @NonNull Throwable t) {

                hideProgressDialog();
                t.printStackTrace();
                Log.e("Category_Response", t.getMessage() + "");
            }
        });


    }

    public class TopCategoryCustomAdapter extends RecyclerView.Adapter<TopCategoryCustomAdapter.MyViewHolder> {

        private ArrayList<Subcategory> moviesList;

        public TopCategoryCustomAdapter(ArrayList<Subcategory> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);

            return new MyViewHolder(itemView);
        }

        public void clear() {
            int size = this.moviesList.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    this.moviesList.remove(0);
                }

                this.notifyItemRangeRemoved(0, size);
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


            final Subcategory datum = moviesList.get(position);

            holder.tvCatName.setText(datum.getSName() + "");

            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), AddSubCategoriesActivity.class);
                    i.putExtra("name", datum.getSName() + "");
                    i.putExtra("id", datum.getId() + "");
                    startActivityForResult(i, 1);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", datum.getId() + "");


                    showProgressDialog();
                    Call<DeleteSubCategoriesModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).DeleteSubCategoriesModel(hashMap);
                    loginModelCall.enqueue(new Callback<DeleteSubCategoriesModel>() {

                        @Override
                        public void onResponse(@NonNull Call<DeleteSubCategoriesModel> call, @NonNull Response<DeleteSubCategoriesModel> response) {
                            DeleteSubCategoriesModel object = response.body();
                            hideProgressDialog();
                            if (response.isSuccessful()) {
                                Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                                assert object != null;
                                Toast.makeText(getContext(), object.getMessage() + "", Toast.LENGTH_SHORT).show();
                                HomeCateogry();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getContext(), jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DeleteSubCategoriesModel> call, @NonNull Throwable t) {
                            hideProgressDialog();
                            t.printStackTrace();
                            Log.e("Login_Response", t.getMessage() + "");
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView ivCatImage;
            RelativeLayout lyt_parent;
            TextView tvCatName, tvEdit, tvDelete;

            public MyViewHolder(View view) {
                super(view);


                tvCatName = view.findViewById(R.id.tvCatName);
                tvEdit = view.findViewById(R.id.tvEdit);
                tvDelete = view.findViewById(R.id.tvDelete);

            }

        }

    }

}