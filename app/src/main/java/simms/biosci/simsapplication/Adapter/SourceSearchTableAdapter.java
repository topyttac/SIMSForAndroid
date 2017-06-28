package simms.biosci.simsapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import simms.biosci.simsapplication.Interface.OnItemClickListener;
import simms.biosci.simsapplication.Object.FeedSource;
import simms.biosci.simsapplication.R;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class SourceSearchTableAdapter extends RecyclerView.Adapter<SourceSearchTableAdapter.CustomViewHolder> implements Filterable {

    private List<FeedSource> mArrayList;
    private List<FeedSource> mFilteredList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold;
    private String text = "";

    public SourceSearchTableAdapter(Context context, List<FeedSource> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.source_table_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_source.setText(mFilteredList.get(position).getS_name());
        holder.tv_desc.setText(mFilteredList.get(position).getS_desc());

        FeedSource txt = mFilteredList.get(position);
        String source = txt.getS_name().toLowerCase(Locale.getDefault());
        String desc = txt.getS_desc().toLowerCase(Locale.getDefault());
        // logic of highlighted text
        if (source.contains(text)) {

            int startPos = source.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_source.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_source.setText(spanString);
        }
        if (desc.contains(text)) {

            int startPos = desc.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_desc.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_desc.setText(spanString);
        }

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
                text = charString;

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
        protected TextView tv_source, tv_desc;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            this.tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            this.tv_source.setTypeface(montserrat_bold);
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
