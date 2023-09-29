package com.felix.uas_fashionstore.ui.mainmenu.addItem;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.databinding.FragmentAddItemBinding;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;

import java.util.List;

public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding binding;
    private TextView inputProduct, inputDescription, inputPrice, inputStockTerkecil, inputStockTengah, inputStockTerbesar;
    private Spinner categorySpinner;
    private Button addItemButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((MainMenuActivity) requireActivity()).navView.setVisibility(View.GONE);
        ((MainMenuActivity) getActivity()).actionBar.hide();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainMenuActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
        ((MainMenuActivity) getActivity()).actionBar.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        inputProduct = view.findViewById(R.id.productNameEditText);
        inputDescription = view.findViewById(R.id.descriptionEditText);
        inputPrice = view.findViewById(R.id.priceInput);

        inputStockTerkecil = view.findViewById(R.id.ukuranTerkecilInput);
        inputStockTengah = view.findViewById(R.id.ukuranTengahInput);
        inputStockTerbesar = view.findViewById(R.id.ukuranTerbesarInput);

        addItemButton = view.findViewById(R.id.addItemButton);

        loadCategorySpinnerData();

        inputProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                check_input();
            }
        });

        inputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                check_input();
            }
        });

        inputStockTerkecil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                check_input();
            }
        });

        inputStockTengah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                check_input();
            }
        });

        inputStockTerbesar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                check_input();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini kode buat add new item
                ((MainMenuActivity) requireActivity()).controller.add_new_item(categorySpinner.getSelectedItem().toString(), inputProduct.getText().toString(), inputDescription.getText().toString(), parseInt(inputStockTerkecil.getText().toString()), parseInt(inputStockTengah.getText().toString()), parseInt(inputStockTerbesar.getText().toString()), parseInt(inputPrice.getText().toString()), null);
                Toast.makeText(getActivity(), "Berhasil menambahkan item baru", Toast.LENGTH_SHORT).show();
                clear_data();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainMenuActivity) requireActivity()).navView.setVisibility(View.GONE);
        binding = null;
    }
    private void loadCategorySpinnerData() {
        List<String> labels = ((MainMenuActivity) requireActivity()).controller.get_list_category();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);
    }

    private void clear_data() {
        categorySpinner.clearFocus();
        inputProduct.clearFocus();
        inputProduct.setText("");
        inputDescription.clearFocus();
        inputDescription.setText("");
        inputProduct.clearFocus();
        inputPrice.clearFocus();
        inputPrice.setText("");
        inputStockTerkecil.clearFocus();
        inputStockTerkecil.setText("");
        inputStockTengah.clearFocus();
        inputStockTengah.setText("");
        inputStockTerbesar.clearFocus();
        inputStockTerbesar.setText("");
    }

    private void check_input() {
        if (inputProduct.getText().toString().equals("") || inputDescription.getText().toString().equals("") || inputPrice.getText().toString().equals("") || inputStockTerkecil.getText().toString().equals("") || inputStockTengah.getText().toString().equals("") || inputStockTerbesar.getText().toString().equals("")) {
            addItemButton.setEnabled(false);
        } else {
            if (parseInt(inputPrice.getText().toString()) > 0 && (parseInt(inputStockTerkecil.getText().toString()) > 0 || parseInt(inputStockTengah.getText().toString()) > 0 || parseInt(inputStockTerbesar.getText().toString()) > 0)) addItemButton.setEnabled(true);
            else addItemButton.setEnabled(false);
        }
    }
}