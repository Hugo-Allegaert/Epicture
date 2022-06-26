package com.example.epicture;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {
    private Button btnUpload;
    private EditText title;
    private ImageView selectedImg;
    static final int RESULT_LOAD_IMG = 1;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        btnUpload = view.findViewById(R.id.upload_img);
        selectedImg = view.findViewById(R.id.selected);
        title = view.findViewById(R.id.titre_photo);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        return (view);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, final Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), this.getActivity().getString(R.string.errorAppend), Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getActivity(), this.getActivity().getString(R.string.noImageChoosen), Toast.LENGTH_LONG).show();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri test = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(test,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(picturePath);
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", title.getText().toString(), RequestBody.create(MEDIA_TYPE_PNG, file))
                        .addFormDataPart("title", title.getText().toString())
                        .build();

                 Request request = new Request.Builder()
                         .header("Authorization", "Bearer " + GlobalData.getAccess_token())
                         .url("https://api.imgur.com/3/image")
                         .method("POST", body)
                         .build();

                OkHttpClient client = new OkHttpClient();
                try {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            getActivity().finish();
                            startActivity(getActivity().getIntent());
                        }
                    });
                } catch (Exception e) {}
            }
        });
    }
}

