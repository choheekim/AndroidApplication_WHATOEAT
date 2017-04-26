package company.chohee.csc780.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collection;

import company.chohee.csc780.R;
import company.chohee.csc780.model.Restaurant;

/**
 * Created by Chohee on 4/17/17.
 */

public abstract class CardStackAdapter extends BaseCardStackAdapter {
    private final Context mContext;

    /**
     * Lock used to modify the content of {@link #mData}. Any write operation
     * performed on the deque should be synchronized on this lock.
     */
    private final Object mLock = new Object();
    private ArrayList<Restaurant> mData;

    private boolean mShouldFillCardBackground = false;

    public CardStackAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<Restaurant>();
    }

    public CardStackAdapter(Context context, Collection<? extends Restaurant> items) {
        mContext = context;
        mData = new ArrayList<Restaurant>(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout wrapper = (FrameLayout) convertView;
        FrameLayout innerWrapper;
        View cardView;
        View convertedCardView;
        if (wrapper == null) {
            wrapper = new FrameLayout(mContext);
            wrapper.setBackgroundResource(R.drawable.card_bg);
            if (shouldFillCardBackground()) {
                innerWrapper = new FrameLayout(mContext);
                innerWrapper.setBackgroundColor(mContext.getResources().getColor(R.color.Gray));
                wrapper.addView(innerWrapper);
            } else {
                innerWrapper = wrapper;
            }
            cardView = getCardView(position, getRestaurants(position), null, parent);
            innerWrapper.addView(cardView);
        } else {
            if (shouldFillCardBackground()) {
                innerWrapper = (FrameLayout) wrapper.getChildAt(0);
            } else {
                innerWrapper = wrapper;
            }
            cardView = innerWrapper.getChildAt(0);
            convertedCardView = getCardView(position, getRestaurants(position), cardView, parent);
            if (convertedCardView != cardView) {
                wrapper.removeView(cardView);
                wrapper.addView(convertedCardView);
            }
        }

        return wrapper;
    }

    protected abstract View getCardView(int position, Restaurant model, View convertView, ViewGroup parent);

    public void setShouldFillCardBackground(boolean isShouldFillCardBackground) {
        this.mShouldFillCardBackground = isShouldFillCardBackground;
    }

    public boolean shouldFillCardBackground() {
        return mShouldFillCardBackground;
    }

    public void add(Restaurant item) {
        synchronized (mLock) {
            mData.add(item);
        }
        notifyDataSetChanged();
    }

    public Restaurant pop() {
        Restaurant model;
        synchronized (mLock) {
            model = mData.remove(mData.size() - 1);
        }
        notifyDataSetChanged();
        return model;
    }

    @Override
    public Object getItem(int position) {
        return getRestaurants(position);
    }

    public Restaurant getRestaurants(int position) {
        synchronized (mLock) {
            return mData.get(mData.size() - 1 - position);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public Context getContext() {
        return mContext;
    }
}
