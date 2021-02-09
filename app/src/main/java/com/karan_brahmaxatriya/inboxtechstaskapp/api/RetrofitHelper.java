package com.karan_brahmaxatriya.inboxtechstaskapp.api;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.AddCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.DeleteCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.add_categories.UpdateCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories.GetCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.login.CheckLoginModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.AddProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.DeleteProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.add.UpdateProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get.GetProductsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.register.add_users.AddUsersModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.AddSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.DeleteSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.add.UpdateSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub.GetSubCategoriesModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.add.AddUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.add.DeleteUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.add.UpdateUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.get.GetUnitsModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.add.AddWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.add.DeleteWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.add.UpdateWeightModel;
import com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.get.GetWeightModel;


import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;


/**
 * Created by Karan Brahmaxatriya on 02-Feb-21.
 */
public class RetrofitHelper {
    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit, retrofitMatchScore;
    public static CookieManager cookieManager;
    private Context mContext;

    public RetrofitHelper(Context context) {
        mContext = context;
    }

    public static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient != null) {
            return okHttpClient;
        }


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(300, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
              /*  if (!Connectivity.isConnected(Betx11Application.getContext())) {
                    throw new NoConnectivityException();
                }*/
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder(); // Add Device Detail

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                String requestedHost = request.url().host();
                assert response.networkResponse() != null;
                String responseHost = response.networkResponse().request().url().host();
                if (!requestedHost.equalsIgnoreCase(responseHost)) {
                    throw new NoConnectivityException();
                }


                return response;
            }
        });
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConstants.CURRENT_REST_URL)
                    .client(getOkHttpClientInstance())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }
        return retrofit.create(serviceClass);
    }


    public interface Service {

        /************************************************************************************************************************************************/

        @FormUrlEncoded
        @POST("addUser")
        Call<AddUsersModel> addUser(@Body HashMap<String, String> hashMap);

        /************************************************************************************************************************************************/

        //        @FormUrlEncoded
        @POST("checkLogin")
        Call<CheckLoginModel> checkLogin(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        //        @Multipart
        @POST("addUser")
        Call<AddUsersModel> AddUsersModel(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        @GET("getCategories")
        Call<GetCategoriesModel> GetCategoriesModel();

        @POST("addCategory")
        Call<AddCategoriesModel> AddCategoriesModel(@Body Map<String, String> body);

        @PUT("updateCategory")
        Call<UpdateCategoriesModel> UpdateCategoriesModel(@Body Map<String, String> body);

        @PUT("deleteCategory")
        Call<DeleteCategoriesModel> DeleteCategoriesModel(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        @GET("getSubCategories")
        Call<GetSubCategoriesModel> GetSubCategoriesModel();

        @POST("addSubCategory")
        Call<AddSubCategoriesModel> AddSubCategoriesModel(@Body Map<String, String> body);

        @POST("getSubCategoryById")
        Call<GetSubCategoriesModel> GetSubCategoriesModelByID(@Body Map<String, String> body);

        @PUT("updateSubCategory")
        Call<UpdateSubCategoriesModel> UpdateSubCategoriesModel(@Body Map<String, String> body);

        @PUT("deleteSubCategory")
        Call<DeleteSubCategoriesModel> DeleteSubCategoriesModel(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        @GET("getUnit")
        Call<GetUnitsModel> GetUnitsModel();

        @POST("addUnit")
        Call<AddUnitsModel> AddUnitsModel(@Body Map<String, String> body);

        @PUT("updateUnit")
        Call<UpdateUnitsModel> UpdateUnitsModel(@Body Map<String, String> body);

        @PUT("deleteUnit")
        Call<DeleteUnitsModel> DeleteUnitsModel(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        @GET("getWeight")
        Call<GetWeightModel> GetWeightModel();

        @POST("addWeight")
        Call<AddWeightModel> AddWeightModel(@Body Map<String, String> body);

        @PUT("updateWeight")
        Call<UpdateWeightModel> UpdateWeightModel(@Body Map<String, String> body);

        @PUT("deleteWeight")
        Call<DeleteWeightModel> DeleteWeightModel(@Body Map<String, String> body);

        /************************************************************************************************************************************************/

        @GET("getProducts")
        Call<GetProductsModel> GetProductsModel();

        @POST("addProduct")
        Call<AddProductsModel> AddProductsModel(@Body Map<String, String> body);

        @PUT("updateProduct")
        Call<UpdateProductsModel> UpdateProductsModel(@Body Map<String, String> body);

        @PUT("deleteProduct")
        Call<DeleteProductsModel> DeleteProductsModel(@Body Map<String, String> body);
    }


    public static ArrayList<KeyValueModel> getKeyValueInputData(LinkedHashMap<String, String> hm) {
        ArrayList<KeyValueModel> modelList = new ArrayList<>();
        for (String key : hm.keySet()) {
            KeyValueModel obj = new KeyValueModel();
            obj.setKey(key);
            obj.setValue(hm.get(key));
            modelList.add(obj);
        }
        return modelList;
    }


}