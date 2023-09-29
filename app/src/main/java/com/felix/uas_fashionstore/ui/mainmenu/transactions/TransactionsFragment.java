package com.felix.uas_fashionstore.ui.mainmenu.transactions;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.felix.uas_fashionstore.databinding.FragmentTransactionsBinding;
import com.felix.uas_fashionstore.ui.mainmenu.MainMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private FragmentTransactionsBinding binding;
    public List<History> history = new ArrayList<>();
    private HistoryAdapter adapter;
    private TextView warning, titleTransaksi;
    public View temp;
    private static TransactionsFragment instance;
    DBController controller;
    Integer type = 0; //Berarti user biasa, tampilin history seperti biasa

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        instance = this;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle passing = getArguments();

        if (passing != null) {
            type = passing.getInt("type"); //1 berarti seller
        }

        controller = new DBController(getActivity(), "", null, 1);
        history = controller.list_transaksi(type);
        warning = view.findViewById(R.id.warning_history);
        titleTransaksi = view.findViewById(R.id.title_history_transaction);

        if (((MainMenuActivity) getActivity()).get_tipe() == 1) { //Jika seller yang login
            titleTransaksi.setText("Pesanan masuk:");
        } else {
            titleTransaksi.setText("History transaksi:");
        }

        temp = view;
        refresh_recycler(temp);
    }

    public static TransactionsFragment getInstance() {
        return instance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refresh_recycler(View view) {
        if (view == null) view = temp;
        history = controller.list_transaksi(type);
        if (!(history.isEmpty())) {
            warning.setVisibility(GONE);
            adapter = new HistoryAdapter(getContext(), history);
            RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler_history);
            LinearLayoutManager layout = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layout);
            recycler.setAdapter(adapter);
            recycler.setItemAnimator(new DefaultItemAnimator());
        } else warning.setVisibility(View.VISIBLE);
    }
}