
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quantity {

    @SerializedName("product_quantity")
    @Expose
    private String productQuantity;

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

}
