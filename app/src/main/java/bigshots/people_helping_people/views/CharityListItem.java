package bigshots.people_helping_people.views;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.VoteManager;

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
    private int clickedX, clickedY;
    private String link;
    private boolean touchDown = false, votedFor;
    private int pos;

    public CharityListItem(Context context) {
        super(context);
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

    public boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {
        this.votedFor = votedFor;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public void setPrimaryText(String text) {
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
            if (account.name.contains("@gmail")) {
                removeVote();
                voteManager.castVote(link, account.name);
                Toast.makeText(getContext(), "casting :" + link, Toast.LENGTH_SHORT).show();
                currentVote = link;
                break;
            }
        }
    }

    private void removeVote() {
        for (Account account : accounts) {
            if (account.name.contains("@gmail")) {
                voteManager.removeVote(account.name);
                Toast.makeText(getContext(), "removing :" + account.name, Toast.LENGTH_SHORT).show();
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

    public class PlusButton extends Button {

        private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animateRipple = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                animateRipple = false;
                invalidatePoster();
            }

            @Override
            public void onAnimationCancel(Animator animator) {


            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ripple_animated_value = (Float) (animation.getAnimatedValue());

                invalidatePoster();
            }
        };

        private int cx, cy;
        private boolean votedFor = false, animateRipple;
        private float ripple_animated_value = 0;
        private int rippleR;

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
            animator.setDuration((int) (duration * 0.75));
            animator.addUpdateListener(animatorUpdateListener);
            animator.addListener(animatorListener);
            animator.setInterpolator(interpolator);
            paint.setTextAlign(Paint.Align.CENTER);
            setWillNotDraw(false);
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            setText(getString());
            setTextColor(0xffffffff);
            setBackgroundColor(0x00000000);
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

        @Override
        protected void onDraw(Canvas canvas) {
            if (animateRipple && animator.isRunning()) {
                paint.setColor(getColor(votedFor));
                canvas.drawCircle(cx, cy, rippleR, paint);
                paint.setColor(getColor(!votedFor));
                canvas.drawCircle(cx, cy, rippleR * (1 - ripple_animated_value), paint);
            } else {
                paint.setColor(getColor(votedFor));
                canvas.drawCircle(cx, cy, rippleR, paint);
            }
            super.onDraw(canvas);
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            cx = w / 2;
            cy = h / 2;
            w = w - getPaddingLeft() - getPaddingRight();
            h = h - getPaddingTop() - getPaddingBottom();

            rippleR = Math.min(w, h) / 2;

        }


        public void setIsVotedFor(boolean votedFor) {
            this.votedFor = votedFor;
            setText(getString());
        }

        private void startAnimator() {
            if (animator.isRunning()) {
                animator.end();
                animator.cancel();
            }

            animator.start();
        }


        public void click() {
            setIsVotedFor(!votedFor);
            startAnimator();
            if (isVotedFor())
                removeVote();
            else
                castVote();
        }


        private int getColor(boolean b) {
            return b ? 0xffe51c23 : 0xff42bd41;
        }

        private String getString() {
            return votedFor ? "-" : "+";
        }
    }
}
