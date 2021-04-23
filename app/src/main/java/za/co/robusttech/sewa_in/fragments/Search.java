package za.co.robusttech.sewa_in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;
import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Products;


public class Search extends Fragment {
    private EditText mSearchField;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        mSearchField =view.findViewById(R.id.search_field);
        ImageButton mSearchBtn = view.findViewById(R.id.search_btn);

        mResultList = view.findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

        return view;

    }

    private void firebaseUserSearch(String searchText) {
        Toast.makeText(getContext(), "Started Search", Toast.LENGTH_LONG).show();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Products");

        Query firebaseSearchQuery = mUserDatabase.orderByChild("productName").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(firebaseSearchQuery,Products.class).build();
        FirebaseRecyclerAdapter<Products, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull final Products model) {
                holder.setDetails(getContext(), model.getProductName(), model.getProductImage(), model.getProductRatings(), model.getProductPrice(), model.getProductDeliveryTime(), model.getProductId(), model.getProductDiscount(), model.getProductCategory() , model.getProductDesciption());
              


            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent, false);
                return new UsersViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }


        public void setDetails(Context context, String productName, String productImage, String productRatings, String productPrice, String productDeliveryTime, String productId, String productDiscount, String productCategory, String productDesciption) {

            TextView name = mView.findViewById(R.id.productName);
            ImageView image = mView.findViewById(R.id.product_image);
            TextView ratings = mView.findViewById(R.id.productRatings);
            TextView price = mView.findViewById(R.id.productPrice);
            TextView deliveryTime = mView.findViewById(R.id.productDeliveryTime);
            TextView discount = mView.findViewById(R.id.productDiscount);

            name.setText(productName);
            Glide.with(context).load(productImage).into(image);
            ratings.setText(productRatings);
            price.setText(productPrice);
            deliveryTime.setText(productDeliveryTime);
            discount.setText(productDiscount);






        }
    }


}