package com.jasonstanl3y.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {

        void onItemClicked(int position);
    }
    public interface OnLongClickListener {
        void onItemLongClicked(int adapterPosition);

    }

    List<String> items;
    OnLongClickListener onLongClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String>items, OnLongClickListener onLongClickListener, OnClickListener clickListener) {

        this.items = items;
        this.onLongClickListener = onLongClickListener;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Use layout inflator to inflate a view
        // wrap it inside a view Holder and return it
        View toDoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);


        return new ViewHolder(toDoView);
    }

    //Responsible for binding data to a particular view Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Grab item at the position
       String item = items.get(position);
        //Bind the item in specified view holder
        holder.bind(item);
    }

    // Tells the Recycler View how many items are in the list
    @Override
    public int getItemCount() {

        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update view inside the view holder with this data
        public void bind(String item) {

            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    //notify the listener which item was long pressed
                    onLongClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
