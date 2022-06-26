package com.example.epicture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;


public class ApiRequest {

    private String access_token;
    private Activity context;
    private GlobalData.RequestType request_type;
    private String url;
    private List<Photo> photos;

    public interface RequestCallback {
        void onRequestResult(Request request);
    }

    public ApiRequest(String access_token, GlobalData.RequestType request, String url, Activity context) {
        this.photos = new ArrayList<Photo>();
        this.access_token = access_token;
        this.request_type = request;
        this.url = url;
        this.context = context;

        if (request == GlobalData.RequestType.UserPhotos)
            getPhotos();
        switch (request) {
            case UserPhotos:
                getPhotos();
                break;
            case UserFav:
                getFav();
                break;
            case ImgFav:
                postFav();
                break;
            case Search:
                search();
                break;
            case Get:
                getRequest();
                break;
        }
    }

    private void getPhotos() {
        Request request = new Request.Builder()
                .url(context.getString(R.string.urlAccountImg))
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        new ApiCallback(context, request, request_type);
    }

    private void getFav() {
        Request request = new Request.Builder()
                .url(context.getString(R.string.urlFavImg) + GlobalData.getAccount_username() + "/favorites/")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        new ApiCallback(context, request, request_type);
    }

    private void postFav() {
        RequestBody body = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder()
                .url(context.getString(R.string.imgurApi) + url + "/favorite")
                .header("Authorization", "Bearer " + access_token)
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
            }
        });
    }

    private void getRequest() {
        Request request = new Request.Builder()
                .url(context.getString(R.string.imgurApi) + url)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        new ApiCallback(context, request, GlobalData.RequestType.Search);
    }

    private void search() {
        String search = url;

        search = search.replaceAll("\\p{Blank}","+");
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/search/time/weak/1?q=" + search)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        new ApiCallback(context, request, request_type);
    }
}