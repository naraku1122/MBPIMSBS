package com.example.admin.pimsbs.listeners;

import android.view.View;

public interface ProductDataRecyclerViewListener {

    void onItemClick(View view, int position);
    void onItemLongPressed(View view, int position);
    void onItemLongPressedDelete(View view, int position);

}
