package com.felix.uas_fashionstore.ui.mainmenu;


import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentDetailProductBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailFragment extends Fragment {
    private static final String TAG = "Product Detail Fragment";

    private FragmentDetailProductBinding binding;
    private DBController controller;
    public TextView productTitle, productPrice, productStock, productDescription, stokS, stokM, stokL;
    public Button wishlistButton, keranjangButton;
    private Product product;
    private LinearLayout addStok;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetailProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        controller = new DBController(getContext(), "", null, 1);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainMenuActivity) getActivity()).actionBar.hide();

        productTitle = view.findViewById(R.id.detailProductTitle);
        productPrice = view.findViewById(R.id.detailProductPrice);
        productStock = view.findViewById(R.id.detailProductStock);
        productDescription = view.findViewById(R.id.detailProductDescription);

        wishlistButton = view.findViewById(R.id.detailWishlistButton);
        keranjangButton = view.findViewById(R.id.detailProductKeranjangButton);

        addStok = view.findViewById(R.id.layout_add_stok);
        stokS = view.findViewById(R.id.ukuranTerkecilInput);
        stokM = view.findViewById(R.id.ukuranTengahInput);
        stokL = view.findViewById(R.id.ukuranTerbesarInput);

        product = ((MainMenuActivity) getActivity()).controller.get_detail_product(getArguments().getInt("id"));

        if (((MainMenuActivity) getActivity()).get_tipe() == 1) {
            wishlistButton.setVisibility(View.GONE);
            addStok.setVisibility(View.VISIBLE);
            stokS.setText(String.valueOf(product.getStockS()));
            stokM.setText(String.valueOf(product.getStockM()));
            stokL.setText(String.valueOf(product.getStockL()));
        } else {
            addStok.setVisibility(View.GONE);
            wishlistButton.setVisibility(View.VISIBLE);
        }

        update_wishlist();

        ((MainMenuActivity) getActivity()).navView.setVisibility(View.GONE);

        productTitle.setText(product.getName());
        productPrice.setText("Rp" + product.getPrice());
        productStock.setText("Stok (S: " + product.getStockS() + ", M: " + product.getStockM() + ", L: " + product.getStockL() + ")");
        productDescription.setText(product.getDescription());

        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.check_wishlist(product.getIdDB())) {
                    controller.delete_wishlist(product.getIdDB());
                    Snackbar.make(view, "Barang berhasil dihapus dari wishlist", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    //Kode buat nambahin ke wishlist
                    ((MainMenuActivity) requireActivity()).controller.add_wishlist(((MainMenuActivity) requireActivity()).controller.get_state(), product.getIdDB());
                    Snackbar.make(view, "Barang berhasil ditambahkan ke wishlist", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                update_wishlist();

            }
        });

        if (((MainMenuActivity) getActivity()).get_tipe() == 2) { // Jika yang login adalah user
            keranjangButton.setText("+ Keranjang"); //Ngubah text "+ keranjang"
            if (product.getStockS() == 0 && product.getStockM() == 0 && product.getStockL() == 0) keranjangButton.setEnabled(false);
            else keranjangButton.setEnabled(true);
        } else {
            keranjangButton.setEnabled(false);
            keranjangButton.setText("Update stock"); //Ngubah text jadi update stock (di tombol keranjang)
        }

        keranjangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainMenuActivity) getActivity()).get_tipe() == 2) {
                    //Kode buat nambahin ke keranjang di sini
                    showBottomSheetDialog(view);
                } else {
                    // Buat update stock
                    int stokSBaru = 0, stokMBaru = 0, stokLBaru = 0;

                    if (!(product.getStockS().equals(parseInt(stokS.getText().toString())))) {
                        stokSBaru = parseInt(stokS.getText().toString()) - product.getStockS();
                    } else stokSBaru = 0;

                    if (!(product.getStockM().equals(parseInt(stokM.getText().toString())))) {
                        stokMBaru = parseInt(stokM.getText().toString()) - product.getStockM();
                    } else stokMBaru = 0;

                    if (!(product.getStockL().equals(parseInt(stokL.getText().toString())))) {
                        stokLBaru = parseInt(stokL.getText().toString()) - product.getStockL();
                    } else stokLBaru = 0;

                    if (stokSBaru == 0 && stokMBaru == 0 && stokLBaru == 0) {
                        Snackbar.make(view, "Stok tidak ada perubahan", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        product.setStockS(parseInt(stokS.getText().toString()));
                        product.setStockM(parseInt(stokM.getText().toString()));
                        product.setStockL(parseInt(stokL.getText().toString()));

                        productStock.setText("Stok (S: " + product.getStockS() + ", M: " + product.getStockM() + ", L: " + product.getStockL() + ")");
                        ((MainMenuActivity) getActivity()).controller.update_stok(product.getIdStock(), stokSBaru, stokMBaru, stokLBaru);
                        Snackbar.make(view, "Stok berhasil diupdate!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }

                }
            }
        });

        stokS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!(stokS.getText().toString().equals(""))) {
                    if (product.getStockS().equals(parseInt(stokS.getText().toString()))) keranjangButton.setEnabled(false);
                    else keranjangButton.setEnabled(true);
                } else keranjangButton.setEnabled(false);
            }
        });

        stokM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!(stokM.getText().toString().equals(""))) {
                    if (product.getStockM().equals(parseInt(stokM.getText().toString())))
                        keranjangButton.setEnabled(false);
                    else keranjangButton.setEnabled(true);
                } else keranjangButton.setEnabled(false);
            }
        });

        stokL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!(stokL.getText().toString().equals(""))) {
                    if (product.getStockL().equals(parseInt(stokL.getText().toString())))
                        keranjangButton.setEnabled(false);
                    else keranjangButton.setEnabled(true);
                } else keranjangButton.setEnabled(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (((MainMenuActivity) requireActivity()).get_tipe() == 2)
            ((MainMenuActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
        ((MainMenuActivity) getActivity()).actionBar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainMenuActivity) requireActivity()).navView.setVisibility(View.GONE);
        ((MainMenuActivity) getActivity()).actionBar.hide();
    }

    private void showBottomSheetDialog(View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        Button small = bottomSheetDialog.findViewById(R.id.buttonUkuranTerkecil);
        Button medium = bottomSheetDialog.findViewById(R.id.buttonUkuranTengah);
        Button large = bottomSheetDialog.findViewById(R.id.buttonUkuranTerbesar);

        if (product.getStockS() == 0) small.setEnabled(false);
        else small.setEnabled(true);
        if (product.getStockM() == 0) medium.setEnabled(false);
        else medium.setEnabled(true);
        if (product.getStockL() == 0) large.setEnabled(false);
        else large.setEnabled(true);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenuActivity) requireActivity()).controller.add_keranjang(product.getIdStock(), 1, 1);
                Snackbar.make(view, "Barang berhasil ditambahkan ke keranjang", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                bottomSheetDialog.dismiss();
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenuActivity) requireActivity()).controller.add_keranjang(product.getIdStock(), 2, 1);
                Snackbar.make(view, "Barang berhasil ditambahkan ke keranjang", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                bottomSheetDialog.dismiss();
            }
        });

        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenuActivity) requireActivity()).controller.add_keranjang(product.getIdStock(), 3, 1);
                Snackbar.make(view, "Barang berhasil ditambahkan ke keranjang", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    public void update_wishlist() {
        if (controller.check_wishlist(product.getIdDB()) == false)
            wishlistButton.setText("Tambahkan ke Wishlist");
        else wishlistButton.setText("Hapus dari Wishlist");
    }
}
