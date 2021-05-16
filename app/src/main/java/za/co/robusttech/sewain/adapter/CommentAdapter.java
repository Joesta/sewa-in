package za.co.robusttech.sewain.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Comment;
import za.co.robusttech.sewain.models.profile;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Comment> mComment;
    private String postid;

    private FirebaseUser firebaseUser;

    public CommentAdapter(Context context, List<Comment> comments, String postid){
        mContext = context;
        mComment = comments;
        this.postid = postid;
    }

    @NonNull
    @Override
    public CommentAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(position);

        holder.comment.setText(comment.getComment());
        holder.title.setText(comment.getCommentTitle());
        holder.ratingBar.setRating(comment.getCommentRatings());
        Glide.with(mContext).load(comment.getProductImageUrl()).into(holder.imageView);



        getUserInfo(holder.image_profile, holder.username, comment.getPublisher());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (comment.getPublisher().equals(firebaseUser.getUid())) {

                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Do you want to delete?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance().getReference("Comments")
                                                .child(postid).child(comment.getCommentid())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView image_profile;
        public TextView username, comment , title;
        public RatingBar ratingBar;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile_c);
            username = itemView.findViewById(R.id.username_c);
            comment = itemView.findViewById(R.id.comment_c);
            title = itemView.findViewById(R.id.comment_title_c);
            ratingBar = itemView.findViewById(R.id.rating_bar_review);
            imageView = itemView.findViewById(R.id.image_view_comment);

        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile user = dataSnapshot.getValue(profile.class);
                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(mContext).load(user.getImageURL()).into(imageView);
                }
                username.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
