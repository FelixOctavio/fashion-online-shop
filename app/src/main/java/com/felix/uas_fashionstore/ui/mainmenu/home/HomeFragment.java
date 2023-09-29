package com.felix.uas_fashionstore.ui.mainmenu.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentHomeBinding;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;
import com.felix.uas_fashionstore.ui.mainmenu.Product;
import com.felix.uas_fashionstore.ui.mainmenu.ProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductAdapter adapterTerbaru, adapterBaju, adapterCelana;
    public List<Product> listTerbaru, listBaju, listCelana = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((((MainMenuActivity) requireActivity()).get_tipe()) == 1) {
                    Navigation.findNavController(view).navigate(R.id.navigation_add_item);
                } else {
                    Navigation.findNavController(view).navigate(R.id.navigation_keranjang);
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView welcome = (TextView) view.findViewById(R.id.welcome_text);
        TextView tipe = (TextView) view.findViewById(R.id.account_type);
        Button pesananButton = (Button) view.findViewById(R.id.terima_pesanan_button);

        welcome.setText("Welcome, " + ((MainMenuActivity) requireActivity()).get_user());
        if (((MainMenuActivity) getActivity()).get_tipe() == 1) {
            tipe.setText("Seller");
            pesananButton.setVisibility(View.VISIBLE);
        } else {
            tipe.setText("Customer");
            pesananButton.setVisibility(View.INVISIBLE);
        }

        DBController controller;
        controller = new DBController(getActivity(), "", null, 1);

        listTerbaru = controller.list_product(0, 1);
        if (((MainMenuActivity) getActivity()).get_tipe() == 1) {
            Integer hitung = controller.hitung_pesanan();
            if (hitung > 0) {
                pesananButton.setEnabled(true);
                pesananButton.setText("Ada pesanan! (" + String.valueOf(hitung) + ")");
            } else {
                pesananButton.setEnabled(false);
                pesananButton.setText("Tidak ada pesanan saat ini");
            }
        }

        if (!(listTerbaru.isEmpty())) {
            adapterTerbaru = new ProductAdapter(getContext(), listTerbaru);
            RecyclerView recyclerTerbaru = (RecyclerView) view.findViewById(R.id.recycler_upselling);
            LinearLayoutManager layoutTerbaru = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerTerbaru.setLayoutManager(layoutTerbaru);
            recyclerTerbaru.setAdapter(adapterTerbaru);
            recyclerTerbaru.setItemAnimator(new DefaultItemAnimator());
        }

        listBaju = controller.list_product(1, 0);

        if (!(listBaju.isEmpty())) {
            adapterBaju = new ProductAdapter(getContext(), listBaju);
            RecyclerView recyclerBaju = (RecyclerView) view.findViewById(R.id.recycler_baju);
            LinearLayoutManager layoutBaju = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerBaju.setLayoutManager(layoutBaju);
            recyclerBaju.setAdapter(adapterBaju);
            recyclerBaju.setItemAnimator(new DefaultItemAnimator());
        }

        listCelana = controller.list_product(2, 0);

        if (!(listCelana.isEmpty())) {
            adapterCelana = new ProductAdapter(getContext(), listCelana);
            RecyclerView recyclerCelana = (RecyclerView) view.findViewById(R.id.recycler_celana);
            LinearLayoutManager layoutCelana = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerCelana.setLayoutManager(layoutCelana);
            recyclerCelana.setAdapter(adapterCelana);
            recyclerCelana.setItemAnimator(new DefaultItemAnimator());
        }

        pesananButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat onclick terima pesanan
                Bundle passing = new Bundle();
                passing.putInt("type", 1);
                Navigation.findNavController(view).navigate(R.id.navigation_transactions, passing);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}