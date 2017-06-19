package simms.biosci.simsapplication.Fragment;

import android.content.Intent;
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

import simms.biosci.simsapplication.Activity.AddGermplasmActivity;
import simms.biosci.simsapplication.Manager.FeedCross;
import simms.biosci.simsapplication.Manager.FeedGermplasm;
import simms.biosci.simsapplication.Manager.FeedLocation;
import simms.biosci.simsapplication.Manager.FeedSource;
import simms.biosci.simsapplication.Manager.GermplasmSearchAdapter;
import simms.biosci.simsapplication.Manager.OnItemClickListener;
import simms.biosci.simsapplication.R;

import static android.content.ContentValues.TAG;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class GermplasmFragment extends Fragment {

    private Typeface montserrat_regular, montserrat_bold;
    private FloatingSearchView floating_search_view;
    private TextView tv_title, tv_result;
    private SheetLayout bottom_sheet;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_SHOW = 4;
    private RecyclerView recyclerView_germplasm;
    private GermplasmSearchAdapter germplasmAdapter;
    private List<FeedGermplasm> feedGermplasm;
    private DatabaseReference mRootRef, mGermplasmRef;

    public GermplasmFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static GermplasmFragment newInstance() {
        GermplasmFragment fragment = new GermplasmFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_germplasm, container, false);
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
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        bottom_sheet = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView_germplasm = (RecyclerView) rootView.findViewById(R.id.recycler_view_germplasm);
        tv_title.setTypeface(montserrat_bold);
        tv_result.setTypeface(montserrat_bold);
        tv_result.setVisibility(View.INVISIBLE);

        bottom_sheet.setFab(fab);
        feedGermplasm = new ArrayList<>();

        germplasmAdapter = new GermplasmSearchAdapter(getContext(), feedGermplasm);
        recyclerView_germplasm.setAdapter(germplasmAdapter);
        recyclerView_germplasm.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setAutoMeasureEnabled(false);
        recyclerView_germplasm.setLayoutManager(llm);

        bottom_sheet.setFabAnimationEndListener(fab_animation_click);
        fab.setOnClickListener(fab_click);
        germplasmAdapter.setOnItemClickListener(onItemClickListener);

        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                germplasmAdapter.getFilter().filter(newQuery.toLowerCase());
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
            Intent intent = new Intent(getContext(), AddGermplasmActivity.class);
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
        } else if (requestCode == REQUEST_CODE_SHOW) {
            try {
                String what2do = data.getStringExtra("what2do");
                if (what2do.toString().equals("update")) {
                    String g_name = data.getStringExtra("g_name");
                    String g_cross = data.getStringExtra("g_cross");
                    String g_source = data.getStringExtra("g_source");
                    int g_lot = data.getIntExtra("g_lot", 0);
                    String g_location = data.getStringExtra("g_location");
                    String g_stock = data.getStringExtra("g_stock");
                    int g_balance = data.getIntExtra("g_balance", 0);
                    int g_room = data.getIntExtra("g_room", 0);
                    int g_shelf = data.getIntExtra("g_shelf", 0);
                    int g_row = data.getIntExtra("g_row", 0);
                    int g_box = data.getIntExtra("g_box", 0);
                    String g_note = data.getStringExtra("g_note");
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedGermplasm.size(); i++) {
                        if (feedGermplasm.get(i).getG_key().equals(key)) {
                            feedGermplasm.get(i).setG_name(g_name + "");
                            feedGermplasm.get(i).setG_cross(g_cross + "");
                            feedGermplasm.get(i).setG_source(g_source + "");
                            feedGermplasm.get(i).setG_lot(g_lot);
                            feedGermplasm.get(i).setG_location(g_location + "");
                            feedGermplasm.get(i).setG_stock(g_stock + "");
                            feedGermplasm.get(i).setG_balance(g_balance);
                            feedGermplasm.get(i).setG_room(g_room);
                            feedGermplasm.get(i).setG_shelf(g_shelf);
                            feedGermplasm.get(i).setG_row(g_row);
                            feedGermplasm.get(i).setG_box(g_box);
                            feedGermplasm.get(i).setG_note(g_note + "");
                            germplasmAdapter.notifyDataSetChanged();
                            germplasmAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                        }
                    }
                } else if (what2do.toString().equals("delete")) {
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedGermplasm.size(); i++) {
                        if (feedGermplasm.get(i).getG_key().equals(key)) {
                            feedGermplasm.remove(i);
                            germplasmAdapter.notifyItemRemoved(i);
                            germplasmAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                        }
                    }
                }
            } catch (Exception e) {

            }

        }
    }

    ChildEventListener germplasmEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                FeedGermplasm model = dataSnapshot.getValue(FeedGermplasm.class);
                feedGermplasm.add(model);
                germplasmAdapter.notifyItemInserted(feedGermplasm.size() - 1);
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
                    germplasmAdapter.notifyDataSetChanged();
                    germplasmAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedGermplasm p0 = dataSnapshot.getValue(FeedGermplasm.class);
            for (int i = 0; i < feedGermplasm.size(); i++) {
                if (feedGermplasm.get(i).getG_key().equals(p0.getG_key())) {
                    feedGermplasm.remove(i);
                    germplasmAdapter.notifyItemRemoved(i);
                    germplasmAdapter.notifyItemRangeChanged(i, feedGermplasm.size());
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
            Intent intent = new Intent(getContext(), AddGermplasmActivity.class);
            intent.putExtra("KEY", item.getG_key());
            Log.i("key", item.getG_key() + "");
            intent.putExtra("REQUEST_CODE", REQUEST_CODE_SHOW);
            startActivityForResult(intent, REQUEST_CODE_SHOW);
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
}
