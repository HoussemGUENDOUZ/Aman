package com.example.aman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmergencyCaseAdapter extends RecyclerView.Adapter<EmergencyCaseAdapter.ViewHolder> {
    private Context context;
    private List<EmergencyCase> emergencyCases;
    private SimpleDateFormat dateFormat;

String pathTransformed;
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
        pathTransformed= emergencyCase.getImage();
        // You can set up the more info button here if needed
        holder.moreInfoButton.setOnClickListener(v -> {
//            Intent intent = new Intent(holder.itemView.getContext(), HomeEuaDetailsActivity.class);
//            intent.putExtra("case_id", emergencyCase.getUser_id());
//            intent.putExtra("case_time", emergencyCase.getTime());
//            intent.putExtra("case_type", emergencyCase.getType());
//            intent.putExtra("case_description", emergencyCase.getDescription());
            //intent.putExtra("case_image", emergencyCase.getImage());
            fetchAndSendImages(emergencyCase);
           // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // holder.itemView.getContext().startActivity(intent);
        });
    }
    private void fetchAndSendImages(EmergencyCase emergencyCase) {
        // Implement logic to fetch images and send paths to HomeEuaDetailsActivity
        // For example, you could use Firebase Storage to fetch images and filter based on the required pattern

        // Dummy example to demonstrate sending paths
        List<String> imagePaths = fetchImagePathsFromFirebase();
        Intent intent = new Intent(context, HomeEuaDetailsActivity.class);
        intent.putExtra("case_id", emergencyCase.getUser_id());
        intent.putExtra("case_time", emergencyCase.getTime());
        intent.putExtra("case_type", emergencyCase.getType());
        intent.putExtra("case_description", emergencyCase.getDescription());
        intent.putExtra("case_image", emergencyCase.getImage()); // Send array of paths
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private List<String> fetchImagePathsFromFirebase() {
        // Implement actual logic to fetch image paths from Firebase Storage
        // Query Firebase Storage for images under "emergencyPhotos" and filter paths based on your criteria
        // Return a list of paths that match the pattern "JPEG_123456_12344"

        // Dummy implementation
        List<String> imagePaths = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("emergencyPhotos");

        // List all images under "emergencyPhotos" directory
        storageRef.listAll()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Iterate through each image
                        for (StorageReference item : task.getResult().getItems()) {
                            String path = item.getPath(); // Get full path of the image
                            // Check if the image path starts with "JPEG_123456_12344"
//                            if (path.startsWith(pathTransformed)) {
//                                // Add the image path to the list
//                                imagePaths.add(path);
//                            }
                        }
                        // Notify RecyclerView adapter of changes if needed
                        notifyDataSetChanged();
                    } else {
                        // Handle unsuccessful fetching
                        // Log error or show a message
                    }
                });

        return imagePaths;
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
