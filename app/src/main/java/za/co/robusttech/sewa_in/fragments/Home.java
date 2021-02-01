package za.co.robusttech.sewa_in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.activities.ItemDetailActivity;
import za.co.robusttech.sewa_in.models.Products;

public class Home extends Fragment {
    GridView gridView;
    private List<Products> product;
    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);

        gridView = view.findViewById(R.id.grid_layout);
        gridLoad();

        return view;


    }

    private class CustomAdapter extends BaseAdapter {
        private Context mContext;
        private List<Products> products;
        int total;

        class ViewHolder {
            ImageView image;
            TextView name , description , price;
        }

        public CustomAdapter(Context context, List<Products> products) {
            mContext = context;
            this.products = products;
        }

        @Override
        public int getCount() {

            //  return
            total = products.size();

            return total;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.horizontal_product_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view1.findViewById(R.id.h_s_product_image);
            viewHolder.name = (TextView) view1.findViewById(R.id.h_s_product_title);
            viewHolder.description = (TextView) view1.findViewById(R.id.h_s_product_description);
            viewHolder.price = (TextView) view1.findViewById(R.id.h_s_product_price);


            if (i <= products
                    .size()) {
                Products produc = products.get(i);
                Glide.with(getContext()).load(produc.getProductImage()).into(viewHolder.image);
                viewHolder.name.setText(produc.getProductName());
                viewHolder.description.setText(produc.getProductDesciption());
                viewHolder.price.setText(produc.getProductPrice());

            }
            return view1;

        }


    }


    private void gridLoad() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ItemDetailActivity.class);
                intent.putExtra("productName", product.get(position).getProductName());
                intent.putExtra("productRatings", product.get(position).getProductRatings());
                intent.putExtra("productPrice", product.get(position).getProductPrice());
                intent.putExtra("productDeliveryTime", product.get(position).getProductDeliveryTime());
                intent.putExtra("productDescription", product.get(position).getProductDesciption());
                intent.putExtra("productDiscount", product.get(position).getProductDiscount());
                intent.putExtra("productId", product.get(position).getProductId());
                intent.putExtra("productCategory", product.get(position).getProductCategory());
                intent.putExtra("productImage", product.get(position).getProductImage());
                startActivity(intent);
                Toast.makeText(getContext(), product.get(position).getProductId(), Toast.LENGTH_SHORT).show();

            }
        });


        product = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
       mDatabaseRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for (DataSnapshot data:snapshot.getChildren()){

                   Products pro = data.getValue(Products.class);
                   String productImage = pro.getProductImage();
                   String productName = pro.getProductName();
                   String productDesciption = pro.getProductDesciption();
                   String productPrice = pro.getProductPrice();
                   String productId = pro.getProductId();
                   String productCategory = pro.getProductCategory();
                   String productDiscount = pro.getProductDiscount();
                   String productDeliveryTime = pro.getProductDeliveryTime();
                   String productRatings = pro.getProductRatings();
                   String productSeller = pro.getProductSeller();
                   String productAvailability = pro.getProductAvailability();
                   String productOriginalPrice = pro.getProductOriginalPrice();

                   Products products = new Products(productImage , productName, productDesciption ,productPrice , productId, productCategory , productDiscount, productDeliveryTime , productRatings, productSeller ,productAvailability , productOriginalPrice);
                   product.add(products);

                   CustomAdapter customAdapter = new CustomAdapter(getContext(), product);
                   gridView.setVisibility(View.VISIBLE);
                   gridView.setAdapter(customAdapter);

               }



           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });




    }
}