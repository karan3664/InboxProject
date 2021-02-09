package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.products;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.DeleteCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.Category;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.GetCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.DeleteProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get.GetProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get.Product;
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


public class ProductsFragment extends Fragment {

    RecyclerView rvCategories;
    FloatingActionButton fabAdd;
    ArrayList<Product> categoryArrayList = new ArrayList<>();
    private TopCategoryCustomAdapter topCategoryCustomAdapter;
    protected ViewDialog viewDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        rvCategories = root.findViewById(R.id.rvCategories);
        viewDialog = new ViewDialog(getContext());
        viewDialog.setCancelable(false);
        fabAdd = root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProductsActivity.class);
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
        Call<GetProductsModel> marqueCall = RetrofitHelper.createService(RetrofitHelper.Service.class).GetProductsModel();
        marqueCall.enqueue(new Callback<GetProductsModel>() {
            @Override
            public void onResponse(@NonNull Call<GetProductsModel> call, @NonNull Response<GetProductsModel> response) {
                hideProgressDialog();
                GetProductsModel object = response.body();

                if (response.isSuccessful()) {

                    Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));

                    assert object != null;


                    categoryArrayList = object.getProduct();
                    Collections.reverse(categoryArrayList);
                    topCategoryCustomAdapter = new TopCategoryCustomAdapter(categoryArrayList);
                    rvCategories.setAdapter(topCategoryCustomAdapter);


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetProductsModel> call, @NonNull Throwable t) {

                hideProgressDialog();
                t.printStackTrace();
                Log.e("Category_Response", t.getMessage() + "");
            }
        });


    }

    public class TopCategoryCustomAdapter extends RecyclerView.Adapter<TopCategoryCustomAdapter.MyViewHolder> {

        private ArrayList<Product> moviesList;

        public TopCategoryCustomAdapter(ArrayList<Product> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_products, parent, false);

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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


            final Product datum = moviesList.get(position);

            holder.tvCatName.setText(datum.getProductName() + "");
            if (datum.getProductImage() != null) {
//                Uri uri = Uri.parse(BuildConstants.Main_Image + datum.getImage());
//                holder.imageView_Category.setImageURI(uri);

                Glide.with(getContext())
                        .load(datum.getProductImage())
                        .placeholder(R.drawable.ic_image)
//                    .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.imageView);
            }
            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), AddProductsActivity.class);
                    i.putExtra("name", datum.getProductImage() + "");
                    i.putExtra("id", datum.getProductId() + "");
                    startActivityForResult(i, 1);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("product_id", datum.getProductId() + "");


                    showProgressDialog();
                    Call<DeleteProductsModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).DeleteProductsModel(hashMap);
                    loginModelCall.enqueue(new Callback<DeleteProductsModel>() {

                        @Override
                        public void onResponse(@NonNull Call<DeleteProductsModel> call, @NonNull Response<DeleteProductsModel> response) {
                            DeleteProductsModel object = response.body();
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
                        public void onFailure(@NonNull Call<DeleteProductsModel> call, @NonNull Throwable t) {
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

            ImageView imageView;
            RelativeLayout lyt_parent;
            TextView tvCatName, tvEdit, tvDelete;

            public MyViewHolder(View view) {
                super(view);


                tvCatName = view.findViewById(R.id.tvCatName);
                tvEdit = view.findViewById(R.id.tvEdit);
                tvDelete = view.findViewById(R.id.tvDelete);
                imageView = view.findViewById(R.id.imageView);

            }

        }

    }
}