package com.felix.uas_fashionstore.ui.mainmenu.keranjang;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentKeranjangBinding;
import com.felix.uas_fashionstore.ui.mainmenu.KeranjangAdapter;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;
import com.felix.uas_fashionstore.ui.mainmenu.Product;
import com.felix.uas_fashionstore.ui.mainmenu.ProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeranjangFragment extends Fragment {

    private FragmentKeranjangBinding binding;
    public List<Product> keranjang, listUpselling, listWishlist = new ArrayList<>();
    public KeranjangAdapter adapter;
    private TextView warning, keranjangHarga, upselling, wishlist;
    private Button beliButton;
    public DBController controller;
    private static KeranjangFragment instance;
    private ProductAdapter adapterUpselling, adapterWishlist;
    public View temp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentKeranjangBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((MainMenuActivity) getActivity()).actionBar.hide();

        instance = this;
        return root;
    }

    public static KeranjangFragment getInstance() {
        return instance;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainMenuActivity) requireActivity()).navView.setVisibility(VISIBLE);
        ((MainMenuActivity) getActivity()).actionBar.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        warning = (TextView) view.findViewById(R.id.warning_keranjang);
        keranjangHarga = (TextView) view.findViewById(R.id.totalKeranjangText);
        beliButton = (Button) view.findViewById(R.id.keranjangBeliButton);

        controller = new DBController(getActivity(),"",null,1);

        upselling = (TextView) view.findViewById(R.id.title_upselling);
        wishlist = (TextView) view.findViewById(R.id.title_wishlis_upselling);

        temp = view;

        refresh_keranjang(view);
        refresh_harga();

        beliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat onclick beli
                String kodePembayaran = UUID.randomUUID().toString(); //Generate kode buat pembayaran
                for (int counter = 0; counter < keranjang.size(); counter++) {
                    controller.add_transaksi(keranjang.get(counter).getIdStock(), keranjang.get(counter).getStockS(), keranjang.get(counter).getStockM(), keranjang.get(counter).getStockL(), keranjang.get(counter).getPrice(), kodePembayaran);
                } //Buat pindahin semua ke history transaksi
                controller.delete_keranjang_user(); //Buat bersihin keranjang belanja user
                Bundle passing = new Bundle();
                passing.putString("kode", kodePembayaran);
                Navigation.findNavController(view).navigate(R.id.navigation_beli, passing); //Nampilin kode pembayaran ke user
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Integer hitungTotal() {
        Integer count = 0;
        for (int counter = 0; counter < keranjang.size(); counter++) {
            count += (keranjang.get(counter).getPrice() * (keranjang.get(counter).getStockS() + keranjang.get(counter).getStockM() + keranjang.get(counter).getStockL()));
        }
        return count;
    }

    public void refresh_harga() {
        Integer hitung = hitungTotal();
        keranjangHarga.setText("Rp" + String.valueOf(hitung));
        if (hitung == 0) beliButton.setEnabled(false);
        else beliButton.setEnabled(true);
    }

    public void upselling_trick(View view) {
        listUpselling = controller.list_product(0, 1);

        if (!(listUpselling.isEmpty())) {
            upselling.setVisibility(VISIBLE);
            adapterUpselling = new ProductAdapter(getContext(), listUpselling);
            RecyclerView recyclerUpselling = (RecyclerView) view.findViewById(R.id.recycler_upselling);
            LinearLayoutManager layoutUpselling = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerUpselling.setLayoutManager(layoutUpselling);
            recyclerUpselling.setAdapter(adapterUpselling);
            recyclerUpselling.setItemAnimator(new DefaultItemAnimator());
        } else upselling.setVisibility(GONE);

        listWishlist = controller.list_wishlist();

        if (!(listWishlist.isEmpty())) {
            wishlist.setVisibility(VISIBLE);
            adapterWishlist = new ProductAdapter(getContext(), listWishlist);
            RecyclerView recyclerWishlist = (RecyclerView) view.findViewById(R.id.recycler_wishlist_upselling);
            LinearLayoutManager layoutWishlist = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerWishlist.setLayoutManager(layoutWishlist);
            recyclerWishlist.setAdapter(adapterWishlist);
            recyclerWishlist.setItemAnimator(new DefaultItemAnimator());
        } else wishlist.setVisibility(GONE);
    }

    public void refresh_keranjang(View view) {
        if (view == null) view = temp;
        keranjang = controller.list_keranjang();
        if (!(keranjang.isEmpty())) {
            upselling.setVisibility(GONE);
            wishlist.setVisibility(GONE);
            warning.setVisibility(GONE);
            adapter = new KeranjangAdapter(getContext(), keranjang);
            RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler_keranjang);
            LinearLayoutManager layout = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layout);
            recycler.setAdapter(adapter);
            recycler.setItemAnimator(new DefaultItemAnimator());
        } else {
            warning.setVisibility(VISIBLE);
            upselling_trick(view);
        }
    }
}