package vit.project.stock_dekho2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*----------Variables----------*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<CategoryData> categoryDataArrayList;
    CategoryAdapter categoryAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data....");
        progressDialog.show();

        /*----------Hooks----------*/
        drawerLayout = findViewById(R.id.MainDrawerLay);
        navigationView = findViewById(R.id.MainNavBar);
        toolbar = findViewById(R.id.ActionBar);
        recyclerView = findViewById(R.id.categoryRecyclerView);
        db = FirebaseFirestore.getInstance();
        categoryDataArrayList = new ArrayList<CategoryData>();
        categoryAdapter = new CategoryAdapter(MainActivity.this,categoryDataArrayList);

        /*----------RecyclerView----------*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryAdapter);

        /*----------ActionBar----------*/
        toolbar.bringToFront();
        setSupportActionBar(toolbar);

        /*----------NavigationBar----------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle =new ActionBarDrawerToggle(
                MainActivity.this,drawerLayout,toolbar,R.string.NavDrawerOpen,R.string.NavDrawerClose);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        /*----------DataFetching------*/
        dataFetching();
    }

    private void dataFetching() {
        db.collection("category").orderBy("cat", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("ABFirebase",error.getMessage());
                            return;
                        }
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                categoryDataArrayList.add(documentChange.getDocument().toObject(CategoryData.class));
                            }
                            categoryAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.NavResetPassword:
                startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class));
                break;
            case R.id.NavLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return true;
    }
}