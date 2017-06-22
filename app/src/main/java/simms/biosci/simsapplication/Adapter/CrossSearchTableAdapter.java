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

import simms.biosci.simsapplication.Manager.OnItemClickListener;
import simms.biosci.simsapplication.Object.FeedCross;
import simms.biosci.simsapplication.R;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class CrossSearchTableAdapter extends RecyclerView.Adapter<CrossSearchTableAdapter.CustomViewHolder> implements Filterable {

    private List<FeedCross> mArrayList;
    private List<FeedCross> mFilteredList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Typeface montserrat_regular, montserrat_bold;
    private String text = "";

    public CrossSearchTableAdapter(Context context, List<FeedCross> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cross_table_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_cross.setText(mFilteredList.get(position).getC_name());
        holder.tv_desc.setText(mFilteredList.get(position).getC_desc());

        FeedCross txt = mFilteredList.get(position);
        String cross = txt.getC_name().toLowerCase(Locale.getDefault());
        String desc = txt.getC_desc().toLowerCase(Locale.getDefault());
        // logic of highlighted text
        if (cross.contains(text)) {

            int startPos = cross.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_cross.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_cross.setText(spanString);
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
                onItemClickListener.onCrossClick(mFilteredList.get(position));
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

                    List<FeedCross> filteredList = new ArrayList<>();

                    for (FeedCross feedCross : mArrayList) {

                        if (feedCross.getC_name().toLowerCase().contains(charString) || feedCross.getC_desc().toLowerCase().contains(charString)) {

                            filteredList.add(feedCross);
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
                mFilteredList = (List<FeedCross>) filterResults.values;
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
        protected TextView tv_cross, tv_desc;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_cross = (TextView) itemView.findViewById(R.id.tv_cross);
            this.tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            this.tv_cross.setTypeface(montserrat_bold);
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
