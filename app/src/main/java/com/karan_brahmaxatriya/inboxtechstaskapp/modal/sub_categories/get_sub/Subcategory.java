
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.sub_categories.get_sub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcategory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_id")
    @Expose
    private String cId;
    @SerializedName("s_name")
    @Expose
    private String sName;
    @SerializedName("active")
    @Expose
    private String active;

    public Subcategory(String select_sub_category) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
