package com.example.tp72;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.CoursViewHolder> {
    private final List<Cours> coursList;
    private final Context context;
    private final CoursAdapterCallback callback;

    public interface CoursAdapterCallback {
        void onEditCours(Cours cours); // Callback for editing a course
        void onDeleteCours(Cours cours); // Callback for deleting a course
    }

    public CoursAdapter(Context context, List<Cours> coursList, CoursAdapterCallback callback) {
        this.context = context;
        this.coursList = coursList;
        this.callback = callback;
    }

    public  void clearAllData() {
        this.coursList.clear(); // Clear the current list
        notifyDataSetChanged(); // Notify the adapter about the data change
    }


    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_element, parent, false);
        return new CoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
        Cours cours = coursList.get(position);

        holder.typeCours.setText(cours.getType());
        holder.titreCours.setText(cours.getName());
        holder.nbHeure.setText(String.valueOf(cours.getHours()));

        // Handle Edit Button
        holder.btnEdit.setOnClickListener(v -> {
            if (callback != null) {
                callback.onEditCours(cours);
            } else {
                Toast.makeText(context, "Edit callback not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete Button
        holder.btnDelete.setOnClickListener(v -> {
            if (callback != null) {
                callback.onDeleteCours(cours);
            } else {
                Toast.makeText(context, "Delete callback not implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateData(List<Cours> newCoursList) {
        this.coursList.clear();
        this.coursList.addAll(newCoursList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return coursList.size();
    }

    static class CoursViewHolder extends RecyclerView.ViewHolder {
        TextView typeCours, titreCours;
        EditText nbHeure;
        Spinner enseignantSpinner;
        Button btnEdit, btnDelete;

        public CoursViewHolder(@NonNull View itemView) {
            super(itemView);
            typeCours = itemView.findViewById(R.id.typeCours);
            titreCours = itemView.findViewById(R.id.titreCours);
            nbHeure = itemView.findViewById(R.id.nbHeure);
            enseignantSpinner = itemView.findViewById(R.id.enseignantSpinner);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


}
