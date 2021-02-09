
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.units.get;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUnitsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("unit")
    @Expose
    private ArrayList<Unit> unit = null;

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

    public ArrayList<Unit> getUnit() {
        return unit;
    }

    public void setUnit(ArrayList<Unit> unit) {
        this.unit = unit;
    }

}
