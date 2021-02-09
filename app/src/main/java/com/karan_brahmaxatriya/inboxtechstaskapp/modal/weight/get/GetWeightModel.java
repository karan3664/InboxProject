
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.weight.get;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWeightModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("weight")
    @Expose
    private ArrayList<Weight> weight = null;

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

    public ArrayList<Weight> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Weight> weight) {
        this.weight = weight;
    }

}
