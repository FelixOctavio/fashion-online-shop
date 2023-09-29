package com.felix.uas_fashionstore.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.databinding.FragmentRegisterUserBinding;
import com.felix.uas_fashionstore.ui.start.login.LoginActivity;

public class RegisterUserFragment extends Fragment {

    private FragmentRegisterUserBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterUserBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        Bundle passing = new Bundle();

        binding.sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passing.putInt("type", 1); //Register sebagai seller
                intent.putExtras(passing);
                startActivity(intent);
            }
        });

        binding.userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passing.putInt("type", 2); //Register sebagai customer
                intent.putExtras(passing);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}