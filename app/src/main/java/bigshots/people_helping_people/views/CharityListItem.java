package bigshots.people_helping_people.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.io.VoteManager;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class CharityListItem extends ViewGroup {
    public static final VoteManager voteManager = new VoteManager();
    public static VoteCharityAdapter voteCharityAdapter;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public String link;
    private MaterialTwoLineText textView;
    private LikeButton likeButton;
    private int width;
    private int height;
    private int votes;
    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof LikeButton) {
                ((LikeButton) v).click();
            } else {
                openLink();
            }
        }
    };
    private int clickedX, clickedY;
    private boolean touchDown = false, votedFor, trusted;
    private int pos;
    private String name;

    public CharityListItem(Context context, VoteCharityAdapter voteCharityAdapter) {
        super(context);
        this.voteCharityAdapter = voteCharityAdapter;
        init();
    }

    public CharityListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static String getCurrentVote() {
        return voteCharityAdapter.getVotedFor();
    }

    public static void setCurrentVote(String currentVote) {
        voteCharityAdapter.setVotedFor(currentVote);
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {
        likeButton.setIsVotedFor(votedFor);
        if (!votedFor) {
            try {
                if (VoteFragment.pedestal != null)
                    VoteFragment.pedestal.setVotedFor(voteCharityAdapter.getVotedFor().equals(MainMenu.pedestal.getUrl()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        invalidatePoster();
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setPrimaryText(String text) {
        name = text;
        textView.setPrimaryText(text);
        invalidatePoster();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        final int padding = dpToPixels(12);
        final int buttonWidth = dpToPixels(52);

        textView = new MaterialTwoLineText(getContext());
        textView.setPrimaryTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.getView().setOnClickListener(onClickListener);

        likeButton = new LikeButton(getContext());
        likeButton.setLayoutParams(new LayoutParams(buttonWidth, buttonWidth));

        setWillNotDraw(false);
        paint.setColor(0x25000000);
        addView(textView.getView());
        addView(likeButton);
    }

    public void setPrimaryTextSize(int sp) {
        textView.setPrimaryTextSize(sp);
    }

    public void setPrimaryTextColor(int color) {
        textView.setPrimaryTextColor(color);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
        textView.setTrustedViewText(trusted ? "Verified" : "");
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                likeButton.invalidatePoster();
                invalidate();
            }
        });
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 3)
            return;
        super.addView(child, index, params);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredHeight = Math.max(child.getMeasuredHeight(), measuredHeight);
            measuredWidth += child.getMeasuredWidth();
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0), resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        //progressBar.layout(0, getHeight() - progressBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        final int imageViewPadding = (getMeasuredHeight() - likeButton.getMeasuredHeight()) / 2;
        likeButton.layout(getMeasuredWidth() - getPaddingLeft() - likeButton.getMeasuredWidth(),
                imageViewPadding,
                getMeasuredWidth() - getPaddingLeft(),
                getMeasuredHeight() - imageViewPadding
        );

        final int textViewPadding = (getMeasuredHeight() - textView.getView().getMeasuredHeight()) / 2;
        textView.getView().layout(getPaddingLeft(), 0,
                getMeasuredWidth() - getPaddingLeft() - likeButton.getMeasuredWidth(),
                getMeasuredHeight());


        checkViewParams(textView.getView());
    }

    public void setSecondaryText(String text) {
        textView.setSecondaryText(text);
    }

//    public void setProgress(int progress) {
//        progressBar.setProgress(progress);
//    }

    public void setSecondaryText(int text) {
        String append = text == 1 ? " vote" : " votes";
        setSecondaryText(String.valueOf(text) + append);
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void castVote() {
        voteManager.removeVote(voteCharityAdapter.getVotedFor(), MainMenu.email);
        voteManager.castVote(link, MainMenu.email);
        voteCharityAdapter.setVotedFor(link);
        MainMenu.getCurrentCharity();
    }

    private void removeThisVote() {
        voteCharityAdapter.setVotedFor("");
        voteManager.removeVote(voteCharityAdapter.getVotedFor(), MainMenu.email);
        MainMenu.getCurrentCharity();
    }

    private void removeCurrentVote() {
        voteCharityAdapter.setVotedFor("");
        voteManager.removeVote(voteCharityAdapter.getVotedFor(), MainMenu.email);
        MainMenu.getCurrentCharity();
    }

    private void openLink() {
        try {
            if (link != null && link.length() > 3) {
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));//currentCharity));
                getContext().startActivity(browserIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();
        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            measureChild(view, MeasureSpec.AT_MOST, MeasureSpec.AT_MOST);
            view.requestLayout();
            view.invalidate();
            requestLayout();

        }
    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();
        checkViewParams(view, layoutWidth, layoutHeight);
    }

    public enum QueItemType {
        REMOVE, CAST
    }

    public static class QueItem {
        public final QueItemType type;
        public final String link;

        public QueItem(QueItemType type, String link) {
            this.type = type;
            this.link = link;
        }

        @Override
        public String toString() {
            return String.valueOf(type) + " : " + link;
        }
    }

    public class LikeButton extends ImageView {
        public LikeButton(Context context) {
            super(context);
            init();
        }

        public LikeButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            super.setOnClickListener(onClickListener);
            setWillNotDraw(false);
            setScaleType(ScaleType.CENTER_INSIDE);
            final int p = dpToPixels(8);
            LikeButton.this.setPadding(p, p, p, p);
            invalidate();
        }

        private void invalidatePoster() {
            this.post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
        }


        public void setIsVotedFor(boolean isVotedFor) {
            votedFor = isVotedFor;
            setImageResource(isVotedFor ? R.drawable.like : R.drawable.not_liked);
        }

        public void click() {
            setIsVotedFor(!votedFor);
            if (link.equals(voteCharityAdapter.getVotedFor())) {
                removeThisVote();
                setVotedFor(false);
                try {
                    votes--;
                    textView.setSecondaryText(String.valueOf(votes));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (voteCharityAdapter != null) {
                    voteCharityAdapter.setVotedFor("");
                }
            } else {
                try {
                    votes++;
                    textView.setSecondaryText(String.valueOf(votes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                castVote();
                setVotedFor(true);
                if (voteCharityAdapter != null) {
                    voteCharityAdapter.setVotedFor(link);
                }
            }

            if (voteCharityAdapter != null) {
                voteCharityAdapter.notifyDataSetChanged();
            }
            CharityListItem.this.invalidatePoster();
        }

        @Override
        public void invalidate() {
            super.invalidate();
        }

    }
}
