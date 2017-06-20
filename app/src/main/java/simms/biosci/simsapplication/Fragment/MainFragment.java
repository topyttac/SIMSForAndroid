package simms.biosci.simsapplication.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.Activity.AddCrossActivity;
import simms.biosci.simsapplication.Activity.AddGermplasmActivity;
import simms.biosci.simsapplication.Activity.AddLocationActivity;
import simms.biosci.simsapplication.Activity.AddSourceActivity;
import simms.biosci.simsapplication.Adapter.CrossSearchAdapter;
import simms.biosci.simsapplication.Object.FeedCross;
import simms.biosci.simsapplication.Object.FeedGermplasm;
import simms.biosci.simsapplication.Object.FeedLocation;
import simms.biosci.simsapplication.Object.FeedSource;
import simms.biosci.simsapplication.Adapter.GermplasmSearchAdapter;
import simms.biosci.simsapplication.Adapter.LocationSearchAdapter;
import simms.biosci.simsapplication.Manager.OnItemClickListener;
import simms.biosci.simsapplication.Adapter.SourceSearchAdapter;
import simms.biosci.simsapplication.R;

import static android.content.ContentValues.TAG;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {

    private TextView tv_germplasm, tv_location, tv_source, tv_cross;
    private TextView tv_result_germplasm, tv_loading_germplasm, tv_result_location, tv_loading_location,
            tv_result_source, tv_loading_source, tv_result_cross, tv_loading_cross;
    private Typeface montserrat_regular, montserrat_bold;
    private FloatingSearchView floating_search_view;
    private DatabaseReference mRootRef;
    private RecyclerView recyclerView_germplasm;
    private RecyclerView recyclerView_location;
    private RecyclerView recyclerView_source;
    private RecyclerView recyclerView_cross;
    private GermplasmSearchAdapter germplasmSearchAdapter;
    private LocationSearchAdapter locationSearchAdapter;
    private SourceSearchAdapter sourceSearchAdapter;
    private CrossSearchAdapter crossSearchAdapter;
    private List<FeedGermplasm> feedGermplasm;
    private List<FeedLocation> feedLocations;
    private List<FeedSource> feedSources;
    private List<FeedCross> feedCrosses;
    private static final int REQUEST_CODE_SHOW = 4;
    private int time = 100;

    public MainFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("germplasm").orderByChild("g_name").addChildEventListener(germplasmEventListener);
        mRootRef.child("location").orderByChild("l_name").addChildEventListener(locationEventListener);
        mRootRef.child("source").orderByChild("s_name").addChildEventListener(sourceEventListener);
        mRootRef.child("cross").orderByChild("c_name").addChildEventListener(crossEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

        tv_germplasm = (TextView) rootView.findViewById(R.id.tv_germplasm);
        tv_location = (TextView) rootView.findViewById(R.id.tv_location);
        tv_source = (TextView) rootView.findViewById(R.id.tv_source);
        tv_cross = (TextView) rootView.findViewById(R.id.tv_cross);
        tv_result_germplasm = (TextView) rootView.findViewById(R.id.tv_result_germplasm);
        tv_loading_germplasm = (TextView) rootView.findViewById(R.id.tv_loading_germplasm);
        tv_result_location = (TextView) rootView.findViewById(R.id.tv_result_location);
        tv_loading_location = (TextView) rootView.findViewById(R.id.tv_loading_location);
        tv_result_source = (TextView) rootView.findViewById(R.id.tv_result_source);
        tv_loading_source = (TextView) rootView.findViewById(R.id.tv_loading_source);
        tv_result_cross = (TextView) rootView.findViewById(R.id.tv_result_cross);
        tv_loading_cross = (TextView) rootView.findViewById(R.id.tv_loading_cross);
        floating_search_view = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
        recyclerView_germplasm = (RecyclerView) rootView.findViewById(R.id.recycler_view_germplasm);
        recyclerView_location = (RecyclerView) rootView.findViewById(R.id.recycler_view_location);
        recyclerView_source = (RecyclerView) rootView.findViewById(R.id.recycler_view_source);
        recyclerView_cross = (RecyclerView) rootView.findViewById(R.id.recycler_view_cross);

        tv_germplasm.setTypeface(montserrat_bold);
        tv_location.setTypeface(montserrat_bold);
        tv_source.setTypeface(montserrat_bold);
        tv_cross.setTypeface(montserrat_bold);
        tv_result_germplasm.setTypeface(montserrat_bold);
        tv_loading_germplasm.setTypeface(montserrat_bold);
        tv_loading_germplasm.setVisibility(View.VISIBLE);
        tv_result_location.setTypeface(montserrat_bold);
        tv_loading_location.setTypeface(montserrat_bold);
        tv_loading_location.setVisibility(View.VISIBLE);
        tv_result_source.setTypeface(montserrat_bold);
        tv_loading_source.setTypeface(montserrat_bold);
        tv_loading_source.setVisibility(View.VISIBLE);
        tv_result_cross.setTypeface(montserrat_bold);
        tv_loading_cross.setTypeface(montserrat_bold);
        tv_loading_cross.setVisibility(View.VISIBLE);
        feedGermplasm = new ArrayList<>();
        feedLocations = new ArrayList<>();
        feedSources = new ArrayList<>();
        feedCrosses = new ArrayList<>();

        new CountDownTimer(800, 1000) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tv_loading_germplasm.setVisibility(View.INVISIBLE);
                tv_loading_location.setVisibility(View.INVISIBLE);
                tv_loading_source.setVisibility(View.INVISIBLE);
                tv_loading_cross.setVisibility(View.INVISIBLE);
            }

        }.start();

        germplasmSearchAdapter = new GermplasmSearchAdapter(getContext(), feedGermplasm);
        recyclerView_germplasm.setNestedScrollingEnabled(false);
        recyclerView_germplasm.setAdapter(germplasmSearchAdapter);
        recyclerView_germplasm.setHasFixedSize(true);
        LinearLayoutManager llm_germplasm = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_germplasm.setAutoMeasureEnabled(false);
        recyclerView_germplasm.setLayoutManager(llm_germplasm);

        locationSearchAdapter = new LocationSearchAdapter(getContext(), feedLocations);
        recyclerView_location.setNestedScrollingEnabled(false);
        recyclerView_location.setAdapter(locationSearchAdapter);
        recyclerView_location.setHasFixedSize(true);
        LinearLayoutManager llm_location = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_location.setAutoMeasureEnabled(false);
        recyclerView_location.setLayoutManager(llm_location);

        sourceSearchAdapter = new SourceSearchAdapter(getContext(), feedSources);
        recyclerView_source.setNestedScrollingEnabled(false);
        recyclerView_source.setAdapter(sourceSearchAdapter);
        recyclerView_source.setHasFixedSize(true);
        LinearLayoutManager llm_source = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_source.setAutoMeasureEnabled(false);
        recyclerView_source.setLayoutManager(llm_source);

        crossSearchAdapter = new CrossSearchAdapter(getContext(), feedCrosses);
        recyclerView_cross.setNestedScrollingEnabled(false);
        recyclerView_cross.setAdapter(crossSearchAdapter);
        recyclerView_cross.setHasFixedSize(true);
        LinearLayoutManager llm_cross = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_cross.setAutoMeasureEnabled(false);
        recyclerView_cross.setLayoutManager(llm_cross);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                germplasmSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                locationSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                sourceSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                crossSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                if (germplasmSearchAdapter.getItemCount() == 0) {
                    tv_result_germplasm.setVisibility(View.VISIBLE);
                } else {
                    tv_result_germplasm.setVisibility(View.INVISIBLE);
                }
                if (locationSearchAdapter.getItemCount() == 0) {
                    tv_result_location.setVisibility(View.VISIBLE);
                } else {
                    tv_result_location.setVisibility(View.INVISIBLE);
                }
                if (sourceSearchAdapter.getItemCount() == 0) {
                    tv_result_source.setVisibility(View.VISIBLE);
                } else {
                    tv_result_source.setVisibility(View.INVISIBLE);
                }
                if (crossSearchAdapter.getItemCount() == 0) {
                    tv_result_cross.setVisibility(View.VISIBLE);
                } else {
                    tv_result_cross.setVisibility(View.INVISIBLE);
                }
            }
        });

        floating_search_view.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.search_camera) {
                    Toast.makeText(getContext(), "Camera launch.", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.search_barcode) {
                    Toast.makeText(getContext(), "Barcode launch.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        germplasmSearchAdapter.setOnItemClickListener(germplasmClickListener);
        locationSearchAdapter.setOnItemClickListener(locationClickListener);
        sourceSearchAdapter.setOnItemClickListener(sourceClickListener);
        crossSearchAdapter.setOnItemClickListener(crossClickListener);
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

    ChildEventListener germplasmEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                FeedGermplasm model = dataSnapshot.getValue(FeedGermplasm.class);
                feedGermplasm.add(model);
                germplasmSearchAdapter.notifyItemInserted(feedGermplasm.size() - 1);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            FeedGermplasm p0 = dataSnapshot.getValue(FeedGermplasm.class);
            for (int i = 0; i < feedGermplasm.size(); i++) {
                if (feedGermplasm.get(i).getG_key().equals(p0.getG_key())) {
                    feedGermplasm.get(i).setG_name(p0.getG_name());
                    feedGermplasm.get(i).setG_cross(p0.getG_cross());
                    feedGermplasm.get(i).setG_source(p0.getG_source());
                    feedGermplasm.get(i).setG_lot(p0.getG_lot());
                    feedGermplasm.get(i).setG_location(p0.getG_location());
                    feedGermplasm.get(i).setG_stock(p0.getG_stock());
                    feedGermplasm.get(i).setG_balance(p0.getG_balance());
                    feedGermplasm.get(i).setG_room(p0.getG_room());
                    feedGermplasm.get(i).setG_shelf(p0.getG_shelf());
                    feedGermplasm.get(i).setG_row(p0.getG_row());
                    feedGermplasm.get(i).setG_box(p0.getG_box());
                    feedGermplasm.get(i).setG_note(p0.getG_note());
                    germplasmSearchAdapter.notifyDataSetChanged();
                    germplasmSearchAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedGermplasm p0 = dataSnapshot.getValue(FeedGermplasm.class);
            for (int i = 0; i < feedGermplasm.size(); i++) {
                if (feedGermplasm.get(i).getG_key().equals(p0.getG_key())) {
                    feedGermplasm.remove(i);
                    germplasmSearchAdapter.notifyItemRemoved(i);
                    germplasmSearchAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
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

    OnItemClickListener germplasmClickListener = new OnItemClickListener() {
        @Override
        public void onGermplasmClick(FeedGermplasm item) {
            Intent intent = new Intent(getContext(), AddGermplasmActivity.class);
            intent.putExtra("KEY", item.getG_key());
            Log.i("key", item.getG_key() + "");
            intent.putExtra("REQUEST_CODE", 4);
            startActivityForResult(intent, 4);
        }

        @Override
        public void onLocationClick(FeedLocation item) {

        }

        @Override
        public void onSourceClick(FeedSource item) {

        }

        @Override
        public void onCrossClick(FeedCross item) {

        }
    };
    OnItemClickListener locationClickListener = new OnItemClickListener() {
        @Override
        public void onGermplasmClick(FeedGermplasm item) {

        }

        @Override
        public void onLocationClick(FeedLocation item) {
            Intent intent = new Intent(getContext(), AddLocationActivity.class);
            intent.putExtra("KEY", item.getL_key());
            intent.putExtra("REQUEST_CODE", 5);
            startActivityForResult(intent, 5);
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
                    locationSearchAdapter.notifyItemInserted(feedLocations.size() - 1);
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
                    locationSearchAdapter.notifyDataSetChanged();
                    locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedLocation p0 = dataSnapshot.getValue(FeedLocation.class);
            for (int i = 0; i < feedLocations.size(); i++) {
                if (feedLocations.get(i).getL_key().equals(p0.getL_key())) {
                    feedLocations.remove(i);
                    locationSearchAdapter.notifyItemRemoved(i);
                    locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
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

    ChildEventListener sourceEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                try {
                    FeedSource model = dataSnapshot.getValue(FeedSource.class);
                    feedSources.add(model);
                    sourceSearchAdapter.notifyItemInserted(feedSources.size() - 1);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            FeedSource p0 = dataSnapshot.getValue(FeedSource.class);
            for (int i = 0; i < feedSources.size(); i++) {
                if (feedSources.get(i).getS_key().equals(p0.getS_key())) {
                    feedSources.get(i).setS_name(p0.getS_name());
                    feedSources.get(i).setS_desc(p0.getS_desc());
                    sourceSearchAdapter.notifyDataSetChanged();
                    sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedSource p0 = dataSnapshot.getValue(FeedSource.class);
            for (int i = 0; i < feedSources.size(); i++) {
                if (feedSources.get(i).getS_key().equals(p0.getS_key())) {
                    feedSources.remove(i);
                    sourceSearchAdapter.notifyItemRemoved(i);
                    sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
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

    OnItemClickListener sourceClickListener = new OnItemClickListener() {
        @Override
        public void onGermplasmClick(FeedGermplasm item) {

        }

        @Override
        public void onLocationClick(FeedLocation item) {

        }

        @Override
        public void onSourceClick(FeedSource item) {
            Intent intent = new Intent(getContext(), AddSourceActivity.class);
            intent.putExtra("KEY", item.getS_key());
            intent.putExtra("REQUEST_CODE", 6);
            startActivityForResult(intent, 6);
        }

        @Override
        public void onCrossClick(FeedCross item) {

        }
    };
    ChildEventListener crossEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                try {
                    FeedCross model = dataSnapshot.getValue(FeedCross.class);
                    feedCrosses.add(model);
                    crossSearchAdapter.notifyItemInserted(feedCrosses.size() - 1);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            FeedCross p0 = dataSnapshot.getValue(FeedCross.class);
            for (int i = 0; i < feedCrosses.size(); i++) {
                if (feedCrosses.get(i).getC_key().equals(p0.getC_key())) {
                    feedCrosses.get(i).setC_name(p0.getC_name());
                    feedCrosses.get(i).setC_desc(p0.getC_desc());
                    crossSearchAdapter.notifyDataSetChanged();
                    crossSearchAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedCross p0 = dataSnapshot.getValue(FeedCross.class);
            for (int i = 0; i < feedCrosses.size(); i++) {
                if (feedCrosses.get(i).getC_key().equals(p0.getC_key())) {
                    feedCrosses.remove(i);
                    crossSearchAdapter.notifyItemRemoved(i);
                    crossSearchAdapter.notifyItemRangeChanged(i, feedCrosses.size());
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

    OnItemClickListener crossClickListener = new OnItemClickListener() {
        @Override
        public void onGermplasmClick(FeedGermplasm item) {

        }

        @Override
        public void onLocationClick(FeedLocation item) {

        }

        @Override
        public void onSourceClick(FeedSource item) {

        }

        @Override
        public void onCrossClick(FeedCross item) {
            Intent intent = new Intent(getContext(), AddCrossActivity.class);
            intent.putExtra("KEY", item.getC_key());
            intent.putExtra("REQUEST_CODE", 8);
            startActivityForResult(intent, 8);
        }
    };
}
