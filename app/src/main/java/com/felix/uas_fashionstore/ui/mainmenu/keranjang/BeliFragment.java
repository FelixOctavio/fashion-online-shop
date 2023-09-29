package com.felix.uas_fashionstore.ui.mainmenu.keranjang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentBeliBinding;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;

import java.util.UUID;

public class BeliFragment extends Fragment {

    private FragmentBeliBinding binding;
    public TextView kode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBeliBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((MainMenuActivity) getActivity()).actionBar.hide();

        ((MainMenuActivity) getActivity()).navView.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainMenuActivity) getActivity()).actionBar.show();
        ((MainMenuActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView kode = (TextView) view.findViewById(R.id.kodePembayaran);

        kode.setText(getArguments().getString("kode"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
