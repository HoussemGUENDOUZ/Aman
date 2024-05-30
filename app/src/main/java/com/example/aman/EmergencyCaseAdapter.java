package com.example.aman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmergencyCaseAdapter extends RecyclerView.Adapter<EmergencyCaseAdapter.ViewHolder> {
    private Context context;
    private List<EmergencyCase> emergencyCases;
    private SimpleDateFormat dateFormat;

    public EmergencyCaseAdapter(Context context, List<EmergencyCase> emergencyCases) {
        this.context = context;
        this.emergencyCases = emergencyCases;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emergency_case, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyCase emergencyCase = emergencyCases.get(position);
        holder.dateTimeTV.setText(dateFormat.format(new Date(emergencyCase.getTime())));
        holder.caseTypeTV.setText(emergencyCase.getType());
        holder.caseDescriptionTV.setText(emergencyCase.getDescription());

        // You can set up the more info button here if needed
        holder.moreInfoButton.setOnClickListener(v -> {
            // Implement what happens when more info is clicked
        });
    }

    @Override
    public int getItemCount() {
        return emergencyCases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTV, caseTypeTV, caseDescriptionTV;
        Button moreInfoButton;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTimeTV = itemView.findViewById(R.id.DateTimeTV);
            caseTypeTV = itemView.findViewById(R.id.CaseTypeTV);
            caseDescriptionTV = itemView.findViewById(R.id.CaseDescriptionTV);
            moreInfoButton = itemView.findViewById(R.id.btnMoreInfo);
        }
    }
}
