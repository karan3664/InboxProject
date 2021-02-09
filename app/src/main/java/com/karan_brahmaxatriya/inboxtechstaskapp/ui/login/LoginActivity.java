package com.karan_brahmaxatriya.inboxtechstaskapp.ui.login;

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
import com.karan_brahmaxatriya.inboxtechstaskapp.MainActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.R;
import com.karan_brahmaxatriya.inboxtechstaskapp.api.RetrofitHelper;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.login.CheckLoginModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.dashboard.DashboardActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.ui.register.RegisterActivity;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.PrefUtils;
import com.karan_brahmaxatriya.inboxtechstaskapp.utils.ViewDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText etUserMobile, etUserPassword;
    Button btnLogin;
    ViewDialog viewDialog;
    private View parent_view;
    TextView tvCreateAccount;
    CheckLoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);
        viewDialog = new ViewDialog(this);
        viewDialog.setCancelable(false);
        etUserMobile = findViewById(R.id.etUserMobile);
        etUserPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvCreateAccount.setOnClickListener(this);
        loginModel = PrefUtils.getUser(this);
        try {
            if (loginModel.getMessage().matches("Valid user")) {
                Intent ia = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(ia);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                LoginCall();
                break;
            case R.id.tvCreateAccount:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    protected void hideProgressDialog() {
        viewDialog.dismiss();
    }

    protected void showProgressDialog() {
        viewDialog.show();
    }

    public void LoginCall() {

        final String user_mobile = etUserMobile.getText().toString().trim();
        final String user_password = etUserPassword.getText().toString().trim();

        final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";
        if (user_mobile.isEmpty()) {
            etUserMobile.setError("Mobile Required...");
            etUserMobile.requestFocus();
            return;
        } else if (!user_mobile.matches(regexStr)) {
            etUserMobile.setError("Enter Valid Mobile...");
            etUserMobile.requestFocus();
            return;
        } else if (user_password.isEmpty()) {
            etUserPassword.setError("Password Required...");
            etUserPassword.requestFocus();
            return;
        } else {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("user_mobile", user_mobile + "");
            hashMap.put("user_password", user_password + "");

            showProgressDialog();
            Call<CheckLoginModel> loginModelCall = RetrofitHelper.createService(RetrofitHelper.Service.class).checkLogin(hashMap);
            loginModelCall.enqueue(new Callback<CheckLoginModel>() {

                @Override
                public void onResponse(@NonNull Call<CheckLoginModel> call, @NonNull Response<CheckLoginModel> response) {
                    CheckLoginModel object = response.body();
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        Log.e("TAG", "Login_Response : " + new Gson().toJson(response.body()));
//                    labelNotification.setText(object.getResultMarque() + "");
                        if (object.getStatus() == 400) {
                            Toast.makeText(LoginActivity.this, object.getMessage() + "", Toast.LENGTH_LONG).show();

                        } else {
                            PrefUtils.setUser(object, LoginActivity.this);
                            Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }


                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(LoginActivity.this, jObjError.getString("error") + "", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CheckLoginModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    t.printStackTrace();
                    Log.e("Login_Response", t.getMessage() + "");
                }
            });

        }
    }
}