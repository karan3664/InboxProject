
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.categories.get_categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("c_id")
    @Expose
    private String cId;
    @SerializedName("c_name")
    @Expose
    private String cName;
    @SerializedName("active")
    @Expose
    private String active;

    public Category(String select_insurance_company) {
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
