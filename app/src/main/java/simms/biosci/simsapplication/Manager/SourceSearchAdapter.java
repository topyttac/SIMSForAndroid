package simms.biosci.simsapplication.Manager;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.R;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class SourceSearchAdapter extends RecyclerView.Adapter<SourceSearchAdapter.CustomViewHolder> implements Filterable{

    private List<FeedSource> mArrayList;
    private List<FeedSource> mFilteredList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold;

    public SourceSearchAdapter(Context context, List<FeedSource> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.source_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_source.setText(mFilteredList.get(position).getS_name());
        holder.tv_desc.setText(mFilteredList.get(position).getS_desc());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSourceClick(mFilteredList.get(position));
            }
        };
        holder.ll_card.setOnClickListener(listener);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    List<FeedSource> filteredList = new ArrayList<>();

                    for (FeedSource feedSource : mArrayList) {

                        if (feedSource.getS_name().toLowerCase().contains(charString) || feedSource.getS_desc().toLowerCase().contains(charString)) {

                            filteredList.add(feedSource);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<FeedSource>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return null != mFilteredList ? mFilteredList.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout ll_card;
        protected TextView tv_source, tv_desc, tv_title_desc;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            this.tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            this.tv_title_desc = (TextView) itemView.findViewById(R.id.tv_title_desc);
            this.tv_source.setTypeface(montserrat_bold);
            this.tv_title_desc.setTypeface(montserrat_regular);
            this.tv_desc.setTypeface(montserrat_regular);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
