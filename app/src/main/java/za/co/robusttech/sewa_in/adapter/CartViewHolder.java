package za.co.robusttech.sewa_in.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_profile, post_image, comment, save, more;
    public TextView username, likes, publisher, description, comments;
    public ToggleButton like;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);


    }
}
