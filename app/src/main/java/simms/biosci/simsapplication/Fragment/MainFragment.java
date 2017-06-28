package simms.biosci.simsapplication.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import simms.biosci.simsapplication.Adapter.CrossSearchTableAdapter;
import simms.biosci.simsapplication.Adapter.GermplasmSearchAdapter;
import simms.biosci.simsapplication.Adapter.GermplasmSearchTableAdapter;
import simms.biosci.simsapplication.Adapter.LocationSearchAdapter;
import simms.biosci.simsapplication.Adapter.LocationSearchTableAdapter;
import simms.biosci.simsapplication.Adapter.SourceSearchAdapter;
import simms.biosci.simsapplication.Adapter.SourceSearchTableAdapter;
import simms.biosci.simsapplication.Manager.IntentIntegrator;
import simms.biosci.simsapplication.Manager.IntentResult;
import simms.biosci.simsapplication.Interface.OnItemClickListener;
import simms.biosci.simsapplication.Manager.ScannerInterface;
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
public class MainFragment extends Fragment {

    private SharedPreferences display_read, germplasm_one_read, germplasm_two_read;
    private Boolean card_view_type;
    private int germplasm_which_one, germplasm_which_two;
    private TextView tv_loading, tv_germplasm, tv_location, tv_source, tv_cross, tv_germplasm_header, tv_one, tv_two, tv_location_header,
            tv_address, tv_source_header, tv_source_desc, tv_cross_header, tv_cross_desc;
    private TextView tv_loading_germplasm, tv_loading_location, tv_loading_source, tv_loading_cross, tv_result;
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
    private GermplasmSearchTableAdapter germplasmSearchTableAdapter;
    private LocationSearchTableAdapter locationSearchTableAdapter;
    private SourceSearchTableAdapter sourceSearchTableAdapter;
    private CrossSearchTableAdapter crossSearchTableAdapter;
    private List<FeedGermplasm> feedGermplasm;
    private List<FeedLocation> feedLocations;
    private List<FeedSource> feedSources;
    private List<FeedCross> feedCrosses;
    private static final int REQUEST_CODE_SHOW = 4;
    private IntentIntegrator scanIntegrator;
    private LinearLayout ll_germplasm, ll_location, ll_source, ll_cross, ll_germplasm_header, ll_location_header,
            ll_source_header, ll_cross_header, ll_content;
    private ScannerInterface scanner;
    private IntentFilter intentFilter;
    private BroadcastReceiver scanReceiver;
    private boolean isContinue = false;
    private static final String RES_ACTION = "android.intent.action.SCANRESULT";
    private ContextWrapper contextWrapper;

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

