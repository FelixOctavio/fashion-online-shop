package com.felix.uas_fashionstore.ui.mainmenu.transactions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;

import java.util.List;

public class HistoryAdapter extends
        RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private static final String TAG = "History Adapter";
    private Context context;
    private List<History> list;
    Integer varian = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView historyTanggal, historyTitle, historyStatus, historyTotalBarang, historyTotalPriceTitle, historyTotalPrice, kodePembayaran;
        public Button historyKonfirmasi;

        public MyViewHolder(View view) {
            super(view);
            historyTanggal = view.findViewById(R.id.historyTanggal);
            historyTitle = view.findViewById(R.id.historyTitle);
            historyStatus = view.findViewById(R.id.historyStatus);
            historyTotalBarang = view.findViewById(R.id.historyTotalBarang);
            historyTotalPriceTitle = view.findViewById(R.id.historyTotalPriceTitle);
            historyTotalPrice = view.findViewById(R.id.historyTotalPrice);
            historyKonfirmasi = view.findViewById(R.id.historyKonfirmasiButton);
            kodePembayaran = view.findViewById(R.id.kode_pembayaran);
        }
    }


    public HistoryAdapter(Context context, List<History> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list, parent, false);

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        final History product = list.get(position);

        holder.historyTanggal.setText(product.getdate());
        holder.historyTitle.setText(product.getName());
        if (product.getStatus() == 1) holder.historyStatus.setText("Belum diambil");
        else holder.historyStatus.setText("Sudah diambil");
        holder.historyTotalBarang.setText(String.valueOf(product.getStockS() + product.getStockM() + product.getStockL()) + " barang");
        holder.historyTotalPrice.setText(String.valueOf(product.getPrice() * (product.getStockS() + product.getStockM() + product.getStockL())));
        holder.kodePembayaran.setText(product.getKode());

        if (((MainMenuActivity) context).get_tipe() == 1) {
            holder.historyKonfirmasi.setVisibility(View.VISIBLE);
            holder.historyTotalPriceTitle.setText("Total Penjualan");
        }
        else holder.historyKonfirmasi.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat onclick recyclernya
                if (product.getStatus() == 1) { //Jika belum ambil barang
                    if (((MainMenuActivity) context).get_tipe() == 2) { //Jika diklik customer
                        Bundle passing = new Bundle();
                        passing.putString("kode", product.getKode());
                        Navigation.findNavController(view).navigate(R.id.navigation_beli, passing); //Nampilin kode pembayaran ke user
                    }
                }
            }
        });

        holder.historyKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ini buat konfirmasi pesanan (set selesai) (blm ada kodenya)
                TransactionsFragment.getInstance().controller.update_status(product.getIdStock(), product.getStockS(), product.getStockM(), product.getStockL(), product.getdate());
                list.remove(position);  // Hapus dari list
                notifyItemRemoved(position); // Kasih tau ke adapter
                TransactionsFragment.getInstance().refresh_recycler(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
