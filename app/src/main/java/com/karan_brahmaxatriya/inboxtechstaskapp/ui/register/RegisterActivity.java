package com.karan_brahmaxatriya.inboxtechstaskapp.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.register.add_users.AddUsersModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission_group.CAMERA;
import static android.os.Build.VERSION_CODES.M;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etUserFname, etUserLname, etUserMobile, etUserPassword, etUserAddress, etUserCity, etUserRole;
    ImageView imageViewProfile, imageView;
    Button btnRegister;
    ViewDialog viewDialog;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    private static final int IMAGE_REQUEST = 1;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    private File photoFile = null;
    File profileImage = null;
    private View parent_view;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        parent_view = findViewById(android.R.id.content);
        viewDialog = new ViewDialog(this);
        viewDialog.setCancelable(false);
        etUserFname = findViewById(R.id.etUserFname);
        etUserLname = findViewById(R.id.etUserLname);
        etUserMobile = findViewById(R.id.etUserMobile);
        etUserPassword = findViewById(R.id.etPassword);
        etUserAddress = findViewById(R.id.etUserAddress);
        etUserCity = findViewById(R.id.etUserCity);
        etUserRole = findViewById(R.id.etUserRole);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageView = findViewById(R.id.imageView);
        btnRegister = findViewById(R.id.btnRegister);
        requestMultiplePermissions();
        btnRegister.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        etUserRole.setOnClickListener(this);


    }

    protected void hideProgressDialog() {
        viewDialog.dismiss();
    }

    protected void showProgressDialog() {
        viewDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                CreateUser();
                break;
            case R.id.imageViewProfile:
                selectImageProfile();
                break;
            case R.id.etUserRole:
                selectRole(v);
                break;
        }
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(RegisterActivity.this)
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
                        Toast.makeText(RegisterActivity.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void selectImageProfile() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void selectRole(final View v) {
        final String[] array = new String[]{
                "Admin", "Customer"
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select Role");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void CreateUser() {
        final String user_fname = etUserFname.getText().toString().trim();
        final String user_lname = etUserLname.getText().toString().trim();
        final String user_mobile = etUserMobile.getText().toString();
        final String user_password = etUserPassword.getText().toString().trim();
        final String user_address = etUserAddress.getText().toString().trim();
        final String user_city = etUserCity.getText().toString().trim();
        final String user_role_id = etUserRole.getText().toString().trim();
        final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";

        if (user_fname.isEmpty()) {
            etUserFname.setError("First Name Required...");
            etUserFname.requestFocus();
        } else if (user_lname.isEmpty()) {
            etUserLname.setError("Last Name Required...");
            etUserLname.requestFocus();
        } else if (user_mobile.isEmpty()) {
            etUserMobile.setError("Mobile Number Required...");
            etUserMobile.requestFocus();
        } else if (!user_mobile.matches(regexStr)) {
            etUserMobile.setError("Enter Valid Mobile...");
            etUserMobile.requestFocus();
            return;
        } else if (user_password.isEmpty()) {
            etUserPassword.setError("Password Required...");
            etUserPassword.requestFocus();
        } else if (user_address.isEmpty()) {
            etUserAddress.setError("Address Required...");
            etUserAddress.requestFocus();
        } else if (user_city.isEmpty()) {
            etUserCity.setError("City Required...");
            etUserCity.requestFocus();
        } else if (user_role_id.isEmpty()) {
            etUserRole.setError("Role Required...");
            etUserRole.requestFocus();
        } else if (imageView == null) {
            Snackbar.make(parent_view, "Profile Image Required...", Snackbar.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            Map<String, String> hashMap = new HashMap<>();


            RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), user_fname + "");
            RequestBody lname = RequestBody.create(MediaType.parse("text/plain"), user_lname + "");
            RequestBody mob = RequestBody.create(MediaType.parse("text/plain"), user_mobile + "");
            RequestBody pass = RequestBody.create(MediaType.parse("text/plain"), user_password + "");
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), user_address + "");
            RequestBody city = RequestBody.create(MediaType.parse("text/plain"), user_city + "");
            RequestBody roleA = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody roleC = RequestBody.create(MediaType.parse("text/plain"), "2");

            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");


            hashMap.put("user_fname", user_fname + "");
            hashMap.put("user_lname", user_lname + "");
            hashMap.put("user_mobile", user_mobile + "");
            hashMap.put("user_password", user_password + "");
            hashMap.put("user_address", user_address + "");
            hashMap.put("user_city", user_city + "");
            hashMap.put("user_image", image + "");
            if (etUserRole.getText().toString().matches("Admin")) {
                hashMap.put("user_role_id", "1");
            } else {
                hashMap.put("user_role_id", "2");
            }


            RequestBody thumbnailimage3 = null;
            try {
//                assert file2 != null;
//                assert fileImage2 != null;
                thumbnailimage3 = RequestBody.create(MediaType.parse("*/*"), profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", profileImage.getName(), fbody);
            MultipartBody.Part body = null;

            if (profileImage != null) {
                String fileExtension
                        = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(profileImage).toString());
                String mimeType
                        = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                RequestBody fbody = RequestBody.create(MediaType.parse(mimeType), profileImage);
                body = MultipartBody.Part.createFormData("user_image", profileImage.getName(), fbody);

//                hashMap.put("user_image\";  filename=\"" + profileImage.getName() + "\"", thumbnailimage3 + "");
            } else {

//                hashMap.put("user_image", attachmentEmpty + "" );
//               body = MultipartBody.Part.createFormData("file", profileImage.getName(), fbody);

            }


            Log.e("Params", hashMap.values() + "");

            Call<AddUsersModel> registerModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).AddUsersModel(hashMap);
            registerModelCall.enqueue(new Callback<AddUsersModel>() {

                @Override
                public void onResponse(@NonNull Call<AddUsersModel> call, @NonNull Response<AddUsersModel> response) {
                    AddUsersModel object = response.body();
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        Log.e("TAG", "Register_Response : " + new Gson().toJson(response.body()));
                        Snackbar.make(parent_view, object.getMessage() + "", Snackbar.LENGTH_SHORT).show();
                        etUserFname.setText("");
                        etUserLname.setText("");
                        etUserMobile.setText("");
                        etUserPassword.setText("");
                        etUserAddress.setText("");
                        etUserCity.setText("");
                        etUserRole.setText("");
                        imageView.setImageBitmap(null);


                    } else {

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            Snackbar.make(parent_view, jObjError.getString("errors") + "", Snackbar.LENGTH_SHORT).show();

//                                Toast.makeText(EditProfileActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AddUsersModel> call, @NonNull Throwable t) {

                    t.printStackTrace();
//                    Snackbar.make(parent_view, t.getMessage() + "", Snackbar.LENGTH_SHORT).show();

                    hideProgressDialog();
                    Log.e("Register_Response", t.getMessage() + "");
                }
            });
        }
    }
}