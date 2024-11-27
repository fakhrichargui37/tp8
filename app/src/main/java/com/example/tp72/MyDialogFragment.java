package com.example.tp72;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    public static MyDialogFragment newInstance(TeacherAdapter teacherAdapter) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.setOnTeacherAddedListener(teacher -> {
            // Add the new teacher to the adapter and notify changes
            teacherAdapter.addTeacher(teacher);
            teacherAdapter.notifyDataSetChanged();
        });
        return fragment;
    }


    // Listener interface for notifying teacher addition
    interface OnTeacherAddedListener {
        void onTeacherAdded(Teacher teacher);
    }

    private OnTeacherAddedListener listener;

    public void setOnTeacherAddedListener(OnTeacherAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.layout_add_dialog, null);
        builder.setView(dialogView);

        // Initialize input fields
        final EditText nameEditText = dialogView.findViewById(R.id.edit_text);
        final EditText emailEditText = dialogView.findViewById(R.id.email);

        // Configure dialog buttons
        builder.setTitle("Ajouter Nouveau Enseignant")
                .setPositiveButton("Valider", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();

                    if (name.isEmpty() || email.isEmpty()) {
                        Toast.makeText(requireContext(), "Tous les champs sont obligatoires !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    gererDB db = new gererDB(requireContext());
                    Teacher teacher = new Teacher(name, email);
                    try {
                        long result = db.addTeacher(teacher);
                        if (result != -1) {
                            teacher.setId((int) result);
                            if (listener != null) {
                                listener.onTeacherAdded(teacher);
                            }
                            Toast.makeText(requireContext(), "Enseignant ajouté avec succès.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de l'ajout.", Toast.LENGTH_SHORT).show();
                        }
                    } finally {
                        db.fermer();
                    }
                }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
