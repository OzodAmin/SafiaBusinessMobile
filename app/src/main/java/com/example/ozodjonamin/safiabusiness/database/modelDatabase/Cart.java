package com.example.ozodjonamin.safiabusiness.database.modelDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "product_id")
    public int product_id;

    @ColumnInfo(name = "name")
    public String productName;

    @ColumnInfo(name = "image")
    public String productImage;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "price")
    public double price;
}
