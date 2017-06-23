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

import simms.biosci.simsapplication.Activity.AddCrossActivity;
import simms.biosci.simsapplication.Adapter.CrossSearchAdapter;
import simms.biosci.simsapplication.Adapter.CrossSearchTableAdapter;
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
public class CrossFragment extends Fragment {

    private SharedPreferences display_read;
    private Boolean card_view_type;
    private Typeface montserrat_regular, montserrat_bold;
    private FloatingSearchView floating_search_view;
    private TextView tv_title, tv_cross, tv_desc;
    private SheetLayout bottom_sheet;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_ADD = 7;
    private static final int REQUEST_CODE_SHOW = 8;
    private RecyclerView recyclerView_cross;
    private CrossSearchAdapter crossSearchAdapter;
    private CrossSearchTableAdapter crossSearchTableAdapter;
    private List<FeedCross> feedCrosses;
    private DatabaseReference mRootRef, mCrossRef;
    private IntentIntegrator scanIntegrator;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager llm;
    private LinearLayout ll_table_header;

    public CrossFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CrossFragment newInstance() {
        CrossFragment fragment = new CrossFragment();
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
        mRootRef.child("cross").orderByChild("c_name").addChildEventListener(crossEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cross, container, false);
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
        recyclerView_cross = (RecyclerView) rootView.findViewById(R.id.recycler_view_cross);
        bottom_sheet = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tv_cross = (TextView) rootView.findViewById(R.id.tv_cross);
        tv_desc = (TextView) rootView.findViewById(R.id.tv_desc);
        ll_table_header = (LinearLayout) rootView.findViewById(R.id.ll_table_header);

        tv_title.setTypeface(montserrat_bold);
        tv_cross.setTypeface(montserrat_bold);
        tv_desc.setTypeface(montserrat_bold);

        feedCrosses = new ArrayList<>();
        bottom_sheet.setFab(fab);
        display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
        card_view_type = display_read.getBoolean("card_view_type", true);
        if (card_view_type) {
            crossSearchAdapter = new CrossSearchAdapter(getContext(), feedCrosses);
            recyclerView_cross.setAdapter(crossSearchAdapter);
            crossSearchAdapter.setOnItemClickListener(onItemClickListener);
            ll_table_header.setVisibility(View.GONE);
        } else {
            crossSearchTableAdapter = new CrossSearchTableAdapter(getContext(), feedCrosses);
            recyclerView_cross.setAdapter(crossSearchTableAdapter);
            crossSearchTableAdapter.setOnItemClickListener(onItemClickListener);
            ll_table_header.setVisibility(View.VISIBLE);
        }


        recyclerView_cross.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setAutoMeasureEnabled(false);
        recyclerView_cross.setLayoutManager(llm);

        bottom_sheet.setFabAnimationEndListener(fab_animation_click);
        fab.setOnClickListener(fab_click);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (card_view_type) {
                    crossSearchAdapter.getFilter().filter(newQuery.toLowerCase());
                } else {
                    crossSearchTableAdapter.getFilter().filter(newQuery.toLowerCase());
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

        recyclerView_cross.addOnScrollListener(recyclerViewScrollListener);
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
            Intent intent = new Intent(getContext(), AddCrossActivity.class);
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
                Log.i("hello", feedCrosses.get(i).getC_key() + "");
                Log.i("hello", p0.getC_key() + "");
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

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
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
            intent.putExtra("REQUEST_CODE", REQUEST_CODE_SHOW);
            startActivityForResult(intent, REQUEST_CODE_SHOW);
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
                        crossSearchAdapter.getFilter().filter(contents.toLowerCase());
                    } else {
                        crossSearchTableAdapter.getFilter().filter(contents.toLowerCase());
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
                    String c_name = data.getStringExtra("c_name");
                    String c_desc = data.getStringExtra("c_desc");
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedCrosses.size(); i++) {
                        if (feedCrosses.get(i).getC_key().equals(key)) {
                            feedCrosses.get(i).setC_name(c_name + "");
                            feedCrosses.get(i).setC_desc(c_desc + "");
                            if (card_view_type) {
                                crossSearchAdapter.notifyDataSetChanged();
                                crossSearchAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                            } else {
                                crossSearchTableAdapter.notifyDataSetChanged();
                                crossSearchTableAdapter.notifyItemRangeChanged(i, feedCrosses.size());
                            }
                        }
                    }
                } else if (what2do.toString().equals("delete")) {
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedCrosses.size(); i++) {
                        if (feedCrosses.get(i).getC_key().equals(key)) {
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
}
