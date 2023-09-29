package com.felix.uas_fashionstore.ui.mainmenu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;

import java.util.List;

public class ProductAdapter extends
            RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
        private static final String TAG = "Product Adapter";
        private Context context;
        private List<Product> notesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productTitle;
            public TextView productPrice;

            public MyViewHolder(View view) {
                super(view);
                productTitle = view.findViewById(R.id.productTitle);
                productPrice = view.findViewById(R.id.productPrice);
            }
        }


        public ProductAdapter(Context context, List<Product> notesList) {
            this.context = context;
            this.notesList = notesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Product product = notesList.get(position);

            holder.productTitle.setText(product.getName());
            holder.productPrice.setText("Rp" + product.getPrice().toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle productClick = new Bundle();
                    productClick.putInt("id", product.getIdDB()); //Passing id ke fragment detail produk
                    Navigation.findNavController(view).navigate(R.id.navigation_detail_product, productClick);
                }
            });
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }
}
