
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("c_name")
    @Expose
    private String cName;
    @SerializedName("s_name")
    @Expose
    private String sName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("old_prices")
    @Expose
    private ArrayList<OldPrice> oldPrices = null;
    @SerializedName("new_prices")
    @Expose
    private ArrayList<NewPrice> newPrices = null;
    @SerializedName("quantities")
    @Expose
    private ArrayList<Quantity> quantities = null;
    @SerializedName("weights")
    @Expose
    private ArrayList<Weight> weights = null;
    @SerializedName("units")
    @Expose
    private ArrayList<Unit> units = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public ArrayList<OldPrice> getOldPrices() {
        return oldPrices;
    }

    public void setOldPrices(ArrayList<OldPrice> oldPrices) {
        this.oldPrices = oldPrices;
    }

    public ArrayList<NewPrice> getNewPrices() {
        return newPrices;
    }

    public void setNewPrices(ArrayList<NewPrice> newPrices) {
        this.newPrices = newPrices;
    }

    public ArrayList<Quantity> getQuantities() {
        return quantities;
    }

    public void setQuantities(ArrayList<Quantity> quantities) {
        this.quantities = quantities;
    }

    public ArrayList<Weight> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Weight> weights) {
        this.weights = weights;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }
}
