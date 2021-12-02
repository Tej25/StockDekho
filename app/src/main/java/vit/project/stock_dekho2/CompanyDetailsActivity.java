package vit.project.stock_dekho2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CompanyDetailsActivity extends AppCompatActivity {

    String companySymbolIntent;
    TextView companyName,symbol,companyPrice,isin,rangePrice,marketCap,primaryExchange;
    ProgressDialog progressDialog;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_company_details);

        companyName = findViewById(R.id.companyName);
        symbol = findViewById(R.id.symbol);
        companyPrice = findViewById(R.id.companyPrice);
        isin = findViewById(R.id.isin);
        rangePrice = findViewById(R.id.rangePrice);
        marketCap = findViewById(R.id.marketCap);
        primaryExchange = findViewById(R.id.primaryExchange);

        Log.d("ABC","Activity created");
        Log.d("ABC","CompanyList intent coming");

        if(getIntent().hasExtra("companySymbol")){
            Log.d("ABC","found");
            companySymbolIntent = getIntent().getStringExtra("companySymbol");
            Log.d("ABC","Company ListPage "+companySymbolIntent);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Message...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();

        datafetching();
    }

    private void datafetching() {
        db.collection(companySymbolIntent).document(companySymbolIntent).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String companyName_firebase = documentSnapshot.getString("companyName");
                            String currentPrice_firebase = documentSnapshot.getString("currentPrice");
                            String isin_firebase = documentSnapshot.getString("isin");
                            String marketCap_firebase = documentSnapshot.getString("marketCap");
                            String primaryExchange_firebase = documentSnapshot.getString("primaryExchange");
                            String rangePrice_firebase = documentSnapshot.getString("rangePrice");

                            companyName.setText(companyName_firebase);
                            symbol.setText(companySymbolIntent);
                            companyPrice.setText(currentPrice_firebase);
                            isin.setText(isin_firebase);
                            marketCap.setText(marketCap_firebase);
                            primaryExchange.setText(primaryExchange_firebase);
                            rangePrice.setText(rangePrice_firebase);

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                        else{
                            Toast.makeText(CompanyDetailsActivity.this,"ERROr",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompanyDetailsActivity.this,"ERROrFailure",Toast.LENGTH_LONG).show();
                        Log.d("ABC",e.getMessage());
                    }
                });
    }
}