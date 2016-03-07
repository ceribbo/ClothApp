package com.clothapp.profile.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clothapp.ImageFragment;
import com.clothapp.R;
import com.clothapp.profile.UserProfileActivity;
import com.clothapp.profile_shop.ShopProfileActivity;
import com.clothapp.resources.Image;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileUploadedPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String profilo;
    public static List<Image> photos;

    private final static String username = ParseUser.getCurrentUser().getUsername();

    public ProfileUploadedPhotosAdapter(List<Image> items, String profilo) {
        this.profilo = profilo;
        ProfileUploadedPhotosAdapter.photos = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_uploaded_photos_list_item, parent, false);
        return new PhotoViewHolder(v, profilo);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;

        File imageFile = photos.get(position).getFile();

        if (profilo.equals("persona")) {
            Glide.with(UserProfileActivity.context)
                    .load(imageFile)
                    .into(photoViewHolder.photo);
        } else {
            Glide.with(ShopProfileActivity.context)
                    .load(imageFile)
                    .into(photoViewHolder.photo);
        }

        // Set item names
        photoViewHolder.txtItemNames.setText(photos.get(position).getTypeVestitiToString());

        // Set hashtags
        photoViewHolder.txtHashtags.setText(photos.get(position).getHashtagToString());

        String username = ParseUser.getCurrentUser().getUsername();

        List likeUsers = photos.get(position).getLike();

        if (likeUsers != null && likeUsers.contains(username)) {
            photoViewHolder.likeImage.setColorFilter(Color.rgb(210, 36, 36));
        } else {
            photoViewHolder.likeImage.setColorFilter(Color.rgb(205, 205, 205));
        }

        photoViewHolder.txtLikeCount.setText(photos.get(position).getNumLike() + "");
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemNames;
        TextView txtHashtags;
        ImageView photo;
        TextView txtLikeCount;
        ImageView likeImage;

        PhotoViewHolder(View itemView, final String profilo) {
            super(itemView);

            txtItemNames = (TextView) itemView.findViewById(R.id.profile_uplaoded_photos_card_item_name);
            txtHashtags = (TextView) itemView.findViewById(R.id.profile_uploaded_photos_card_hashtags);
            photo = (ImageView) itemView.findViewById(R.id.profile_uploaded_photos_card_image);
            txtLikeCount = (TextView) itemView.findViewById(R.id.profile_uploaded_photos_card_like_count);
            likeImage = (ImageView) itemView.findViewById(R.id.profile_uploaded_photos_card_like_image);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profilo.equals("persona")) {
                        Intent intent = new Intent(UserProfileActivity.context, ImageFragment.class);
                        intent.putExtra("classe", "profilo");
                        intent.putExtra("position", PhotoViewHolder.this.getAdapterPosition());
                        UserProfileActivity.activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(ShopProfileActivity.context, ImageFragment.class);
                        intent.putExtra("classe", "profilo");
                        intent.putExtra("position", PhotoViewHolder.this.getAdapterPosition());
                        ShopProfileActivity.activity.startActivity(intent);
                    }
                }
            });

            likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Image image = ProfileUploadedPhotosAdapter.photos.get(PhotoViewHolder.this.getAdapterPosition());

                    final boolean add = !image.getLike().contains(username);
                    if (add) {
                        // Log.d("ProfileUploadedPhotos", "Adding...");
                        image.addLike(username);
                    } else {
                        // Log.d("ProfileUploadedPhotos", "Removing...");
                        image.remLike(username);
                    }

                    notifyDataSetChanged();

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");

                    query.getInBackground(image.getObjectId(), new GetCallback<ParseObject>() {
                        public void done(ParseObject photo, ParseException e) {
                            if (e == null) {
                                if (add) {
                                    photo.addUnique("like", username);
                                    photo.put("nLike", photo.getInt("nLike") + 1);
                                    photo.saveInBackground();
                                } else {
                                    photo.removeAll("like", Collections.singletonList(username));
                                    photo.put("nLike", photo.getInt("nLike") - 1);
                                    photo.saveInBackground();
                                }
                            } else {
                                Log.d("ProfileUploadedPhotos", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            });
        }
    }

}
