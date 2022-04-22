package com.example.admin.pimsbs.models;

public class Products {
    private String productbarcode;
    private String productname;
    private String productcategory;
    private String productprice;
    private String productquantity;

    public Products() {

    }

    public Products(String productname, String productcategory, String productprice, String productquantity){
        this.productname=productname;
        this.productcategory=productcategory;
        this.productprice= productprice;
        this.productquantity = productquantity;
    }

    public Products(String productbarcode, String productname, String productcategory, String productprice, String productquantity){

        this.productbarcode=productbarcode;
        this.productname=productname;
        this.productcategory=productcategory;
        this.productprice= productprice;
        this.productquantity = productquantity;
    }

    public void setProductBarcode(String productbarcode) {
        this.productbarcode = productbarcode;
    }

    public void setProductName(String productname) {
        this.productname = productname;
    }

    public void setProductCategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public void setProductPrice(String productprice) {
        this.productprice = productprice;
    }

    public void setProductQuantity(String productquantity) {
        this.productquantity = productquantity;
    }

    public String getProductBarcode() {
        return productbarcode;
    }

    public String getProductName() {
        return productname;
    }

    public String getProductCategory() {
        return productcategory;
    }

    public String getProductPrice() {
        return productprice;
    }

    public String getProductQuantity() {
        return productquantity;
    }

}