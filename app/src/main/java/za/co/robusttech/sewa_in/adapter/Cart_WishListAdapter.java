package za.co.robusttech.sewa_in.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.activities.AddCartActivity;
import za.co.robusttech.sewa_in.activities.HomeActivity;
import za.co.robusttech.sewa_in.activities.ProductDetailActivity;
import za.co.robusttech.sewa_in.activities.WishListActivity;
import za.co.robusttech.sewa_in.models.Product;


public class Cart_WishListAdapter extends RecyclerView.Adapter<Cart_WishListAdapter.ImageViewHolder> {
    private List<Product> products;
    private Context mContext;
    private OnItemClickListener mListener;
    private double mAmountDue = 0.0;

    public Cart_WishListAdapter(Context context, List<Product> products) {
        mContext = context;
        this.products = products;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final Product product = products.get(position);

        double currentProductPrice = product.getProductPrice();
        mAmountDue += currentProductPrice;
        System.out.println("\n\n\n\n\n\t\tProducts total price is " + mAmountDue + "\n\n\n\n\n");

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.valueOf(product.getProductPrice()));
        holder.tvProductQty.setText(" Qty: " + product.getProductQuantity());
        Glide.with(mContext).load(product.getProductImageUrl()).into(holder.product_image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra(HomeActivity.PRODUCT, product);
            mContext.startActivity(intent);
        });


        holder.delete.setOnClickListener(v -> {
            Product remove = products.remove(position);
            dataSetChanged();
            removeAt(remove);
        });
    }

    private void removeAt(Product product) {
        String userId = getUserId();

        FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userId)
                .child(product.getProductId())
                .removeValue()
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isComplete()) {
                        Toast.makeText(mContext.getApplicationContext(), "Product deleted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext.getApplicationContext(), "Failed to delete product", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @UiThread
    protected void dataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public void setOnItemClickListener(AddCartActivity onItemClickListener) {

    }
    public void setOnItemClickListener(WishListActivity onItemClickListener) {

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private String getUserId() {
        return Objects.requireNonNull(FirebaseAuth
                .getInstance()
                .getCurrentUser())
                .getUid();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView product_image, delete, add, minus;

        public TextView productName, productPrice, stock_r_not, shipping, txtCart, tvProductQty;

        public ImageViewHolder(View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            stock_r_not = itemView.findViewById(R.id.stock_r_not);
            shipping = itemView.findViewById(R.id.shipping);
            tvProductQty = itemView.findViewById(R.id.cart_product_quantity);

            delete = itemView.findViewById(R.id.delete);
            add = itemView.findViewById(R.id.add);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}