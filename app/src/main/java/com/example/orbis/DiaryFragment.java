package com.example.orbis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiaryFragment extends Fragment {
    API api;
    View view;

    enum ORDER
    {
        OLD, NEW;
    }

    public ArrayList<DiaryItems> exampleList;

    private RecyclerView mRecyclerView;
    private DiaryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);
        main = ((MainActivity) getActivity());

        api = new API(main);

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolDiary);
        toolbar.inflateMenu(R.menu.diary_menu); //setup menu
        toolbar.setTitle(R.string.diary_screen_toolbar_title);
        main.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        //get diary first page, no searchstring and oprder by new
        getDiary(1, "", ORDER.NEW);

/*
        exampleList = new ArrayList<>();
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "I'm going on an adventure!", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Auckland", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "New-Zealand", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "London", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 5", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Extra 6", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "Extra 7", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 8", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 9", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Extra 10", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "Extra 11", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 12", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));
*/

        return view;
    }

    /**
     *
     * @param page this is the page (page 1 = first ten, page 2 next ten etc...)
     * @param searchString searchstring, can be left empty to show all results
     * @param order order of search from old to new or new to old
     */
    public void getDiary(int page, String searchString, ORDER order) {
        String url = "diary/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("page", page);
            jsonBody.put("search", searchString);
            jsonBody.put("order", ((order == ORDER.OLD) ? "old" : "new"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onDiaryResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onDiaryResponse(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            exampleList = new ArrayList<>();

            JSONArray key = data.names();
            if(key != null) {
                for (int i = 0; i < key.length (); ++i) {
                    String keys = key.getString(i);
                    JSONObject memory = data.getJSONObject(keys);

                    JSONObject image = new JSONObject();
                    if(!memory.isNull("image")) {
                        image = memory.getJSONObject("image");
                    } else {
                        image.put("uri", "");
                    }

                    exampleList.add(new DiaryItems(
                            memory.getInt("id"),
                            image.getString("uri"),
                            memory.getString("title"),
                            memory.getString("description"),
                            memory.getString("datetime")));
                }

                mRecyclerView = view.findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getActivity());
                mAdapter = new DiaryAdapter(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new DiaryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    DiaryItems diaryItems =  exampleList.get(position);
                    Fragment mFragment = new MemoryFragment();

                    //Pass the ID to the memory
                    Bundle bundle = new Bundle(); //bundle stores stuff we want to give to memory
                    bundle.putInt("id", diaryItems.getId()); //the id of the memory
                    mFragment.setArguments(bundle); //set the bundle to the arguments of the memory so we can access it from there

                    main.goToFragment(mFragment, 1);
                    }
                });
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.diary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.searchDiary);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("Search", s);
                getDiary(1, s, ORDER.NEW);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDiary(1, "", ORDER.NEW);
                return false;
            }
        });
    }
}

    //SearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                mAdapter.getFilter().filter(s);
//                return false;





