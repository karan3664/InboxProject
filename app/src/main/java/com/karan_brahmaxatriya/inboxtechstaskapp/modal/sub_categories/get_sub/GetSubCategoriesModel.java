
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSubCategoriesModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subcategory")
    @Expose
    private ArrayList<Subcategory> subcategory = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

}
