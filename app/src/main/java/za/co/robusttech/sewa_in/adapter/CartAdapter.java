package za.co.robusttech.sewa_in.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.activities.AddCartActivity;
import za.co.robusttech.sewa_in.activities.ItemDetailActivity;
import za.co.robusttech.sewa_in.models.Products;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder> {
    private Context mContext;
    private final List<Products> Products;
    private OnItemClickListener mListener;



    public CartAdapter(Context context, List<Products> products) {
        mContext = context;
        Products = products;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final Products products = Products.get(position);
        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());
        Glide.with(mContext).load(products.getProductImage()).into(holder.product_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemDetailActivity.class);
                intent.putExtra("productId" , products.getProductId());
                mContext.startActivity(intent);
            }
        });


        if (holder.txtCart.getText().equals("1")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   holder.txtCart.setText("2");
                }
            });
        }

        if (holder.txtCart.getText().equals("2")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("3");
                }
            });
        }


        if (holder.txtCart.getText().equals("3")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("4");
                }
            });
        }

        if (holder.txtCart.getText().equals("4")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("5");
                }
            });
        }

        if (holder.txtCart.getText().equals("5")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("6");
                }
            });
        }

        if (holder.txtCart.getText().equals("6")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("7");
                }
            });
        }

        if (holder.txtCart.getText().equals("7")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("8");
                }
            });
        }

        if (holder.txtCart.getText().equals("8")){

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtCart.setText("9");
                }
            });
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.
                        getInstance().getCurrentUser().getUid())
                        .child("inCart").child(products.getProductId()).removeValue();

                Intent intent = new Intent(mContext, AddCartActivity.class);
                mContext.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return Products.size();
    }

    public void removeAt(int position){
        Products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, Products.size());
    }


    public void setOnItemClickListener(AddCartActivity onItemClickListener) {

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView product_image, delete, add , minus ;

        public TextView productName, productPrice, stock_r_not, shipping, txtCart ;

        public ImageViewHolder(View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            stock_r_not = itemView.findViewById(R.id.stock_r_not);
            shipping = itemView.findViewById(R.id.shipping);
            minus = itemView.findViewById(R.id.minus);
            delete = itemView.findViewById(R.id.delete);

            txtCart = itemView.findViewById(R.id.txtCart);
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

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}