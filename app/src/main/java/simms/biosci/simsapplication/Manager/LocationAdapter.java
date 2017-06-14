package simms.biosci.simsapplication.Manager;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import simms.biosci.simsapplication.R;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.CustomViewHolder>{

    private List<FeedLocation> feedItems;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold, prompt_regular;

    public LocationAdapter(Context context, List<FeedLocation> feedItems){
        this.context = context;
        this.feedItems = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final FeedLocation feedItemlist = feedItems.get(position);
        holder.tv_location.setText(feedItemlist.getL_name());
        holder.tv_address.setText(feedItemlist.getL_sub_district() + ", " + feedItemlist.getL_district() + ", " + feedItemlist.getL_province());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onLocationClick(feedItemlist);
            }
        };
        holder.ll_card.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return null != feedItems ? feedItems.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

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
