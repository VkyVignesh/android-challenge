package com.vky.startwars.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vky.startwars.BR;
import com.vky.startwars.R;
import com.vky.startwars.model.StarWar;

import java.util.List;

/**
 * Created by Praveena on 15-10-2016.
 */

public class StarShipsAdapter extends RecyclerView.Adapter<StarShipsAdapter.ViewHolder> {

    private List<StarWar.Results> list;
    private LayoutInflater inflater;

    public StarShipsAdapter(Context context, List<StarWar.Results> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_starships, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setVariable(BR.ship, list.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        private ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
