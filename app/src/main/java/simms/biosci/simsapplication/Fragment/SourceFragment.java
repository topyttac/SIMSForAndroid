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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.Activity.AddSourceActivity;
import simms.biosci.simsapplication.Adapter.SourceSearchAdapter;
import simms.biosci.simsapplication.Adapter.SourceSearchTableAdapter;
import simms.biosci.simsapplication.Manager.IntentIntegrator;
import simms.biosci.simsapplication.Manager.IntentResult;
import simms.biosci.simsapplication.Manager.OnItemClickListener;
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
public class SourceFragment extends Fragment {

    private SharedPreferences display_read;
    private Boolean card_view_type;
    private Typeface montserrat_regular, montserrat_bold;
    private FloatingSearchView floating_search_view;
    private TextView tv_title, tv_source, tv_desc;
    private SheetLayout bottom_sheet;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_ADD = 3;
    private static final int REQUEST_CODE_SHOW = 6;
    private RecyclerView recyclerView_source;
    private SourceSearchAdapter sourceSearchAdapter;
    private SourceSearchTableAdapter sourceSearchTableAdapter;
    private List<FeedSource> feedSources;
    private DatabaseReference mRootRef, mSourceRef;
    private IntentIntegrator scanIntegrator;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager llm;
    private LinearLayout ll_table_header;
    private ScannerInterface scanner;
    private IntentFilter intentFilter;
    private BroadcastReceiver scanReceiver;
    private boolean isContinue = false;
    private static final String RES_ACTION = "android.intent.action.SCANRESULT";
    private ContextWrapper contextWrapper;

    public SourceFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SourceFragment newInstance() {
        SourceFragment fragment = new SourceFragment();
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
        mRootRef.child("source").orderByChild("s_name").addChildEventListener(sourceEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_source, container, false);
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
        recyclerView_source = (RecyclerView) rootView.findViewById(R.id.recycler_view_source);
        bottom_sheet = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tv_source = (TextView) rootView.findViewById(R.id.tv_source);
        tv_desc = (TextView) rootView.findViewById(R.id.tv_desc);
        ll_table_header = (LinearLayout) rootView.findViewById(R.id.ll_table_header);

        tv_title.setTypeface(montserrat_bold);
        tv_source.setTypeface(montserrat_bold);
        tv_desc.setTypeface(montserrat_bold);

        feedSources = new ArrayList<>();
        bottom_sheet.setFab(fab);
        initScanner();

        display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
        card_view_type = display_read.getBoolean("card_view_type", true);
        if (card_view_type) {
            sourceSearchAdapter = new SourceSearchAdapter(getContext(), feedSources);
            recyclerView_source.setAdapter(sourceSearchAdapter);
            sourceSearchAdapter.setOnItemClickListener(onItemClickListener);
            ll_table_header.setVisibility(View.GONE);
        } else {
            sourceSearchTableAdapter = new SourceSearchTableAdapter(getContext(), feedSources);
            recyclerView_source.setAdapter(sourceSearchTableAdapter);
            sourceSearchTableAdapter.setOnItemClickListener(onItemClickListener);
            ll_table_header.setVisibility(View.VISIBLE);
        }

        recyclerView_source.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setAutoMeasureEnabled(false);
        recyclerView_source.setLayoutManager(llm);

        bottom_sheet.setFabAnimationEndListener(fab_animation_click);
        fab.setOnClickListener(fab_click);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (card_view_type) {
                    sourceSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                } else {
                    sourceSearchTableAdapter.getFilter().filter(newQuery.toLowerCase());
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

        recyclerView_source.addOnScrollListener(recyclerViewScrollListener);
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
            Intent intent = new Intent(getContext(), AddSourceActivity.class);
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
                Log.i("hello", feedSources.get(i).getS_key() + "");
                Log.i("hello", p0.getS_key() + "");
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

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
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
            intent.putExtra("REQUEST_CODE", REQUEST_CODE_SHOW);
            startActivityForResult(intent, REQUEST_CODE_SHOW);
        }

        @Override
        public void onCrossClick(FeedCross item) {

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
                        sourceSearchAdapter.getFilter().filter(contents.toLowerCase());
                    } else {
                        sourceSearchTableAdapter.getFilter().filter(contents.toLowerCase());
                    }
                    Log.d("SEARCH_EAN", "OK, EAN: " + contents + ", FORMAT: " + format);
                } else {
                    Log.e("SEARCH_EAN", "IntentResult je NULL!");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("SEARCH_EAN", "CANCEL");
            }
        }else if (requestCode == REQUEST_CODE_SHOW) {
            try {
                String what2do = data.getStringExtra("what2do");
                if (what2do.toString().equals("update")) {
                    String s_name = data.getStringExtra("s_name");
                    String s_desc = data.getStringExtra("s_desc");
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedSources.size(); i++) {
                        if (feedSources.get(i).getS_key().equals(key)) {
                            feedSources.get(i).setS_name(s_name + "");
                            feedSources.get(i).setS_desc(s_desc + "");
                            if(card_view_type){
                                sourceSearchAdapter.notifyDataSetChanged();
                                sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
                            } else{
                                sourceSearchTableAdapter.notifyDataSetChanged();
                                sourceSearchTableAdapter.notifyItemRangeChanged(i, feedSources.size());
                            }
                        }
                    }
                } else if (what2do.toString().equals("delete")) {
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedSources.size(); i++) {
                        if (feedSources.get(i).getS_key().equals(key)) {
                            feedSources.remove(i);
                            if(card_view_type){
                                sourceSearchAdapter.notifyItemRemoved(i);
                                sourceSearchAdapter.notifyItemRangeChanged(i, feedSources.size());
                            } else{
                                sourceSearchTableAdapter.notifyItemRemoved(i);
                                sourceSearchTableAdapter.notifyItemRangeChanged(i, feedSources.size());
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }

        }
    }

    RecyclerView.OnScrollListener recyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    fab.animate().translationY(200);
                }
            } else if (dy < 0) {
                fab.animate().translationY(0);
            }
        }
    };

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
                if (card_view_type) {
                    sourceSearchAdapter.getFilter().filter(scanResult.toLowerCase());
                } else {
                    sourceSearchTableAdapter.getFilter().filter(scanResult.toLowerCase());
                }
            }
        }
    }
}
