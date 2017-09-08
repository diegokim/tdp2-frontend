package com.example.android.linkup.sync_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.linkup.R;

public class SyncListViewHolder extends RecyclerView.ViewHolder{
    public TextView texto;

    public SyncListViewHolder(View itemView) {
        super(itemView);
        texto = (TextView) itemView.findViewById(R.id.sync_list_item_text);
    }
}
