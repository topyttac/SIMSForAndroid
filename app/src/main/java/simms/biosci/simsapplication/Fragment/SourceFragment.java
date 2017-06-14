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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.Activity.AddSourceActivity;
import simms.biosci.simsapplication.Manager.FeedGermplasm;
import simms.biosci.simsapplication.Manager.FeedLocation;
import simms.biosci.simsapplication.Manager.FeedSource;
import simms.biosci.simsapplication.Manager.OnItemClickListener;
import simms.biosci.simsapplication.Manager.SourceAdapter;
import simms.biosci.simsapplication.R;

import static android.content.ContentValues.TAG;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class SourceFragment extends Fragment {

    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_title;
    private SheetLayout bottom_sheet;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_ADD = 3;
    private static final int REQUEST_CODE_SHOW = 6;
    private RecyclerView recyclerView_source;
    private SourceAdapter sourceAdapter;
    private List<FeedSource> feedSources;
    private DatabaseReference mRootRef, mSourceRef;

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
        mRootRef = FirebaseDatabase.getInstance().getReference();

        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        recyclerView_source = (RecyclerView) rootView.findViewById(R.id.recycler_view_source);
        bottom_sheet = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tv_title.setTypeface(montserrat_bold);

        feedSources = new ArrayList<>();
        bottom_sheet.setFab(fab);

        sourceAdapter = new SourceAdapter(getContext(), feedSources);
        recyclerView_source.setAdapter(sourceAdapter);
        recyclerView_source.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setAutoMeasureEnabled(false);
        recyclerView_source.setLayoutManager(llm);

        mRootRef.child("source").orderByChild("s_name").addChildEventListener(sourceEventListener);
        bottom_sheet.setFabAnimationEndListener(fab_animation_click);
        fab.setOnClickListener(fab_click);
        sourceAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
//        if (sourceEventListener != null) {
//            mSourceRef.removeEventListener(sourceEventListener);
//        }
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
                    sourceAdapter.notifyItemInserted(feedSources.size() - 1);
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
                    sourceAdapter.notifyDataSetChanged();
                    sourceAdapter.notifyItemRangeChanged(i, feedSources.size());
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedSource p0 = dataSnapshot.getValue(FeedSource.class);
            for (int i = 0; i < feedSources.size(); i++) {
                if (feedSources.get(i).getS_key().equals(p0.getS_key())) {
                    feedSources.remove(i);
                    sourceAdapter.notifyItemRemoved(i);
                    sourceAdapter.notifyItemRangeChanged(i, feedSources.size());
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
                    String s_name = data.getStringExtra("s_name");
                    String s_desc = data.getStringExtra("s_desc");
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedSources.size(); i++) {
                        if (feedSources.get(i).getS_key().equals(key)) {
                            feedSources.get(i).setS_name(s_name + "");
                            feedSources.get(i).setS_desc(s_desc + "");
                            sourceAdapter.notifyDataSetChanged();
                            sourceAdapter.notifyItemRangeChanged(i, feedSources.size());
                        }
                    }
                } else if (what2do.toString().equals("delete")) {
                    String key = data.getStringExtra("KEY");
                    for (int i = 0; i < feedSources.size(); i++) {
                        if (feedSources.get(i).getS_key().equals(key)) {
                            feedSources.remove(i);
                            sourceAdapter.notifyItemRemoved(i);
                            sourceAdapter.notifyItemRangeChanged(i, feedSources.size());
                        }
                    }
                }
            } catch (Exception e) {

            }

        }
    }
}
