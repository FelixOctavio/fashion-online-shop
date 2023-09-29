package com.felix.uas_fashionstore.ui.mainmenu.wishlist;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentWishlistBinding;
import com.felix.uas_fashionstore.ui.mainmenu.Product;
import com.felix.uas_fashionstore.ui.mainmenu.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    public List<Product> wishlist = new ArrayList<>();
    private ProductAdapter adapter;
    private TextView warning;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBController controller;
        controller = new DBController(getActivity(),"",null,1);
        wishlist = controller.list_wishlist();
        warning = (TextView) view.findViewById(R.id.warning_wishlist);

        if (!(wishlist.isEmpty())) {
            warning.setVisibility(GONE);
            adapter = new ProductAdapter(getContext(), wishlist);
            RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler_wishlist);
            GridLayoutManager layout = new GridLayoutManager(getContext(), 4);
            recycler.setLayoutManager(layout);
            recycler.setAdapter(adapter);
            recycler.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}