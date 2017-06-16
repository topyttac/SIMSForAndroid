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

public class LocationSearchAdapter extends RecyclerView.Adapter<LocationSearchAdapter.CustomViewHolder> implements Filterable {

    private List<FeedLocation> mArrayList;
    private List<FeedLocation> mFilteredList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private Typeface montserrat_regular, montserrat_bold, prompt_regular;

    public LocationSearchAdapter(Context context, List<FeedLocation> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_location.setText(mFilteredList.get(position).getL_name());
        holder.tv_address.setText(mFilteredList.get(position).getL_sub_district() + ", " + mFilteredList.get(position).getL_district() + ", " + mFilteredList.get(position).getL_province());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onLocationClick(mFilteredList.get(position));
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

                    List<FeedLocation> filteredList = new ArrayList<>();

                    for (FeedLocation feedLocation : mArrayList) {

                        if (feedLocation.getL_name().toLowerCase().contains(charString) || feedLocation.getL_province().toLowerCase().contains(charString) || feedLocation.getL_district().toLowerCase().contains(charString) || feedLocation.getL_sub_district().toLowerCase().contains(charString)) {

                            filteredList.add(feedLocation);
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
                mFilteredList = (List<FeedLocation>) filterResults.values;
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
        protected TextView tv_location, tv_address, tv_title_address;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            prompt_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Prompt-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            this.tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            this.tv_title_address = (TextView) itemView.findViewById(R.id.tv_title_address);
            this.tv_location.setTypeface(montserrat_bold);
            this.tv_address.setTypeface(prompt_regular);
            this.tv_title_address.setTypeface(montserrat_regular);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
