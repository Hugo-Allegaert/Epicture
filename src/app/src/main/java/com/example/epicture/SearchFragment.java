package com.example.epicture;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.security.AccessController;

import static android.content.ContentValues.TAG;


public class SearchFragment extends Fragment {

    Intent research;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        final EditText text = (EditText) view.findViewById(R.id.recherche);
        final Activity activity = getActivity();
        new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.Get, "gallery/user/viral/day/0?showViral=true&mature=true", activity);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Log.i("TAG", "Ce que j'ai tapé est :" + text.getText().toString());
                    new ApiRequest(GlobalData.getAccess_token(), GlobalData.RequestType.Search, text.getText().toString(), activity);
                    return true;
                }
                return false;
            }
        });
        return (view);
    }
}