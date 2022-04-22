package com.example.admin.pimsbs.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.pimsbs.listeners.ProductDataRecyclerViewListener;
import com.example.admin.pimsbs.models.Products;
import com.example.admin.pimsbs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ProductDataListsAdapter extends FirebaseRecyclerAdapter<Products,
        ProductDataListsAdapter.ProductDataListsViewHolder>
        implements RecyclerView.OnItemTouchListener{

    private ProductDataRecyclerViewListener clickListener;
    private GestureDetector gestureDetector;

    public ProductDataListsAdapter( @NonNull FirebaseRecyclerOptions<Products> products){
        super(products);
    }

    public ProductDataListsAdapter(FirebaseRecyclerOptions<Products> products, Context context, RecyclerView recyclerView, ProductDataRecyclerViewListener clickListener ){
        super(products);
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(child != null && clickListener != null){
                    clickListener.onItemLongPressed(child, recyclerView.getChildAdapterPosition(child));
                    clickListener.onItemLongPressedDelete(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductDataListsViewHolder holder, int position, @NonNull Products model) {
        holder.txtBarcode.setText(model.getProductBarcode());
        holder.txtProductName.setText(model.getProductName());
        holder.txtProductCategory.setText(model.getProductCategory());
        holder.txtProductPrice.setText(model.getProductPrice());
        holder.txtProductQuantity.setText(model.getProductQuantity());
    }

    @NonNull
    @Override
    public ProductDataListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_layout, parent, false);
        return new ProductDataListsAdapter.ProductDataListsViewHolder(view);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
            clickListener.onItemClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public class ProductDataListsViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtBarcode;
        private final TextView txtProductName;
        private final TextView txtProductCategory;
        private final TextView txtProductPrice;
        private final TextView txtProductQuantity;
        public ProductDataListsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBarcode = itemView.findViewById(R.id.viewproductbarcode);
            txtProductName = itemView.findViewById(R.id.viewproductname);
            txtProductCategory = itemView.findViewById(R.id.viewproductcategory);
            txtProductPrice = itemView.findViewById(R.id.viewproductprice);
            txtProductQuantity = itemView.findViewById(R.id.viewproductquantity);
        }
    }
}
