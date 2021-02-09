
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weight {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("active")
    @Expose
    private String active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
