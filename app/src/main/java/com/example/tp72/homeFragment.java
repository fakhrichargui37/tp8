package com.example.tp72;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tp72.databinding.FragmentHomeBinding;

public class homeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Set onClickListener for headerList
        binding.headerList.setOnClickListener(onShowPopupMenu());

        return binding.getRoot(); // Return the root view from the binding
    }

    private View.OnClickListener onShowPopupMenu() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create PopupMenu anchored to the clicked view
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(onPopupMenuClickListener(v)); // Pass the clicked view
                popupMenu.inflate(R.menu.menu_pop_up); // Inflate the custom menu
                popupMenu.show(); // Show the popup menu
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener onPopupMenuClickListener(final View clickedView) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click using if statements
                if (item.getItemId() == R.id.action_edit) {
                    // Change the name of the TextView that was clicked
                    if (clickedView instanceof TextView) {
                        TextView textView = (TextView) clickedView;
                        textView.setText("New Teacher Name"); // Set the new name
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    // Communicate with the MainActivity to toggle RecyclerView visibility
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).toggleRecyclerViewVisibility();
                    }
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cleanup binding to avoid memory leaks
        binding = null;
    }
}
