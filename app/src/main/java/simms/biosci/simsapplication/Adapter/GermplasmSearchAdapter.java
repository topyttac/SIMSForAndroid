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

import simms.biosci.simsapplication.Object.FeedGermplasm;
import simms.biosci.simsapplication.Interface.OnItemClickListener;
import simms.biosci.simsapplication.R;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class GermplasmSearchAdapter extends RecyclerView.Adapter<GermplasmSearchAdapter.CustomViewHolder> implements Filterable {

    private List<FeedGermplasm> mArrayList;
    private List<FeedGermplasm> mFilteredList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private Typeface montserrat_regular, montserrat_bold;
    private String text = "";

    public GermplasmSearchAdapter(Context context, List<FeedGermplasm> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.germplasm_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_germplasm.setText(mFilteredList.get(position).getG_name());
        holder.tv_location.setText(mFilteredList.get(position).getG_location());
        holder.tv_source.setText(mFilteredList.get(position).getG_source());

        FeedGermplasm txt = mFilteredList.get(position);
        String germplasm = txt.getG_name().toLowerCase(Locale.getDefault());
        String location = txt.getG_location().toLowerCase(Locale.getDefault());
        String source = txt.getG_source().toLowerCase(Locale.getDefault());
        // logic of highlighted text
        if (germplasm.contains(text)) {

            int startPos = germplasm.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_germplasm.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_germplasm.setText(spanString);
        }
        if (location.contains(text)) {

            int startPos = location.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_location.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_location.setText(spanString);
        }
        if (source.contains(text)) {

            int startPos = source.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_source.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_source.setText(spanString);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onGermplasmClick(mFilteredList.get(position));
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

                    List<FeedGermplasm> filteredList = new ArrayList<>();

                    for (FeedGermplasm feedGermplasm : mArrayList) {

                        if (feedGermplasm.getG_name().toLowerCase().contains(charString) || feedGermplasm.getG_source().toLowerCase().contains(charString) || feedGermplasm.getG_location().toLowerCase().contains(charString)) {

                            filteredList.add(feedGermplasm);
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
                mFilteredList = (List<FeedGermplasm>) filterResults.values;
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
