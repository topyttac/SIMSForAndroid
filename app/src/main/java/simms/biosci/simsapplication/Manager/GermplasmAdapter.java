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

public class GermplasmAdapter extends RecyclerView.Adapter<GermplasmAdapter.CustomViewHolder>{

    private List<FeedGermplasm> feedItems;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold;

    public GermplasmAdapter(Context context, List<FeedGermplasm> feedItems){
        this.context = context;
        this.feedItems = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.germplasm_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final FeedGermplasm feedItemlist = feedItems.get(position);
        holder.tv_germplasm.setText(feedItemlist.getG_name());
        holder.tv_location.setText(feedItemlist.getG_location());
        holder.tv_source.setText(feedItemlist.getG_source());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onGermplasmClick(feedItemlist);
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
        protected TextView tv_germplasm, tv_location, tv_source, tv_title_location, tv_title_source;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_germplasm = (TextView) itemView.findViewById(R.id.tv_germplasm);
            this.tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            this.tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            this.tv_title_location = (TextView) itemView.findViewById(R.id.tv_title_location);
            this.tv_title_source = (TextView) itemView.findViewById(R.id.tv_title_source);
            this.tv_germplasm.setTypeface(montserrat_bold);
            this.tv_location.setTypeface(montserrat_regular);
            this.tv_source.setTypeface(montserrat_regular);
            this.tv_title_location.setTypeface(montserrat_regular);
            this.tv_title_source.setTypeface(montserrat_regular);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
