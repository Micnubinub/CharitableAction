package bigshots.people_helping_people.views;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.VoteManager;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class CharityListItem extends ViewGroup {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static final VoteManager voteManager = new VoteManager();
    private static int duration = 800;
    private static AccountManager manager;
    private static Account[] accounts;
    private static String currentVote;
    private static VoteCharityAdapter voteCharityAdapter;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof PlusButton) {
                ((PlusButton) v).click();
            } else {
                openLink();
            }

        }
    };
    private MaterialTwoLineText textView;
    private PlusButton plusButton;
    // private ProgressBar progressBar;
    private int width;
    private int height;
    private int votes;
    private int clickedX, clickedY;
    private String link;
    private boolean touchDown = false, votedFor;
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
        return currentVote;
    }

    public static void setCurrentVote(String currentVote) {
        CharityListItem.currentVote = currentVote;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {
        plusButton.setIsVotedFor(votedFor);
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
        final int buttonWidth = dpToPixels(68);

        textView = new MaterialTwoLineText(getContext());
        textView.setPrimaryTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, buttonWidth));
        //textView.setPrimaryTextSize(22);
        textView.setOnClickListener(onClickListener);

        plusButton = new PlusButton(getContext());
        plusButton.setLayoutParams(new LayoutParams(buttonWidth, buttonWidth));
        plusButton.setPadding(padding, padding, padding, padding);

//        progressBar = new ProgressBar(getContext())   ;
//        progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, padding / 5));

        setWillNotDraw(false);
        paint.setColor(0x25000000);
//        addView(progressBar);
        addView(textView);
        addView(plusButton);


        manager = AccountManager.get(getContext());
        accounts = manager.getAccounts();
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


    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                plusButton.invalidatePoster();
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

        final int imageViewPadding = (getMeasuredHeight() - plusButton.getMeasuredHeight()) / 2;
        plusButton.layout(getMeasuredWidth() - getPaddingLeft() - plusButton.getMeasuredWidth(),
                imageViewPadding,
                getMeasuredWidth() - getPaddingLeft(),
                getMeasuredHeight() - imageViewPadding
        );

        final int textViewPadding = (getMeasuredHeight() - textView.getMeasuredHeight()) / 2;
        textView.layout(getPaddingLeft(), 0,
                getMeasuredWidth() - getPaddingLeft() - plusButton.getMeasuredWidth(),
                getMeasuredHeight());

        checkViewParams(textView);
    }

    public void setSecondaryText(String text) {
        textView.setSecondaryText(text);
    }

    public void setSecondaryText(int text) {
        String append = text == 1 ? " vote" : " votes";
        setSecondaryText(String.valueOf(text) + append);
    }

//    public void setProgress(int progress) {
//        progressBar.setProgress(progress);
//    }


    public void setLink(String link) {
        this.link = link;
    }

    private void castVote() {
        for (Account account : accounts) {
            if (account.name.contains("@")) {
                removeCurrentVote();
                voteManager.castVote(link, account.name);
                currentVote = link;
                break;
            }
        }
    }

    private void removeThisVote() {
        for (Account account : accounts) {
            if (account.name.contains("@")) {
                voteManager.removeVote(link, account.name);
                break;
            }
        }
    }

    private void removeCurrentVote() {
        for (Account account : accounts) {
            if (account.name.contains("@")) {
                voteManager.removeVote(currentVote, account.name);
                break;
            }
        }
    }

    private void openLink() {
        try {
            if (link != null && link.length() > 3) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));//currentCharity));
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

    public class PlusButton extends ImageView {
        public PlusButton(Context context) {
            super(context);
            init();
        }

        public PlusButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public PlusButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            super.setOnClickListener(onClickListener);
            setWillNotDraw(false);
            setScaleType(ScaleType.CENTER_INSIDE);
            final int p = dpToPixels(12);
            PlusButton.this.setPadding(p, p, p, p);
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
            if (link.equals(currentVote)) {

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
                    voteCharityAdapter.notifyDataSetChanged();
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
                    voteCharityAdapter.notifyDataSetChanged();
                }
            }
            CharityListItem.this.invalidatePoster();
            if (voteCharityAdapter != null) {
                voteCharityAdapter.notifyDataSetChanged();
            }
        }


        @Override
        public void invalidate() {
            super.invalidate();
        }

    }
}
