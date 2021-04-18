package za.co.robusttech.sewa_in.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.activities.HomeActivity;
import za.co.robusttech.sewa_in.activities.ProductDetailActivity;
import za.co.robusttech.sewa_in.models.home_sub;

public class sub_topic_adapter extends RecyclerView.Adapter<sub_topic_adapter.GroceryViewHolder>{

    private List<home_sub> subTopic;
    Context context;

    public sub_topic_adapter(List<home_sub> subTopic, Context context){
        this.subTopic= subTopic;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.imageView.setImageResource(subTopic.get(position).getTopic_img());
        holder.txtview.setText(subTopic.get(position).getTopic());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = subTopic.get(position).getTopic().toString();
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("productName", productName);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return subTopic.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        public GroceryViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.sub_topic_bg);
            txtview=view.findViewById(R.id.sub_topic);
        }
    }
}

