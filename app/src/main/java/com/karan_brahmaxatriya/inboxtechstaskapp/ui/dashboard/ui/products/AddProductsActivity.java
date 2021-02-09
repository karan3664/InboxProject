package com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.AddCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.Category;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.GetCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.AddProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.UpdateProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub.GetSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub.Subcategory;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.get.GetUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.get.Unit;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.get.GetWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.get.Weight;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.addCategories.AddCategoriesActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.subcategories.AddSubCategoriesActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.subcategories.SubCategoriesFragment;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.ui.weight.WeightFragment;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.register.RegisterActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission_group.CAMERA;
import static android.os.Build.VERSION_CODES.M;

public class AddProductsActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etProductName, etOldPrice, etNewPrice, etDesc, etQty;
    Spinner cat_spinner, sub_spinner, unit_spinner, weight_spinner;
    Button btnAdd;
    ImageView imageViewProfile, imageView;
    ArrayList<Category> categoryArrayList = new ArrayList<>();
    ArrayList<Subcategory> subcategoryArrayList = new ArrayList<>();
    ArrayList<Unit> unitArrayList = new ArrayList<>();
    ArrayList<Weight> weightArrayList = new ArrayList<>();
    protected ViewDialog viewDialog;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    private static final int IMAGE_REQUEST = 1;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    private File photoFile = null;
    File profileImage = null;
    private View parent_view;
    String image, name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        viewDialog = new ViewDialog(this);
        viewDialog.setCancelable(false);
        etProductName = findViewById(R.id.etProductName);
        etOldPrice = findViewById(R.id.etOldPrice);
        etNewPrice = findViewById(R.id.etNewPrice);
        etDesc = findViewById(R.id.etDesc);
        etQty = findViewById(R.id.etQty);
        cat_spinner = findViewById(R.id.cat_spinner);
        sub_spinner = findViewById(R.id.sub_spinner);
        unit_spinner = findViewById(R.id.unit_spinner);
        weight_spinner = findViewById(R.id.weight_spinner);
        btnAdd = findViewById(R.id.btnAdd);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageView = findViewById(R.id.imageView);
        btnAdd.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);

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
                            catCustomSpinner();

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

        GetWeight();
        HomeCateogry();
        Intent i = getIntent();
        name = i.getStringExtra("name");
        id = i.getStringExtra("id");
        if (id != null) {


            btnAdd.setText("Update");

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateProducts(id);
                }
            });
        } else {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddProducts();
                }
            });

        }

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(AddProductsActivity.this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(AddProductsActivity.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void selectImageProfile() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= M) {
                        if (checkCameraPermission())
                            cameraIntentProfile();
                        else
                            requestPermission();
                    } else
                        cameraIntentProfile();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= M) {
                        if (checkGalleryPermission())
                            galleryIntentProfile();
                        else
                            requestGalleryPermission();
                    } else
                        galleryIntentProfile();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    private void galleryIntentProfile() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntentProfile() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.karan_brahmaxatriya.inboxtechstaskapp", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "image",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
        profileImage = new File(image.getAbsolutePath());
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_PIC_REQUEST && photoFile != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            if (null != bitmap) {
                imageView.setImageBitmap(bitmap);
                profileImage = getUserImageFile(bitmap);
                Bitmap bitmaps;
                try {
                    bitmaps = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(profileImage)));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmaps, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);

                    Log.e("ImageBase64", image + "");

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryURI);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != bitmap) {
                imageView.setImageBitmap(bitmap);
                profileImage = getUserImageFile(bitmap);
                Uri targetUri = data.getData();
                Bitmap bitmaps;
                try {
                    bitmaps = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmaps, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);

                    Log.e("ImageBase64", image + "");

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    private File getUserImageFile(Bitmap bitmap) {
        try {
            File f = new File(getCacheDir(), ".jpg");
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AddProductsActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(AddProductsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void GetWeight() {
        showProgressDialog();
        Call<GetWeightModel> marqueCall = RetrofitHelper.createService(RetrofitHelper.Service.class).GetWeightModel();
        marqueCall.enqueue(new Callback<GetWeightModel>() {
            @Override
            public void onResponse(@NonNull Call<GetWeightModel> call, @NonNull Response<GetWeightModel> response) {
                hideProgressDialog();
                GetWeightModel object = response.body();

                if (response.isSuccessful()) {

                    Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));


                    assert object != null;
                    weightArrayList = new ArrayList<Weight>();
//                    categoryArrayList = object.getCategory();
                    weightArrayList.add(new Weight("Select Weight"));
                    try {
                        for (int i = 0; i < weightArrayList.size(); i++) {
                            weightArrayList.add(object.getWeight().get(i));
                            weightCustomSpinner();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetWeightModel> call, @NonNull Throwable t) {

                hideProgressDialog();
                t.printStackTrace();
                Log.e("Category_Response", t.getMessage() + "");
            }
        });


    }

    private void HomeCateogry() {
        showProgressDialog();
        Call<GetUnitsModel> marqueCall = RetrofitHelper.createService(RetrofitHelper.Service.class).GetUnitsModel();
        marqueCall.enqueue(new Callback<GetUnitsModel>() {
            @Override
            public void onResponse(@NonNull Call<GetUnitsModel> call, @NonNull Response<GetUnitsModel> response) {
                hideProgressDialog();
                GetUnitsModel object = response.body();

                if (response.isSuccessful()) {

                    Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));

                    assert object != null;
                    unitArrayList = new ArrayList<Unit>();
//                    categoryArrayList = object.getCategory();
                    unitArrayList.add(new Unit("Select Unit"));
                    try {
                        for (int i = 0; i < unitArrayList.size(); i++) {
                            unitArrayList.add(object.getUnit().get(i));
                            unitCustomSpinner();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUnitsModel> call, @NonNull Throwable t) {

                hideProgressDialog();
                t.printStackTrace();
                Log.e("Category_Response", t.getMessage() + "");
            }
        });


    }

    protected void hideProgressDialog() {
        viewDialog.dismiss();
    }

    protected void showProgressDialog() {
        viewDialog.show();
    }

    private void catCustomSpinner() {
        CatCustomSpinnerAdapter customSpinnerAdapter = new CatCustomSpinnerAdapter(AddProductsActivity.this, categoryArrayList);
        cat_spinner.setAdapter(customSpinnerAdapter);
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String insurSpinner = parent.getItemAtPosition(position).toString();
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("c_id", categoryArrayList.get(cat_spinner.getSelectedItemPosition()).getCId() + "");
                showProgressDialog();
                Call<GetSubCategoriesModel> marqueCalla = RetrofitHelper.createService(RetrofitHelper.Service.class).GetSubCategoriesModelByID(hashMap);
                marqueCalla.enqueue(new Callback<GetSubCategoriesModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GetSubCategoriesModel> call, @NonNull Response<GetSubCategoriesModel> response) {
                        hideProgressDialog();
                        GetSubCategoriesModel object = response.body();

                        if (response.isSuccessful()) {

                            Log.e("TAG", "Category_Response : " + new Gson().toJson(response.body()));

                            assert object != null;


                            subcategoryArrayList = new ArrayList<Subcategory>();
//                    categoryArrayList = object.getCategory();
                            subcategoryArrayList.add(new Subcategory("Select Sub Category"));
                            try {
                                for (int i = 0; i < subcategoryArrayList.size(); i++) {
                                    subcategoryArrayList.add(object.getSubcategory().get(i));
                                    SubCustomSpinner();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CatCustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<Category> asr;

        public CatCustomSpinnerAdapter(Context context, ArrayList<Category> asr) {
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
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER);
            txt.setText(asr.get(position).getCName());
            txt.setTextColor(Color.parseColor("#2196F3"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            txt.setText(asr.get(i).getCName());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }

    private void SubCustomSpinner() {
        SubCatCustomSpinnerAdapter customSpinnerAdapter = new SubCatCustomSpinnerAdapter(AddProductsActivity.this, subcategoryArrayList);
        sub_spinner.setAdapter(customSpinnerAdapter);
        sub_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String insurSpinner = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public class SubCatCustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<Subcategory> asr;

        public SubCatCustomSpinnerAdapter(Context context, ArrayList<Subcategory> asr) {
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
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER);
            txt.setText(asr.get(position).getSName());
            txt.setTextColor(Color.parseColor("#2196F3"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            txt.setText(asr.get(i).getSName());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }


    private void unitCustomSpinner() {
        UnitCustomSpinnerAdapter customSpinnerAdapter = new UnitCustomSpinnerAdapter(AddProductsActivity.this, unitArrayList);
        unit_spinner.setAdapter(customSpinnerAdapter);
        unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class UnitCustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<Unit> asr;

        public UnitCustomSpinnerAdapter(Context context, ArrayList<Unit> asr) {
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
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER);
            txt.setText(asr.get(position).getUnit());
            txt.setTextColor(Color.parseColor("#2196F3"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            txt.setText(asr.get(i).getUnit());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }

    private void weightCustomSpinner() {
        WeightCustomSpinnerAdapter customSpinnerAdapter = new WeightCustomSpinnerAdapter(AddProductsActivity.this, weightArrayList);
        weight_spinner.setAdapter(customSpinnerAdapter);
        weight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class WeightCustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<Weight> asr;

        public WeightCustomSpinnerAdapter(Context context, ArrayList<Weight> asr) {
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
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER);
            txt.setText(asr.get(position).getWeight());
            txt.setTextColor(Color.parseColor("#2196F3"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddProductsActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            txt.setText(asr.get(i).getWeight());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageViewProfile:
                selectImageProfile();
                break;
        }
    }

    public void AddProducts() {

        final String p_name = etProductName.getText().toString().trim();
        final String p_old = etOldPrice.getText().toString().trim();
        final String p_new = etNewPrice.getText().toString().trim();
        final String p_desc = etDesc.getText().toString().trim();
        final String p_qty = etQty.getText().toString().trim();


        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("categories_id", categoryArrayList.get(cat_spinner.getSelectedItemPosition()).getCId() + "");
        hashMap.put("subcategories_id", subcategoryArrayList.get(sub_spinner.getSelectedItemPosition()).getId() + "");
        hashMap.put("product_name", p_name + "");
        hashMap.put("product_old_price", p_old + "");
        hashMap.put("product_new_price", p_new + "");
        hashMap.put("product_description", p_desc + "");
        hashMap.put("product_quantity", p_qty + "");
        hashMap.put("unit_id", unitArrayList.get(unit_spinner.getSelectedItemPosition()).getId() + "");
        hashMap.put("weight_id", weightArrayList.get(weight_spinner.getSelectedItemPosition()).getId() + "");
        hashMap.put("product_image", image + "");


        showProgressDialog();
        Call<AddProductsModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddProductsModel(hashMap);
        loginModelCall.enqueue(new Callback<AddProductsModel>() {

            @Override
            public void onResponse(@NonNull Call<AddProductsModel> call, @NonNull Response<AddProductsModel> response) {
                AddProductsModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddProductsActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddProductsActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddProductsModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }

    public void UpdateProducts(String id) {



        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("product_image", image + "");
        hashMap.put("id", id + "");


        showProgressDialog();
        Call<UpdateProductsModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).UpdateProductsModel(hashMap);
        loginModelCall.enqueue(new Callback<UpdateProductsModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateProductsModel> call, @NonNull Response<UpdateProductsModel> response) {
                UpdateProductsModel object = response.body();
                hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                    assert object != null;
                    Toast.makeText(AddProductsActivity.this, object.getMessage() + "", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddProductsActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateProductsModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
                Log.e("Login_Response", t.getMessage() + "");
            }
        });


    }
}