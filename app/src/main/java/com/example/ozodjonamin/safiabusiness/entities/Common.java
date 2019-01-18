package com.example.ozodjonamin.safiabusiness.entities;

import com.example.ozodjonamin.safiabusiness.database.dataSource.CartRepository;
import com.example.ozodjonamin.safiabusiness.database.local.CartDatabase;
import com.example.ozodjonamin.safiabusiness.model.Category;

public class Common {
    //Database
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;

    public static Category currentCategory = null;

    // URL FOR EMULATOR
    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
    public static final String CATEGORY_IMAGE_URL = "http://10.0.2.2:8080/uploads/category/";
    public static final String PRODUCT_IMAGE_URL = "http://10.0.2.2:8080/uploads/product/";
    public static final String PRODUCT_THUMB_IMAGE_URL = "http://10.0.2.2:8080/uploads/product/admin_";

    // URL FOR MOBILE
//    public static final String BASE_URL = "http://192.168.1.103:8080/api/";
//    public static final String CATEGORY_IMAGE_URL = "http://192.168.1.103:8080/uploads/category/";
//    public static final String PRODUCT_IMAGE_URL = "http://192.168.1.103:8080/uploads/product/";
}
