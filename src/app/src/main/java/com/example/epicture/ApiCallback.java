package com.example.epicture;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.squareup.okhttp.OkHttpClient;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ApiCallback {

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        TextView views;
        ImageButton fav;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    private void render(final List<Photo> photos, final Activity context) {

        RecyclerView rv = context.findViewById(R.id.recyclerView);
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(context));
            RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
                @NonNull
                @Override
                public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    PhotoVH vh = new PhotoVH(context.getLayoutInflater().inflate(R.layout.card, null));
                    vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                    vh.views = (TextView) vh.itemView.findViewById(R.id.views);
                    vh.fav = (ImageButton) vh.itemView.findViewById(R.id.favBtn);
                    return vh;
                }

                @Override
                public void onBindViewHolder(@NonNull final PhotoVH holder, final int position) {
                    Picasso.get().load(photos.get(position).link).into(holder.photo);
                    String views = photos.get(position).views + " " + context.getString(R.string.views);
                    holder.views.setText(views);
                    if (photos.get(position).fav)
                        holder.fav.setImageResource(R.drawable.ic_favorite_red_24dp);
                    holder.fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (photos.get(position).fav) {
                                holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                photos.get(position).fav = false;
                                if (photos.get(position).is_album)
                                    new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.ImgFav, "album/" + photos.get(position).id_album, context);
                                else
                                    new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.ImgFav, "image/" + photos.get(position).id, context);
                            }
                            else {
                                holder.fav.setImageResource(R.drawable.ic_favorite_red_24dp);
                                photos.get(position).fav = true;
                                if (photos.get(position).is_album)
                                    new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.ImgFav, "album/" + photos.get(position).id_album, context);
                                else
                                    new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.ImgFav, "image/" + photos.get(position).id, context);
                            }
                        }
                    });
                }

                @Override
                public int getItemCount() {
                    return photos.size();
                }
            };
            rv.setAdapter(adapter);
            rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 16;
                }
            });
        }
    }

    private void UserPhotosCallback(final Activity context, Request request) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final List<Photo> photos = new ArrayList<Photo>();
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONArray items = data.getJSONArray("data");

                    for (int i = 0; i < items.length(); i++) {
                        try {
                            Photo photo = new Photo();
                            JSONObject obj = items.getJSONObject(i);
                            photo.is_album = false;
                            photo.id = obj.getString("id");
                            photo.link = obj.getString("link");
                            photo.title = obj.getString("title");
                            photo.views = obj.getString("views");
                            photo.fav = obj.getBoolean("favorite");
                            photos.add(photo);
                        } catch (Exception e) {}
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            render(photos, context);
                        }
                    });
                } catch (Exception e) {}
            }
        });
    }

    private void imgurImgCallback(final Activity context, Request request) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final List<Photo> photos = new ArrayList<Photo>();
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONArray items = data.getJSONArray("data");

                    for (int i = 0; i < items.length(); i++) {
                        try {
                            Photo photo = new Photo();
                            JSONObject obj = items.getJSONObject(i);
                            photo.id = obj.getString("cover");
                            photo.title = obj.getString("title");
                            photo.views = obj.getString("views");
                            photo.fav = obj.getBoolean("favorite");
                            photo.is_album = obj.getBoolean("is_album");
                            photo.id_album = obj.getString("id");
                            photo.link = context.getString(R.string.urlImg) + obj.getString("cover") + ".jpeg";
                            photos.add(photo);
                        } catch (Exception e) {}
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            render(photos, context);
                        }
                    });

                } catch (Exception e) {}
            }
        });
    }

    public ApiCallback(final Activity context, Request request, GlobalData.RequestType type) {
        switch (type) {
            case UserPhotos:
                UserPhotosCallback(context, request);
                break;
            case UserFav:
                imgurImgCallback(context, request);
                break;
            case Search:
                imgurImgCallback(context, request);
                break;
        }
    }


}
