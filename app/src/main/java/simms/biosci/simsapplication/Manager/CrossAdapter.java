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

public class CrossAdapter extends RecyclerView.Adapter<CrossAdapter.CustomViewHolder>{

    private List<FeedCross> feedItems;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold;

    public CrossAdapter(Context context, List<FeedCross> feedItems){
        this.context = context;
        this.feedItems = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cross_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final FeedCross feedItemlist = feedItems.get(position);
        holder.tv_cross.setText(feedItemlist.getC_name());
        holder.tv_desc.setText(feedItemlist.getC_desc());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onCrossClick(feedItemlist);
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
        protected TextView tv_cross, tv_desc, tv_title_desc;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_cross = (TextView) itemView.findViewById(R.id.tv_cross);
            this.tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            this.tv_title_desc = (TextView) itemView.findViewById(R.id.tv_title_desc);
            this.tv_cross.setTypeface(montserrat_bold);
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