        contextWrapper = new ContextWrapper(getContext());
        scanIntegrator = new IntentIntegrator(this);
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
        tv_loading_germplasm = (TextView) rootView.findViewById(R.id.tv_loading_germplasm);
        tv_loading_location = (TextView) rootView.findViewById(R.id.tv_loading_location);
        tv_loading_source = (TextView) rootView.findViewById(R.id.tv_loading_source);
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        tv_loading_cross = (TextView) rootView.findViewById(R.id.tv_loading_cross);
        floating_search_view = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
        recyclerView_germplasm = (RecyclerView) rootView.findViewById(R.id.recycler_view_germplasm);
        recyclerView_location = (RecyclerView) rootView.findViewById(R.id.recycler_view_location);
        recyclerView_source = (RecyclerView) rootView.findViewById(R.id.recycler_view_source);
        recyclerView_cross = (RecyclerView) rootView.findViewById(R.id.recycler_view_cross);
        ll_germplasm = (LinearLayout) rootView.findViewById(R.id.ll_germplasm);
        ll_location = (LinearLayout) rootView.findViewById(R.id.ll_location);
        ll_source = (LinearLayout) rootView.findViewById(R.id.ll_source);
        ll_cross = (LinearLayout) rootView.findViewById(R.id.ll_cross);
        tv_germplasm_header = (TextView) rootView.findViewById(R.id.tv_germplasm_header);
        tv_one = (TextView) rootView.findViewById(R.id.tv_one);
        tv_two = (TextView) rootView.findViewById(R.id.tv_two);
        ll_germplasm_header = (LinearLayout) rootView.findViewById(R.id.ll_germplasm_header);
        tv_location_header = (TextView) rootView.findViewById(R.id.tv_location_header);
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);
        ll_location_header = (LinearLayout) rootView.findViewById(R.id.ll_location_header);
        tv_source_header = (TextView) rootView.findViewById(R.id.tv_source_header);
        tv_source_desc = (TextView) rootView.findViewById(R.id.tv_source_desc);
        ll_source_header = (LinearLayout) rootView.findViewById(R.id.ll_source_header);
        tv_cross_header = (TextView) rootView.findViewById(R.id.tv_cross_header);
        tv_cross_desc = (TextView) rootView.findViewById(R.id.tv_cross_desc);
        ll_cross_header = (LinearLayout) rootView.findViewById(R.id.ll_cross_header);
        tv_loading = (TextView) rootView.findViewById(R.id.tv_loading);
        ll_content = (LinearLayout) rootView.findViewById(R.id.ll_content);

        ll_content.setVisibility(View.GONE);
        tv_germplasm.setTypeface(montserrat_bold);
        tv_location.setTypeface(montserrat_bold);
        tv_source.setTypeface(montserrat_bold);
        tv_cross.setTypeface(montserrat_bold);
        tv_germplasm_header.setTypeface(montserrat_bold);
        tv_one.setTypeface(montserrat_bold);
        tv_two.setTypeface(montserrat_bold);
        tv_location_header.setTypeface(montserrat_bold);
        tv_address.setTypeface(montserrat_bold);
        tv_source_header.setTypeface(montserrat_bold);
        tv_source_desc.setTypeface(montserrat_bold);
        tv_cross_header.setTypeface(montserrat_bold);
        tv_cross_desc.setTypeface(montserrat_bold);
        tv_loading_germplasm.setTypeface(montserrat_bold);
        tv_loading_germplasm.setVisibility(View.VISIBLE);
        tv_loading_location.setTypeface(montserrat_bold);
        tv_loading_location.setVisibility(View.VISIBLE);
        tv_loading_source.setTypeface(montserrat_bold);
        tv_loading_source.setVisibility(View.VISIBLE);
        tv_result.setTypeface(montserrat_bold);
        tv_loading_cross.setTypeface(montserrat_bold);
        tv_loading_cross.setVisibility(View.VISIBLE);
        tv_loading.setTypeface(montserrat_bold);
        feedGermplasm = new ArrayList<>();
        feedLocations = new ArrayList<>();
        feedSources = new ArrayList<>();
        feedCrosses = new ArrayList<>();

        String[] germplasm_columns = getResources().getStringArray(R.array.germplasm_column);
        germplasm_one_read = getActivity().getSharedPreferences("germplasm_which_one", MODE_PRIVATE);
        germplasm_which_one = germplasm_one_read.getInt("germplasm_which_one", 2);
        germplasm_two_read = getActivity().getSharedPreferences("germplasm_which_two", MODE_PRIVATE);
        germplasm_which_two = germplasm_two_read.getInt("germplasm_which_two", 4);
        tv_one.setText(germplasm_columns[germplasm_which_one]);
        tv_two.setText(germplasm_columns[germplasm_which_two]);

        initScanner();

        new CountDownTimer(800, 1000) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tv_loading_germplasm.setVisibility(View.GONE);
                tv_loading_location.setVisibility(View.GONE);
                tv_loading_source.setVisibility(View.GONE);
                tv_loading_cross.setVisibility(View.GONE);
                tv_loading.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
            }

        }.start();

        display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
        card_view_type = display_read.getBoolean("card_view_type", true);
        if (card_view_type) {
            germplasmSearchAdapter = new GermplasmSearchAdapter(getContext(), feedGermplasm);
            recyclerView_germplasm.setAdapter(germplasmSearchAdapter);
            germplasmSearchAdapter.setOnItemClickListener(germplasmClickListener);
            ll_germplasm_header.setVisibility(View.GONE);

            locationSearchAdapter = new LocationSearchAdapter(getContext(), feedLocations);
            recyclerView_location.setAdapter(locationSearchAdapter);
            locationSearchAdapter.setOnItemClickListener(locationClickListener);
            ll_location_header.setVisibility(View.GONE);

            sourceSearchAdapter = new SourceSearchAdapter(getContext(), feedSources);
            recyclerView_source.setAdapter(sourceSearchAdapter);
            sourceSearchAdapter.setOnItemClickListener(sourceClickListener);
            ll_source_header.setVisibility(View.GONE);

            crossSearchAdapter = new CrossSearchAdapter(getContext(), feedCrosses);
            recyclerView_cross.setAdapter(crossSearchAdapter);
            crossSearchAdapter.setOnItemClickListener(crossClickListener);
            ll_cross_header.setVisibility(View.GONE);

        } else {
            germplasmSearchTableAdapter = new GermplasmSearchTableAdapter(getContext(), feedGermplasm);
            recyclerView_germplasm.setAdapter(germplasmSearchTableAdapter);
            germplasmSearchTableAdapter.setOnItemClickListener(germplasmClickListener);
            ll_germplasm_header.setVisibility(View.VISIBLE);

            locationSearchTableAdapter = new LocationSearchTableAdapter(getContext(), feedLocations);
            recyclerView_location.setAdapter(locationSearchTableAdapter);
            locationSearchTableAdapter.setOnItemClickListener(locationClickListener);
            ll_location_header.setVisibility(View.VISIBLE);

            sourceSearchTableAdapter = new SourceSearchTableAdapter(getContext(), feedSources);
            recyclerView_source.setAdapter(sourceSearchTableAdapter);
            sourceSearchTableAdapter.setOnItemClickListener(sourceClickListener);
            ll_source_header.setVisibility(View.VISIBLE);

            crossSearchTableAdapter = new CrossSearchTableAdapter(getContext(), feedCrosses);
            recyclerView_cross.setAdapter(crossSearchTableAdapter);
            crossSearchTableAdapter.setOnItemClickListener(crossClickListener);
            ll_cross_header.setVisibility(View.VISIBLE);
        }

        recyclerView_germplasm.setNestedScrollingEnabled(false);
        recyclerView_germplasm.setHasFixedSize(true);
        LinearLayoutManager llm_germplasm = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_germplasm.setAutoMeasureEnabled(false);
        recyclerView_germplasm.setLayoutManager(llm_germplasm);

        recyclerView_location.setNestedScrollingEnabled(false);
        recyclerView_location.setHasFixedSize(true);
        LinearLayoutManager llm_location = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_location.setAutoMeasureEnabled(false);
        recyclerView_location.setLayoutManager(llm_location);

        recyclerView_source.setNestedScrollingEnabled(false);
        recyclerView_source.setHasFixedSize(true);
        LinearLayoutManager llm_source = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_source.setAutoMeasureEnabled(false);
        recyclerView_source.setLayoutManager(llm_source);

        recyclerView_cross.setNestedScrollingEnabled(false);
        recyclerView_cross.setHasFixedSize(true);
        LinearLayoutManager llm_cross = new simms.biosci.simsapplication.Manager.LinearLayoutManager(getActivity(), 1, false);
        llm_cross.setAutoMeasureEnabled(false);
        recyclerView_cross.setLayoutManager(llm_cross);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchingResult(newQuery);
            }
        });

        floating_search_view.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                getActivity().finish();
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

    ChildEventListener germplasmEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                FeedGermplasm model = dataSnapshot.getValue(FeedGermplasm.class);
                feedGermplasm.add(model);
                if (card_view_type) {
                    germplasmSearchAdapter.notifyItemInserted(feedGermplasm.size() - 1);
                } else {
                    germplasmSearchTableAdapter.notifyItemInserted(feedGermplasm.size() - 1);
                }
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
                    if (card_view_type) {
                        germplasmSearchAdapter.notifyDataSetChanged();
                        germplasmSearchAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                    } else {
                        germplasmSearchTableAdapter.notifyDataSetChanged();
                        germplasmSearchTableAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedGermplasm p0 = dataSnapshot.getValue(FeedGermplasm.class);
            for (int i = 0; i < feedGermplasm.size(); i++) {
                if (feedGermplasm.get(i).getG_key().equals(p0.getG_key())) {
                    feedGermplasm.remove(i);
                    if (card_view_type) {
                        germplasmSearchAdapter.notifyItemRemoved(i);
                        germplasmSearchAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                    } else {
                        germplasmSearchTableAdapter.notifyItemRemoved(i);
                        germplasmSearchTableAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
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
                    if (card_view_type) {
                        locationSearchAdapter.notifyItemInserted(feedLocations.size() - 1);
                    } else {
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
                    if (card_view_type) {
                        locationSearchAdapter.notifyDataSetChanged();
                        locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    } else {
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
                    if (card_view_type) {
                        locationSearchAdapter.notifyItemRemoved(i);
                        locationSearchAdapter.notifyItemRangeChanged(i, feedLocations.size());
                    } else {
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

    ChildEventListener sourceEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                try {
                    FeedSource model = dataSnapshot.getValue(FeedSource.class);
                    feedSources.add(model);
                    if (card_view_type) {
                        sourceSearchAdapter.notifyItemInserted(feedSources.size() - 1);
                    } else {
                        sourceSearchTableAdapter.notifyItemInserted(feedSources.size() - 1);
                    }
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
                    if (card_view_type) {
                        sourceSearchAdapter.notifyDataSetChanged();
                        sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
                    } else {
                        sourceSearchTableAdapter.notifyDataSetChanged();
                        sourceSearchTableAdapter.notifyItemRangeChanged(i, feedSources.size());
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedSource p0 = dataSnapshot.getValue(FeedSource.class);
            for (int i = 0; i < feedSources.size(); i++) {
                if (feedSources.get(i).getS_key().equals(p0.getS_key())) {
                    feedSources.remove(i);
                    if (card_view_type) {
                        sourceSearchAdapter.notifyItemRemoved(i);
                        sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
                    } else {
                        sourceSearchTableAdapter.notifyItemRemoved(i);
                        sourceSearchTableAdapter.notifyItemRangeChanged(i, feedSources.size());
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
                    if (card_view_type) {
                        crossSearchAdapter.notifyItemInserted(feedCrosses.size() - 1);
                    } else {
                        crossSearchTableAdapter.notifyItemInserted(feedCrosses.size() - 1);
                    }
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
                    if (card_view_type) {
                        crossSearchAdapter.notifyDataSetChanged();
                        crossSearchAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                    } else {
                        crossSearchTableAdapter.notifyDataSetChanged();
                        crossSearchTableAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedCross p0 = dataSnapshot.getValue(FeedCross.class);
            for (int i = 0; i < feedCrosses.size(); i++) {
                if (feedCrosses.get(i).getC_key().equals(p0.getC_key())) {
                    feedCrosses.remove(i);
                    if (card_view_type) {
                        crossSearchAdapter.notifyItemRemoved(i);
                        crossSearchAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                    } else {
                        crossSearchTableAdapter.notifyItemRemoved(i);
                        crossSearchTableAdapter.notifyItemRangeChanged(i, feedCrosses.size());
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    IntentResult intentResult =
                            IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

                    if (intentResult != null) {

                        String contents = intentResult.getContents();
                        String format = intentResult.getFormatName();

                        floating_search_view.setSearchText(contents);
                        searchingResult(contents);
//                        this.elemQuery.setText(contents);
//                        this.resume = false;
                        Log.d("SEARCH_EAN", "OK, EAN: " + contents + ", FORMAT: " + format);
                    } else {
                        Log.e("SEARCH_EAN", "IntentResult je NULL!");
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e("SEARCH_EAN", "CANCEL");
                }
        }
    }

    private void initScanner() {
        scanner = new ScannerInterface(getContext());
        scanner.open();
        scanner.enablePlayBeep(true);
        scanner.enableFailurePlayBeep(false);
        scanner.enablePlayVibrate(true);
        scanner.setCharSetMode(4);
        scanner.enablShowAPPIcon(false);
        scanner.enablShowNoticeIcon(false);
        scanner.enableAddKeyValue(1);
        scanner.timeOutSet(2);
        scanner.intervalSet(10);
        scanner.lightSet(false);
        scanner.enablePower(true);
        scanner.addPrefix("AAA");
        scanner.addSuffix("BBB");
        scanner.interceptTrimleft(2);
        scanner.interceptTrimright(3);
        scanner.filterCharacter("R");
        scanner.SetErrorBroadCast(true);


        scanner.lockScanKey();
        scanner.setOutputMode(1);


        intentFilter = new IntentFilter(RES_ACTION);
        scanReceiver = new ScannerResultReceiver();
        contextWrapper.registerReceiver(scanReceiver, intentFilter);
        contextWrapper.registerReceiver(scanReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishScanner();
    }

    private void finishScanner() {
        scanner.scan_stop();
        contextWrapper.unregisterReceiver(scanReceiver);
        scanner.continceScan(false);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 && event.getRepeatCount() == 0) {
            scanner.scan_start();
        }
        return onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139) {
            scanner.scan_stop();
        } else if (keyCode == 140) {
            scanner.scan_stop();

            isContinue = !isContinue;
            if (isContinue) {
                scanner.continceScan(true);
            } else {
                scanner.continceScan(false);
            }
        }
        return onKeyUp(keyCode, event);
    }

    public void singleScan(View v) {
        scanner.scan_start();
    }


    private class ScannerResultReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RES_ACTION)) {
                String scanResult = intent.getStringExtra("value");
                floating_search_view.setSearchText(scanResult);
                searchingResult(scanResult);
            }
        }
    }

    private void searchingResult(String search) {
        if (card_view_type) {
            germplasmSearchAdapter.getFilter().filter(search.toLowerCase());
            locationSearchAdapter.getFilter().filter(search.toLowerCase());
            sourceSearchAdapter.getFilter().filter(search.toLowerCase());
            crossSearchAdapter.getFilter().filter(search.toLowerCase());
            if (germplasmSearchAdapter.getItemCount() == 0) {
                ll_germplasm.setVisibility(View.GONE);
            } else {
                ll_germplasm.setVisibility(View.VISIBLE);
            }
            if (locationSearchAdapter.getItemCount() == 0) {
                ll_location.setVisibility(View.GONE);
            } else {
                ll_location.setVisibility(View.VISIBLE);
            }
            if (sourceSearchAdapter.getItemCount() == 0) {
                ll_source.setVisibility(View.GONE);
            } else {
                ll_source.setVisibility(View.VISIBLE);
            }
            if (crossSearchAdapter.getItemCount() == 0) {
                ll_cross.setVisibility(View.GONE);
            } else {
                ll_cross.setVisibility(View.VISIBLE);
            }
            if (germplasmSearchAdapter.getItemCount() == 0 && locationSearchAdapter.getItemCount() == 0 && sourceSearchAdapter.getItemCount() == 0
                    && crossSearchAdapter.getItemCount() == 0) {
                tv_result.setVisibility(View.VISIBLE);
            } else {
                tv_result.setVisibility(View.GONE);
            }

        } else {
            germplasmSearchTableAdapter.getFilter().filter(search.toLowerCase());
            locationSearchTableAdapter.getFilter().filter(search.toLowerCase());
            sourceSearchTableAdapter.getFilter().filter(search.toLowerCase());
            crossSearchTableAdapter.getFilter().filter(search.toLowerCase());
            if (germplasmSearchTableAdapter.getItemCount() == 0) {
                ll_germplasm.setVisibility(View.GONE);
            } else {
                ll_germplasm.setVisibility(View.VISIBLE);
            }
            if (locationSearchTableAdapter.getItemCount() == 0) {
                ll_location.setVisibility(View.GONE);
            } else {
                ll_location.setVisibility(View.VISIBLE);
            }
            if (sourceSearchTableAdapter.getItemCount() == 0) {
                ll_source.setVisibility(View.GONE);
            } else {
                ll_source.setVisibility(View.VISIBLE);
            }
            if (crossSearchTableAdapter.getItemCount() == 0) {
                ll_cross.setVisibility(View.GONE);
            } else {
                ll_cross.setVisibility(View.VISIBLE);
            }
            if (germplasmSearchTableAdapter.getItemCount() == 0 && locationSearchTableAdapter.getItemCount() == 0 && sourceSearchTableAdapter.getItemCount() == 0
                    && crossSearchTableAdapter.getItemCount() == 0) {
                tv_result.setVisibility(View.VISIBLE);
            } else {
                tv_result.setVisibility(View.GONE);
            }
        }
    }
}
