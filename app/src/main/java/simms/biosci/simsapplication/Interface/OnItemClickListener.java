package simms.biosci.simsapplication.Interface;

import simms.biosci.simsapplication.Object.FeedCross;
import simms.biosci.simsapplication.Object.FeedGermplasm;
import simms.biosci.simsapplication.Object.FeedLocation;
import simms.biosci.simsapplication.Object.FeedSource;

/**
 * Created by topyttac on 4/23/2017 AD.
 */

public interface OnItemClickListener {
    void onGermplasmClick(FeedGermplasm item);

    void onLocationClick(FeedLocation item);

    void onSourceClick(FeedSource item);

    void onCrossClick(FeedCross item);
}
