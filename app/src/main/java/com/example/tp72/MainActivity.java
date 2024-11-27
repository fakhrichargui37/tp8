package com.example.tp72;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tp72.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Menu menu;
    private ActivityMainBinding binding;
    private TeacherAdapter teacherAdapter;
    private boolean enseig = false;
    private boolean about = false;
    private boolean courceAbout = false;
    private gererDB gererDB;
    // Add a reference to your database helper class
    private  SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        setTitle("My app");


        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the database helper
        gererDB = new gererDB(this); // Replace with your actual database helper class

        // Setup NavigationView and RecyclerView
        binding.navView.setNavigationItemSelectedListener(this);
        RecyclerView rv = findViewById(R.id.mRecyclerview);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        // Initialize the adapter with an empty list
        teacherAdapter = new TeacherAdapter(new ArrayList<>());
        rv.setAdapter(teacherAdapter);

        // Fetch teachers from the database
        loadTeachersFromDatabase();

        // Drawer toggle setup
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initial fragment setup
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
            binding.navView.setCheckedItem(R.id.nav_home);
        }
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

    }

    // Method to load teacher data from the database and update the RecyclerView
    private void loadTeachersFromDatabase() {
        List<Teacher> teacherList = gererDB.getTeacherList();  // Replace with actual method to fetch teachers
        teacherAdapter.updateTeachers(teacherList);  // Update the adapter with the data
    }

    public void toggleRecyclerViewVisibility() {
        RecyclerView recyclerView = findViewById(R.id.mRecyclerview);  // Use the correct ID
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE); // Hide RecyclerView
        } else {
            recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_about, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear(); // Clear the existing menu items
        // Dynamically load menu based on selected fragment
        if (enseig) {
            getMenuInflater().inflate(R.menu.main_enseig, menu);
        } else if (about) {
            getMenuInflater().inflate(R.menu.main_about, menu);
        }else if(courceAbout){
            getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Switch fragments based on selected item and update flags
        if (item.getItemId() == R.id.nav_home) {
            binding.toolbar.setTitle("Enseignants");
            enseig = true;
            about = false;
            invalidateOptionsMenu();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_about) {
            binding.toolbar.setTitle("About");
            enseig = false;
            about = true;
            invalidateOptionsMenu();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (item.getItemId() == R.id.nav_logout) {
            Log.i("tag", "exit");
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            logout();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish(); // Close the current activity

        } else if (item.getItemId() == R.id.gerer_cours) {
            binding.toolbar.setTitle("Cours");
            courceAbout = true; // Set this flag
            enseig = false; // Reset other flags
            about = false; // Reset other flags
            invalidateOptionsMenu(); // Ensure menu is refreshed
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddCours()).commit();
        }

        // Close drawer after selecting an item
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.a_z) {
            teacherAdapter.sortByName();  // Use the instance teacherAdapter to call sortByName
            return true;
        } else if (item.getItemId() == R.id.z_a) {
            teacherAdapter.reverseByName();  // Use the instance teacherAdapter to call reverseByName
            return true;
        } else if (item.getItemId() == R.id.add) {
            showAddingDialog();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            ajouterCourse();  // Call editCourse when the edit menu item is clicked
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            deleteCourse();  // Call deleteCourse when the delete menu item is clicked
            return true;
        }else if(item.getItemId()== R.id.action_list){
            ListeCours listeCoursFragment = new ListeCours();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, listeCoursFragment) // Adjust 'fragment_container' to match your activity's container ID
                    .addToBackStack(null) // Optional: Allows the user to navigate back
                    .commit();
        }else if(item.getItemId()== R.id.action_search){
            showSearchDialog(); // Handle the search dialog
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Course");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter course name");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String courseName = input.getText().toString().trim();
            if (!courseName.isEmpty()) {
                searchCourse(courseName);
            } else {
                Toast.makeText(this, "Please enter a course name.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.show();
    }

    private void searchCourse(String courseName) {
        // Query the database for the course
        List<Cours> foundCourses = gererDB.searchCourseByName(courseName); // Implement this in your database helper
        if (!foundCourses.isEmpty()) {
            // Show the results in a new fragment or a RecyclerView
            ListeCours listeCoursFragment = ListeCours.newInstance(foundCourses); // Create a fragment to display results
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, listeCoursFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(this, "No courses found with the name: " + courseName, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showAddingDialog() {
        MyDialogFragment dialog = MyDialogFragment.newInstance(teacherAdapter);
        dialog.show(getSupportFragmentManager(), "my_dialog");
    }

    // Method to handle editing a course
    private void ajouterCourse() {
        binding.toolbar.setTitle("Cours");
        courceAbout = true; // Update the flag
        enseig = false;
        about = false; // Ensure other flags are false
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddCours()).commit();
        invalidateOptionsMenu();
    }

    // Method to handle deleting a course
    private void deleteCourse() {
        // Your code to delete a course (e.g., remove the course from the database)
            deleteAllCourses();
    }

    private void deleteAllCourses() {
        // Delete all courses from the database
        gererDB.deleteAllCourses();

        // Update the RecyclerView by clearing the adapter's data
        RecyclerView rv = findViewById(R.id.mRecyclerview);
        if (rv.getAdapter() instanceof CoursAdapter) {
            CoursAdapter coursAdapter = (CoursAdapter) rv.getAdapter();
            coursAdapter.clearAllData(); // Clear all data from the adapter
        }

        // Show a toast message to confirm deletion
        Toast.makeText(this, "All courses have been deleted!", Toast.LENGTH_SHORT).show();
    }
    private void logout() {
        // Clear the login flag from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Show a logout message
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        // Redirect to the LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
        finish(); // Close the current activity
    }

}
