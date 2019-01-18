package com.example.ozodjonamin.safiabusiness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.example.ozodjonamin.safiabusiness.adapter.CategoryAdapter;
import com.example.ozodjonamin.safiabusiness.database.dataSource.CartRepository;
import com.example.ozodjonamin.safiabusiness.database.local.CartDataSource;
import com.example.ozodjonamin.safiabusiness.database.local.CartDatabase;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.manager.UserManager;
import com.example.ozodjonamin.safiabusiness.model.Category;
import com.example.ozodjonamin.safiabusiness.model.User;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    TextView userName, userEmail;
    NotificationBadge badge;
    ApiService service;
    @BindView(R.id.fab)
    CounterFab fab;
    UserManager userManager;
    TokenManager tokenManager;
    Call<User> callUser;
    Call<List<Category>> callCategory;
    RecyclerView listCategories;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        userManager = UserManager.getInstance(getSharedPreferences("user", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        getUser();
        getCategories();
        //Initialise database
        initDB();
        updateCartCount();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @OnClick(R.id.fab)
    void showCartAcrivity() {
        startActivity(new Intent(MainActivity.this, CartActivity.class));
    }

    private void initDB() {
        Common.cartDatabase = CartDatabase.getInsstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
    }

    private void getUser() {
        callUser = service.getUserInformation();
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.w(TAG, "onResponse: " + response);
                userManager.saveUser(response.body());
                View headerView = navigationView.getHeaderView(0);
                userName = (TextView) headerView.findViewById(R.id.userName);
                userEmail = (TextView) headerView.findViewById(R.id.userEmail);
                userName.setText(response.body().getName());
                userEmail.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getCategories() {
        listCategories = (RecyclerView) findViewById(R.id.list_category);
        listCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listCategories.setHasFixedSize(true);

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("App_Lang", "ru");

        callCategory = service.categories(language);
        callCategory.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log.w(TAG, "onResponse: " + response);
                displayCategories(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listCategories.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void updateCartCount() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0)
                    fab.setCount(0);
                else {
                    fab.setCount(Common.cartRepository.countCartItems());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_cart) {
            showCartAcrivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}
