package com.jasonstanl3y.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 1;

    List<String> items;
    Button btnAdd;
    EditText edtItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        edtItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int adapterPosition) {

                // Delete the item from the model
                items.remove(adapterPosition);

                // Notify the adapter
                itemsAdapter.notifyItemRemoved(adapterPosition);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                //create a new activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);

                //display the activity
                startActivityForResult(intent, EDIT_TEXT_CODE);


            }
        };
       itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);

       rvItems.setAdapter(itemsAdapter);
       rvItems.setLayoutManager(new LinearLayoutManager(this));

       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String toDoItem = edtItem.getText().toString();


               if(toDoItem.equals("")) {
                   Toast.makeText(getApplicationContext(), "This text field can't be empty, please add a list item", Toast.LENGTH_SHORT).show();
               } else {
                   //Add item to model
                   items.add(toDoItem);
                   //Notify adapter that item has been inserted
                   itemsAdapter.notifyItemInserted(items.size() - 1);
                   edtItem.setText("");
                   Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                   saveItems();
               }
           }
       });
    }

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //Extract original position of the edited item from the key position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully.", Toast.LENGTH_SHORT).show();


        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {

        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);

        }

    }
}
