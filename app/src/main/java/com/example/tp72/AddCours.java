    package com.example.tp72;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Build;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;
    import android.widget.Spinner;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.core.app.NotificationCompat;
    import androidx.fragment.app.Fragment;

    import java.util.ArrayList;
    import java.util.List;

    public class AddCours extends Fragment {

        private EditText nomCours, nbHeures;
        private RadioGroup typeRadioGroup;
        private Spinner spinnerEnseignant;
        private Button btnGreen, btnRed;

        public AddCours() {
            // Required empty public constructor
        }

        public static AddCours newInstance() {
            return new AddCours();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_add_cours, container, false);

            // Initialize Views
            nomCours = view.findViewById(R.id.nomC);
            nbHeures = view.findViewById(R.id.nbH);
            typeRadioGroup = view.findViewById(R.id.rdg);
            spinnerEnseignant = view.findViewById(R.id.spinner);
            btnGreen = view.findViewById(R.id.btnGreen);
            btnRed = view.findViewById(R.id.btnRed);

            // Populate the spinner with the list of teachers from the database
            populateTeacherSpinner();

            // Add course button click listener
            btnGreen.setOnClickListener(v -> {
                String nomCoursValue = nomCours.getText().toString().trim();
                String nbHeuresValue = nbHeures.getText().toString().trim();
                int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
                String typeValue = selectedTypeId == R.id.cours ? "Cours" : "Atelier";

                if (nomCoursValue.isEmpty() || nbHeuresValue.isEmpty() || selectedTypeId == -1) {
                    Toast.makeText(getContext(), "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int heures;
                try {
                    heures = Integer.parseInt(nbHeuresValue);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid number of hours!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Teacher selectedTeacher = (Teacher) spinnerEnseignant.getSelectedItem();
                if (selectedTeacher == null) {
                    Toast.makeText(getContext(), "Please select a teacher!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create the Cours object
                Cours cours = new Cours();
                cours.setName(nomCoursValue);
                cours.setHours(heures);
                cours.setType(typeValue);
                cours.setTeacher_id(selectedTeacher.getId()); // Set the Teacher's ID

                // Add the course to the database
                gererDB gererDB = new gererDB(getActivity());
                try {
                    if (gererDB.addCours(cours) != -1) {
                        Toast.makeText(getActivity(), "Course added successfully", Toast.LENGTH_LONG).show();
                        showNotification(cours.getName());
                    } else {
                        Toast.makeText(getActivity(), "Failed to add course", Toast.LENGTH_LONG).show();
                    }
                } finally {
                    gererDB.fermer(); // Ensure database is closed
                }

                // Reset fields
                nomCours.setText("");
                nbHeures.setText("");
                typeRadioGroup.clearCheck();
                spinnerEnseignant.setSelection(0);
            });

            // Back button click listener
            btnRed.setOnClickListener(v -> {
                ListeCours listeCoursFragment = new ListeCours();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, listeCoursFragment) // Adjust 'fragment_container' to match your activity's container ID
                        .addToBackStack(null) // Optional: Allows the user to navigate back
                        .commit();
            });

            return view;
        }

        private void populateTeacherSpinner() {
            gererDB gererDB = new gererDB(getActivity());
            List<Teacher> teacherList;
            try {
                teacherList = gererDB.getTeacherList();
            } finally {
                gererDB.fermer(); // Ensure the database connection is closed
            }

            if (teacherList == null || teacherList.isEmpty()) {
                Toast.makeText(getContext(), "No teachers available. Please add teachers first.", Toast.LENGTH_LONG).show();
            } else {
                ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teacherList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEnseignant.setAdapter(adapter);
            }
        }



        private void showNotification(String courseName) {
            String channelId = "course_notification_channel";
            String channelName = "Course Notifications";
            int notificationId = 1;

            // Create NotificationManager
            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            // For Android 8.0 and above, create a notification channel
            if (Build.VERSION.SDK_INT > 0) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Notifications for course addition");
                notificationManager.createNotificationChannel(channel);
            }

            // Intent to open the app when the notification is clicked
            Intent intent = new Intent(requireContext(), MainActivity.class); // Change MainActivity to your activity class
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Create the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                    .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
                    .setContentTitle("New Course Added")
                    .setContentText("The course '" + courseName + "' has been added successfully.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            // Show the notification
            notificationManager.notify(notificationId, builder.build());
        }

    }
