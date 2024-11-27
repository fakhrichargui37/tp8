package com.example.tp72;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListeCours#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeCours extends Fragment implements CoursAdapter.CoursAdapterCallback {

    RecyclerView recyclerView;
    CoursAdapter adapter;
    gererDB gererDB;

    public ListeCours() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided list of courses.
     *
     * @param coursList List of courses to display.
     * @return A new instance of fragment ListeCours.
     */
    public static ListeCours newInstance(List<Cours> coursList) {
        ListeCours fragment = new ListeCours();
        Bundle args = new Bundle();
        args.putSerializable("coursList", new java.util.ArrayList<>(coursList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liste_cours, container, false);

        // Initialize the database handler
        gererDB = new gererDB(getContext());

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch the list of courses from arguments or database
        List<Cours> coursList;
        if (getArguments() != null) {
            coursList = (List<Cours>) getArguments().getSerializable("coursList");
        } else {
            coursList = gererDB.getCoursList();
        }

        // Initialize and set the adapter
        adapter = new CoursAdapter(getActivity(), coursList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Inflate the menu and add it to the action bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cources, menu); // Inflate the menu layout
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            editCourse();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            deleteCourse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Logic for editing a course
    private void editCourse() {
        Toast.makeText(getContext(), "Select a course to edit from the list.", Toast.LENGTH_SHORT).show();
    }

    // Logic for deleting a course
    private void deleteCourse() {
        Toast.makeText(getContext(), "Select a course to delete from the list.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditCours(Cours cours) {
        // Inflate a custom layout for editing the course
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_cours, null);

        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editHours = dialogView.findViewById(R.id.editHours);
        EditText editTeacher = dialogView.findViewById(R.id.editTeacher);
        EditText editType = dialogView.findViewById(R.id.editType);

        // Populate existing data into the dialog fields
        editName.setText(cours.getName());
        editHours.setText(String.valueOf(cours.getHours()));
        editTeacher.setText(cours.getTeacher());
        editType.setText(cours.getType());

        // Show the dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Edit Course")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        // Validate and update course with new values
                        cours.setName(editName.getText().toString());
                        cours.setHours(Integer.parseInt(editHours.getText().toString()));
                        cours.setTeacher(editTeacher.getText().toString());
                        cours.setType(editType.getText().toString());

                        // Update in database
                        gererDB.updateCours(cours);

                        // Refresh RecyclerView
                        List<Cours> updatedList = gererDB.getCoursList();
                        adapter.updateData(updatedList);

                        Toast.makeText(getContext(), "Course updated successfully.", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid input. Please check your data.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDeleteCours(Cours cours) {
        // Show confirmation dialog before deleting
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete " + cours.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete from database
                    gererDB.deleteCours(cours.getId());

                    // Refresh RecyclerView
                    List<Cours> updatedList = gererDB.getCoursList();
                    adapter.updateData(updatedList);

                    Toast.makeText(getContext(), "Course deleted successfully.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
