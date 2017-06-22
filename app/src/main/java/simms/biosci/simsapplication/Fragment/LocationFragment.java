package simms.biosci.simsapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.Activity.AddLocationActivity;
import simms.biosci.simsapplication.Adapter.LocationSearchAdapter;
import simms.biosci.simsapplication.Adapter.LocationSearchTableAdapter;
import simms.biosci.simsapplication.Manager.IntentIntegrator;
import simms.biosci.simsapplication.Manager.IntentResult;
import simms.biosci.simsapplication.Manager.OnItemClickListener;
import simms.biosci.simsapplication.Object.FeedCross;
import simms.biosci.simsapplication.Object.FeedGermplasm;
import simms.biosci.simsapplication.Object.FeedLocation;
import simms.biosci.simsapplication.Object.FeedSource;
import simms.biosci.simsapplication.R;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class LocationFragment extends Fragment {

    private SharedPreferences display_read;
    private Boolean card_view_type;
    private Typeface montserrat_regular, montserrat_bold;
    private FloatingSearchView floating_search_view;
    private TextView tv_title;
    private SheetLayout bottom_sheet;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_ADD = 2;
    private static final int REQUEST_CODE_SHOW = 5;
    private RecyclerView recyclerView_location;
    private LocationSearchAdapter locationSearchAdapter;
    private LocationSearchTableAdapter locationSearchTableAdapter;
    private List<FeedLocation> feedLocations;
    private DatabaseReference mRootRef, mLocationRef;
    private IntentIntegrator scanIntegrator;

    public LocationFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

        scanIntegrator = new IntentIntegrator(this);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("location").orderByChild("l_name").addChildEventListener(locationEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-SemiBold.ttf");

        floating_search_view = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        bottom_sheet = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView_location = (RecyclerView) rootView.findViewById(R.id.recycler_view_location);
        tv_title.setTypeface(montserrat_bold);

        bottom_sheet.setFab(fab);
        feedLocations = new ArrayList<>();
        display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
        card_view_type = display_read.getBoolean("card_view_type", true);
        if (card_view_type) {
            locationSearchAdapter = new LocationSearchAdapter(getContext(), feedLocations);
            recyclerView_location.setAdapter(locationSearchAdapter);
            locationSearchAdapter.setOnItemClickListener(onItemClickListener);
        } else {
            locationSearchTableAdapter = new LocationSearchTableAdapter(getContext(), feedLocations);
            recyclerView_location.setAdapter(locationSearchTableAdapter);
            locationSearchTableAdapter.setOnItemClickListener(onItemClickListener);
        }


        recyclerView_location.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setAutoMeasureEnabled(false);
        recyclerView_location.setLayoutManager(llm);

        bottom_sheet.setFabAnimationEndListener(fab_animation_click);
        fab.setOnClickListener(fab_click);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (card_view_type) {
                    locationSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                } else {
                    locationSearchTableAdapter.getFilter().filter(newQuery.toLowerCase());
                }
            }
        });

        floating_search_view.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.search_camera) {
                    scanIntegrator.initiateScan();
                } else if (item.getItemId() == R.id.search_barcode) {
                    Toast.makeText(getContext(), "Barcode launch.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    SheetLayout.OnFabAnimationEndListener fab_animation_click = new SheetLayout.OnFabAnimationEndListener() {
        @Override
        public void onFabAnimationEnd() {
            Intent intent = new Intent(getContext(), AddLocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }
    };

    View.OnClickListener fab_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottom_sheet.expandFab();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD) {
            bottom_sheet.contractFab();
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                IntentResult intentResult =
                        IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (intentResult != null) {

                    String contents = intentResult.getContents();
                    String format = intentResult.getFormatName();

                    floating_search_view.setSearchText(contents);
                    if (card_view_type) {
                        locationSearchAdapter.getFilter().filter(contents.toLowerCase());
                    } else {
                        locationSearchTableAdapter.getFilter().filter(contents.toLowerCase());
                    }
                    Log.d("SEARCH_EAN", "OK, EAN: " + contents + ", FORMAT: " + format);
                } else {
                    Log.e("SEARCH_EAN", "IntentResult je NULL!");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("SEARCH_EAN", "CANCEL");
            }
        } else if (requestCode == REQUEST_CODE_SHOW) {
            try {
                String what2do = data.getStringExtra("what2do");
                if (what2do.toString().equals("update")) {
                    String l_name = data.getStringExtra("l_name");
                    String l_province = data.getStringExtra("l_province");
                    String l_district = data.getStringExtra("l_district");
                    String l_sub_district = data.getStringExtra("l_sub_district");
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedLocations.size(); i++) {
                        if (feedLocations.get(i).getL_key().equals(key)) {
                            feedLocations.get(i).setL_name(l_name + "");
                            feedLocations.get(i).setL_province(l_province + "");
                            feedLocations.get(i).setL_district(l_district + "");
                            feedLocations.get(i).setL_sub_district(l_sub_district + "");
                            if (card_view_type) {
                                locationSearchAdapter.notifyDataSetChanged();
                                locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                            } else {
                                locationSearchTableAdapter.notifyDataSetChanged();
                                locationSearchTableAdapter.notifyItemRangeChanged(i, feedLocations.size());
                            }
                        }
                    }
                } else if (what2do.toString().equals("delete")) {
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedLocations.size(); i++) {
                        if (feedLocations.get(i).getL_key().equals(key)) {
                            feedLocations.remove(i);
                            if(card_view_type){
                                locationSearchAdapter.notifyItemRemoved(i);
                                locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                            } else{
                                locationSearchTableAdapter.notifyItemRemoved(i);
                                locationSearchTableAdapter.notifyItemRangeChanged(i, feedLocations.size());
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }

        }
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onGermplasmClick(FeedGermplasm item) {

        }

        @Override
        public void onLocationClick(FeedLocation item) {
            Intent intent = new Intent(getContext(), AddLocationActivity.class);
            intent.putExtra("KEY", item.getL_key());
            intent.putExtra("REQUEST_CODE", REQUEST_CODE_SHOW);
            startActivityForResult(intent, REQUEST_CODE_SHOW);
        }

        @Override
        public void onSourceClick(FeedSource item) {

        }

        @Override
        public void onCrossClick(FeedCross item) {

        }
    };

    ChildEventListener locationEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                try {
                    FeedLocation model = dataSnapshot.getValue(FeedLocation.class);
                    feedLocations.add(model);
                    if(card_view_type){
                        locationSearchAdapter.notifyItemInserted(feedLocations.size() - 1);
                    } else{
                        locationSearchTableAdapter.notifyItemInserted(feedLocations.size() - 1);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            FeedLocation p0 = dataSnapshot.getValue(FeedLocation.class);
            for (int i = 0; i < feedLocations.size(); i++) {
                if (feedLocations.get(i).getL_key().equals(p0.getL_key())) {
                    feedLocations.get(i).setL_name(p0.getL_name());
                    feedLocations.get(i).setL_province(p0.getL_province());
                    feedLocations.get(i).setL_district(p0.getL_district());
                    feedLocations.get(i).setL_sub_district(p0.getL_sub_district());
                    if(card_view_type){
                        locationSearchAdapter.notifyDataSetChanged();
                        locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    } else{
                        locationSearchTableAdapter.notifyDataSetChanged();
                        locationSearchTableAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedLocation p0 = dataSnapshot.getValue(FeedLocation.class);
            for (int i = 0; i < feedLocations.size(); i++) {
                if (feedLocations.get(i).getL_key().equals(p0.getL_key())) {
                    feedLocations.remove(i);
                    if(card_view_type){
                        locationSearchAdapter.notifyItemRemoved(i);
                        locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    } else{
                        locationSearchTableAdapter.notifyItemRemoved(i);
                        locationSearchTableAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    }
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, databaseError.getMessage());
        }
    };
}
