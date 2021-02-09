
package com.karan_brahmaxatriya.inboxtechstaskapp.modal.products.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPrice {

    @SerializedName("product_new_price")
    @Expose
    private String productNewPrice;

    public String getProductNewPrice() {
        return productNewPrice;
    }

    public void setProductNewPrice(String productNewPrice) {
        this.productNewPrice = productNewPrice;
    }

}
