package com.example.android.linkup.sync_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;

import java.util.ArrayList;

public class SyncListAdapter<T> extends RecyclerView.Adapter<SyncListViewHolder> {

    private ArrayList<T> dataset;
    private Context context;

    public SyncListAdapter (ArrayList<T> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    public void updateDataSet (ArrayList<T> newDataset) {
        this.dataset = newDataset;
    }

    @Override
    public SyncListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("LASKJDLAKSJD","ASJLDJASLKDJASLKDJASLKDJLKASDJKL");
        View v = View.inflate(context, R.layout.sync_list_item, null);
        return new SyncListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SyncListViewHolder holder, int position) {
        Log.d("ASLKDJASLK: ", "onBind View HOlder " + position);
        holder.texto.setText("ALKSJDLKASJDLK "+ dataset.get(position) + position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
