
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OldPrice {

    @SerializedName("product_old_price")
    @Expose
    private String productOldPrice;

    public String getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(String productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

}
