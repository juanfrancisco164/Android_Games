package com.example.games_overspace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final List<String> menuItems;
    private final View.OnClickListener onClickListener;

    public MenuAdapter(List<String> menuItems, View.OnClickListener onClickListener) {
        this.menuItems = menuItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = menuItems.get(position);
        holder.textView.setText(item);

        if (item.equalsIgnoreCase("2048")) {
            holder.imageView.setImageResource(R.drawable.photo2048);
        } else if (item.equalsIgnoreCase("Senku")) {
            holder.imageView.setImageResource(R.drawable.senku_photo);
        } else if (item.equalsIgnoreCase("Settings")) {
            holder.imageView.setImageResource(R.drawable.settings_photo);
        } else {
            holder.imageView.setImageResource(R.drawable.d20icon);
        }

        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(onClickListener);
    }


    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemText);
            imageView = itemView.findViewById(R.id.itemIcon);
        }
    }
}
