
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Unit {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("active")
    @Expose
    private String active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
