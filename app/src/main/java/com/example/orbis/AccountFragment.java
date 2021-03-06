package com.example.orbis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    View view;
    MainActivity main; //stores our main activity
    API api;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        main = ((MainActivity) getActivity());
        api = new API(main);
        getUser();
        getLatestMem();
        getLatestPhoto();


        Button followersButton = view.findViewById(R.id.followersButton); //get cancel button by view ID
        Button followingButton = view.findViewById(R.id.followingButton); //get cancel button by view ID
        Button allmemoryButton = view.findViewById(R.id.allmemButton); //get cancel button by view ID
        Button diaryButton = view.findViewById(R.id.allmemButton); //get cancel button by view ID
        Button editprofButton = view.findViewById(R.id.editprofButton); //get cancel button by view ID


        //create listener
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), FollowersActivity.class);
                startActivity(intent);
            }

        });

        //edit profile button
        editprofButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new EditFragment()).commit();
            }
        });

        //create listener
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity().getApplication(), FollowingActivity.class);
                startActivity(intent1);
            }

        });

        allmemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new DiaryFragment()).addToBackStack(null).commit();
            }
        });
        //create listener
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DiaryFragment();
                main.goToFragment(fragment, 1);
            }

        });


        ImageButton opensettingsbutton = (ImageButton) view.findViewById(R.id.opensettingsbutton);
        opensettingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
            }
        });
        return view;
    }

    private void getUser() {
        String url = "user/get/";

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onUsernameResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void onUsernameResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if (!error.getBoolean("error")) {

            TextView username = view.findViewById(R.id.usernameText);
            username.setText(data.getString("username"));

            TextView name = view.findViewById(R.id.fullnameText);
            name.setText(data.getString("name"));

            TextView bio = view.findViewById(R.id.bioText);
            bio.setText(data.getString("bio"));

            JSONObject image1 = data.getJSONObject("image");

            ImageView profilepic = view.findViewById(R.id.profilepicView);

            Picasso.get()
                    .load(image1.getString("uri"))
                    .into(profilepic);


        }
    }

    private void getLatestMem() {
        String url = "memory/get/";

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onRecentResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onRecentResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if (!error.getBoolean("error")) {

            TextView title = view.findViewById(R.id.titleAccount);
            title.setText(data.getString("title"));

            TextView description = view.findViewById(R.id.descriptionAccount);
            description.setText(data.getString("description"));

            TextView date = view.findViewById(R.id.dateAccount);
            date.setText(data.getString("datetime"));

        }
    }

    private void getLatestPhoto() {
        String url = "memory/get/";

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onPhotoResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onPhotoResult (JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if (!error.getBoolean("error")) {

            JSONObject image2 = data.getJSONObject("images").getJSONObject("0");

            ImageView latestmemImage = view.findViewById(R.id.latestmemImage);

            Picasso.get()
                    .load(image2.getString("uri"))
                    .into(latestmemImage);
        }
    }
}
