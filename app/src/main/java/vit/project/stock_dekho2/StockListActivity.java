package vit.project.stock_dekho2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {

    String companyName;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<CompanyData> companyDataArrayList;
    CompanyAdapter companyAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock_list);

        Log.d("ABC","Activity created");
        Log.d("ABC","StockList intent coming");

        if(getIntent().hasExtra("catgeoryName")){
            Log.d("ABC","found");
            companyName = getIntent().getStringExtra("catgeoryName");
            Log.d("ABC","STockListPage "+companyName);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Message...");
        progressDialog.show();

        /*----------Hooks----------*/
        recyclerView = (RecyclerView) findViewById(R.id.companyRecycleView);
        db = FirebaseFirestore.getInstance();
        companyDataArrayList = new ArrayList<CompanyData>();
        companyAdapter = new CompanyAdapter(StockListActivity.this,companyDataArrayList);

        /*----------RecyclerView----------*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(companyAdapter);

        dataFetching();
    }

    private void dataFetching() {
        db.collection(companyName).orderBy("companySymbol", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.d("ABC",error.getMessage());
                            return;
                        }
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                companyDataArrayList.add(documentChange.getDocument().toObject(CompanyData.class));
                            }
                            companyAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}