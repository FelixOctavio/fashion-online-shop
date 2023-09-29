package com.felix.uas_fashionstore.ui.mainmenu;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.ui.mainmenu.keranjang.KeranjangFragment;

import java.util.List;

public class KeranjangAdapter extends
        RecyclerView.Adapter<com.felix.uas_fashionstore.ui.mainmenu.KeranjangAdapter.MyViewHolder> {
    private static final String TAG = "Keranjang Adapter";
    private Context context;
    private List<Product> list;
    Integer varian = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView keranjangTitle, keranjangVarian, keranjangHarga, keranjangJumlah;
        public Button keranjangHapus;

        public MyViewHolder(View view) {
            super(view);
            keranjangTitle = view.findViewById(R.id.keranjangProductTitle);
            keranjangVarian = view.findViewById(R.id.keranjangProductVarian);
            keranjangHarga = view.findViewById(R.id.keranjangProductPrice);
            keranjangJumlah = view.findViewById(R.id.keranjangProductJumlah);
            keranjangHapus = view.findViewById(R.id.keranjangHapusButton);
        }
    }


    public KeranjangAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keranjang_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(com.felix.uas_fashionstore.ui.mainmenu.KeranjangAdapter.MyViewHolder holder, int position) {
        final Product product = list.get(position);

        holder.keranjangTitle.setText(product.getName());
        if (product.getStockS() > 0) {
            holder.keranjangVarian.setText("Varian: S");
            varian = 1;
        } else if (product.getStockM() > 0) {
            holder.keranjangVarian.setText("Varian: M");
            varian = 2;
        } else {
            holder.keranjangVarian.setText("Varian: L");
            varian = 3;
        }

        holder.keranjangHarga.setText("Rp" + (product.getPrice().toString())); //Ini harga satuan
        Integer temp = product.getStockS() + product.getStockM() + product.getStockL();
        if (temp > 0) holder.keranjangJumlah.setText(String.valueOf(temp));
        else holder.keranjangJumlah.setText("");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat onclick recyclernya
                Bundle productClick = new Bundle();
                productClick.putInt("id", product.getIdDB()); //Passing id ke fragment detail produk
                Navigation.findNavController(view).navigate(R.id.navigation_detail_product, productClick);
            }
        });

        holder.keranjangHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat hapus dari keranjang
                Log.v(TAG, "Tombol hapus dipencet");
                ((MainMenuActivity) context).controller.delete_item_keranjang(product.getIdStock(), varian);

                list.remove(position);  // Hapus dari list
                notifyItemRemoved(position); // Kasih tau ke adapter
                KeranjangFragment.getInstance().refresh_harga();
                KeranjangFragment.getInstance().refresh_keranjang(null);
            }
        });

        holder.keranjangJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v(TAG, "Jumlah berubah!");
                Integer stok = 0;
                if (holder.keranjangJumlah.getText().toString().equals("")) stok = 0;
                else if (holder.keranjangJumlah.getText().toString().equals("0")) {
                    stok = 1;
                    holder.keranjangJumlah.setText(String.valueOf(stok));
                    Toast.makeText(context, "Jumlah pembelian minimal 1!", Toast.LENGTH_SHORT).show();
                } else {
                    stok = parseInt(holder.keranjangJumlah.getText().toString());
                    Integer hitung = ((MainMenuActivity) context).controller.count_stock(product.getIdStock(), varian);
                    if (stok > hitung) {
                        holder.keranjangJumlah.setText(String.valueOf(hitung));
                        Toast.makeText(context, "Stok hanya ada " + String.valueOf(hitung), Toast.LENGTH_SHORT).show();
                        stok = hitung;
                    }
                }

                if (varian == 1) product.setStockS(stok);
                else if (varian == 2) product.setStockM(stok);
                else if (varian == 3) product.setStockL(stok);

                KeranjangFragment.getInstance().controller.update_item_keranjang(product.getIdStock(), stok, varian);
                KeranjangFragment.getInstance().refresh_harga();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
