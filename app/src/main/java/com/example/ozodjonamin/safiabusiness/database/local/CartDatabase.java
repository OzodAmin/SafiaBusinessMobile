package com.example.ozodjonamin.safiabusiness.database.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();

    private static CartDatabase instance;

    public static CartDatabase getInsstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, "SafiaBusinessDB")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
