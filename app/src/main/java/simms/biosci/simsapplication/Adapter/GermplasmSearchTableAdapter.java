package simms.biosci.simsapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import simms.biosci.simsapplication.Object.FeedGermplasm;
import simms.biosci.simsapplication.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public class GermplasmSearchTableAdapter extends RecyclerView.Adapter<GermplasmSearchTableAdapter.CustomViewHolder> implements Filterable {

    private SharedPreferences germplasm_one_read, germplasm_two_read;
    private int germplasm_which_one, germplasm_which_two;
    private List<FeedGermplasm> mArrayList;
    private List<FeedGermplasm> mFilteredList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private Typeface montserrat_regular, montserrat_bold;
    private String text = "", one = "", two = "", germplasm = "";

    public GermplasmSearchTableAdapter(Context context, List<FeedGermplasm> feedItems) {
        this.context = context;
        this.mArrayList = feedItems;
        this.mFilteredList = feedItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.germplasm_table_list, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        FeedGermplasm txt = mFilteredList.get(position);
        germplasm = txt.getG_name().toLowerCase(Locale.getDefault());

        holder.tv_germplasm.setText(mFilteredList.get(position).getG_name());

        if (germplasm_which_one == 1) {
            holder.tv_one.setText(mFilteredList.get(position).getG_cross());
            one = txt.getG_cross().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_one == 2) {
            holder.tv_one.setText(mFilteredList.get(position).getG_source());
            one = txt.getG_source().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_one == 3) {
            holder.tv_one.setText(mFilteredList.get(position).getG_lot() + "");
            one = String.valueOf(txt.getG_lot());
        } else if (germplasm_which_one == 4) {
            holder.tv_one.setText(mFilteredList.get(position).getG_location());
            one = txt.getG_location().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_one == 5) {
            holder.tv_one.setText(mFilteredList.get(position).getG_stock());
            one = txt.getG_stock().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_one == 6) {
            holder.tv_one.setText(mFilteredList.get(position).getG_balance() + "");
            one = String.valueOf(txt.getG_balance());
        } else if (germplasm_which_one == 7) {
            holder.tv_one.setText(mFilteredList.get(position).getG_room() + "");
            one = String.valueOf(txt.getG_room());
        } else if (germplasm_which_one == 8) {
            holder.tv_one.setText(mFilteredList.get(position).getG_shelf() + "");
            one = String.valueOf(txt.getG_shelf());
        } else if (germplasm_which_one == 9) {
            holder.tv_one.setText(mFilteredList.get(position).getG_row() + "");
            one = String.valueOf(txt.getG_row());
        } else if (germplasm_which_one == 10) {
            holder.tv_one.setText(mFilteredList.get(position).getG_box() + "");
            one = String.valueOf(txt.getG_box());
        } else if (germplasm_which_one == 11) {
            holder.tv_one.setText(mFilteredList.get(position).getG_note());
            one = txt.getG_note().toLowerCase(Locale.getDefault());
        }

        if (germplasm_which_two == 1) {
            holder.tv_two.setText(mFilteredList.get(position).getG_cross());
            two = txt.getG_cross().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_two == 2) {
            holder.tv_two.setText(mFilteredList.get(position).getG_source());
            two = txt.getG_source().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_two == 3) {
            holder.tv_two.setText(mFilteredList.get(position).getG_lot()  + "");
            two = String.valueOf(txt.getG_lot());
        } else if (germplasm_which_two == 4) {
            holder.tv_two.setText(mFilteredList.get(position).getG_location());
            two = txt.getG_location().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_two == 5) {
            holder.tv_two.setText(mFilteredList.get(position).getG_stock());
            two = txt.getG_stock().toLowerCase(Locale.getDefault());
        } else if (germplasm_which_two == 6) {
            holder.tv_two.setText(mFilteredList.get(position).getG_balance() + "");
            two = String.valueOf(txt.getG_balance());
        } else if (germplasm_which_two == 7) {
            holder.tv_two.setText(mFilteredList.get(position).getG_room() + "");
            two = String.valueOf(txt.getG_room());
        } else if (germplasm_which_two == 8) {
            holder.tv_two.setText(mFilteredList.get(position).getG_shelf() + "");
            two = String.valueOf(txt.getG_shelf());
        } else if (germplasm_which_two == 9) {
            holder.tv_two.setText(mFilteredList.get(position).getG_row() + "");
            two = String.valueOf(txt.getG_row());
        } else if (germplasm_which_two == 10) {
            holder.tv_two.setText(mFilteredList.get(position).getG_box() + "");
            two = String.valueOf(txt.getG_box());
        } else if (germplasm_which_two == 11) {
            holder.tv_two.setText(mFilteredList.get(position).getG_note());
            two = txt.getG_note().toLowerCase(Locale.getDefault());
        }

        // logic of highlighted text
        if (germplasm.contains(text)) {

            int startPos = germplasm.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_germplasm.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_germplasm.setText(spanString);
        }
        if (one.contains(text)) {

            int startPos = one.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_one.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text
            holder.tv_one.setText(spanString);
        }
        if (two.contains(text)) {

            int startPos = two.indexOf(text);
            int endPos = startPos + text.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_two.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // red color of matching text

            holder.tv_two.setText(spanString);
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

                        if (feedGermplasm.getG_name().toLowerCase().contains(charString) || one.toLowerCase().contains(charString) || feedGermplasm.getG_location().toLowerCase().contains(charString)) {

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
        protected TextView tv_germplasm, tv_one, tv_two;

        public CustomViewHolder(View itemView) {
            super(itemView);
            montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            montserrat_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");

            germplasm_one_read = context.getSharedPreferences("germplasm_which_one", MODE_PRIVATE);
            germplasm_which_one = germplasm_one_read.getInt("germplasm_which_one", 2);

            germplasm_two_read = context.getSharedPreferences("germplasm_which_two", MODE_PRIVATE);
            germplasm_which_two = germplasm_two_read.getInt("germplasm_which_two", 4);

            this.ll_card = (LinearLayout) itemView.findViewById(R.id.ll_card);
            this.tv_germplasm = (TextView) itemView.findViewById(R.id.tv_germplasm);
            this.tv_one = (TextView) itemView.findViewById(R.id.tv_one);
            this.tv_two = (TextView) itemView.findViewById(R.id.tv_two);
            this.tv_germplasm.setTypeface(montserrat_bold);
            this.tv_one.setTypeface(montserrat_regular);
            this.tv_two.setTypeface(montserrat_regular);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
