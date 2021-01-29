package za.co.robusttech.sewa_in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.co.robusttech.sewa_in.R;
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
        product = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<HashMap<String, String>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {
                };

                String productImage = dataSnapshot.getValue(genericTypeIndicator).get("productImage");
                String productName = dataSnapshot.getValue(genericTypeIndicator).get("productName");
                String productDesciption = dataSnapshot.getValue(genericTypeIndicator).get("productDesciption");
                String productPrice = dataSnapshot.getValue(genericTypeIndicator).get("productPrice");

                Log.e("LoadDataGson", "name " + productName + "  value  " + productImage);



                Products products = new Products(productImage , productName, productDesciption ,productPrice);
                product.add(products);

                CustomAdapter customAdapter = new CustomAdapter(getContext(), product);
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(customAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}