package com.example.tp72;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.POJOViewHolder> {
    private List<Teacher> teacherList;

    public TeacherAdapter(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public POJOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new POJOViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull POJOViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.nameTextView.setText(teacher.getNom());
        holder.emailTextView.setText(teacher.getEmail());

        // Register the context menu and use MenuInflater (not LayoutInflater)
        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            // Use MenuInflater to inflate the context menu XML here
            MenuInflater inflater = ((Activity) v.getContext()).getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu); // context_menu.xml file

            // Optionally, handle custom logic for each menu item
            menu.findItem(R.id.action_delete).setOnMenuItemClickListener(item -> {
                // Call a method to remove the item
                removeItem(position);
                return true;
            });
        });
    }
    public void updateTeachers(List<Teacher> newTeacherList) {
        // Update the teacher list
        teacherList.clear();
        teacherList.addAll(newTeacherList);

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    // Method to sort teachers by name in ascending order
    public void sortByName() {
        Collections.sort(teacherList, new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                return t1.getNom().compareTo(t2.getNom());
            }
        });
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    // Method to sort teachers by name in descending order
    public void reverseByName() {
        Collections.sort(teacherList, new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                return t2.getNom().compareTo(t1.getNom());
            }
        });
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    // Method to add a teacher to the list
    public void addTeacher(Teacher teacher) {
        teacherList.add(teacher);
        notifyItemInserted(teacherList.size() - 1);
    }

    // Method to remove a teacher from the list
    public void removeItem(int position) {
        teacherList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, teacherList.size());
    }

    public class POJOViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView nameTextView;
        TextView emailTextView;

        public POJOViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_teacher_name);
            emailTextView = itemView.findViewById(R.id.text_teacher_email);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = new MenuInflater(view.getContext());
            inflater.inflate(R.menu.context_menu, contextMenu);
        }
    }
}