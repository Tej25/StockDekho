package vit.project.stock_dekho2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    Context context;
    ArrayList<CompanyData> companyDataList;

    public CompanyAdapter(Context context, ArrayList<CompanyData> companyData) {
        this.context = context;
        this.companyDataList = companyData;
    }

    @NonNull
    @Override
    public CompanyAdapter.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.CompanyViewHolder holder, int position) {
        CompanyData companyData = companyDataList.get(position);

        holder.companyName.setText(companyData.companySymbol);
        holder.companyData = companyData;
        try{
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ABC","OnClickCompany" + companyData.companySymbol);
                }
            });
        }
        catch (Exception e){
            Log.d("ABC",e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return companyDataList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder{

        TextView companyName;
        CompanyData companyData;
        RelativeLayout parentLayout;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.itemName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
