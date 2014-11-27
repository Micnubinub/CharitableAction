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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
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
    private final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
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
    private RippleTextView textView;
    private PlusButton plusButton;
    private ProgressBar progressBar;
    private int width;
    private int height;
    private int clickedX, clickedY;
    private String link;
    private boolean touchDown = false, animateRipple, votedFor;
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            ripple_animated_value = (Float) (animation.getAnimatedValue());
            invalidatePoster();
        }
    };
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown)
                ripple_animated_value = 0;

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
    private int rippleR;
    private int rippleColor = 0x25000000;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                rippleR = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

                valueAnimator.start();

                touchDown = true;
                animateRipple = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchDown = false;

                if (!valueAnimator.isRunning()) {
                    ripple_animated_value = 0;
                    invalidatePoster();
                }
                break;
        }
        return true;
    }

    public void setText(String text) {
        textView.setText(text);
        invalidatePoster();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        final int padding = dpToPixels(16);
        final int buttonWidth = dpToPixels(72);

        textView = new RippleTextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        textView.setTextSize(22);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(padding, padding, padding, padding);
        textView.setOnClickListener(onClickListener);

        plusButton = new PlusButton(getContext());
        plusButton.setLayoutParams(new LayoutParams(buttonWidth, buttonWidth));
        plusButton.setPadding(padding, padding, padding, padding);

        progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, padding / 5));

        setWillNotDraw(false);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(animatorUpdateListener);
        valueAnimator.addListener(animatorListener);
        valueAnimator.setDuration(duration);
        paint.setColor(0x25000000);
        addView(progressBar);
        addView(textView);
        addView(plusButton);


        manager = AccountManager.get(getContext());
        accounts = manager.getAccountsByType("com.google");
    }

    public void setPrimaryTextSize(int sp) {
        textView.setTextSize(sp);
    }

    public void setPrimaryTextColor(int color) {
        textView.setTextColor(color);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return false;

    }

    public void setDuration(int duration) {
        CharityListItem.duration = duration;
        valueAnimator.setDuration(duration);
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void setRippleColor(int color) {
        rippleColor = color;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            paint.setColor(rippleColor);
            canvas.drawCircle(clickedX, clickedY, rippleR * ripple_animated_value, paint);
        }
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
        progressBar.layout(0, getHeight() - progressBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());

        final int imageViewPadding = (getMeasuredHeight() - plusButton.getMeasuredHeight()) / 2;
        plusButton.layout(getMeasuredWidth() - getPaddingLeft() - plusButton.getMeasuredWidth(),
                imageViewPadding,
                getMeasuredWidth() - getPaddingLeft(),
                getMeasuredHeight() - imageViewPadding
        );

        final int textViewPadding = (getMeasuredHeight() - textView.getMeasuredHeight()) / 2;
        textView.layout(getPaddingLeft(), textViewPadding,
                getMeasuredWidth() - getPaddingLeft() - plusButton.getMeasuredWidth(),
                getMeasuredHeight() - textViewPadding);

        checkViewParams(textView);
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

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();
        checkViewParams(view, layoutWidth, layoutHeight);
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void castVote() {
        for (Account account : accounts) {
            if (account.name.contains("@gmail")) {

                removeVote();
                voteManager.castVote(currentVote, account.name);
                break;
            }
        }
    }

    private void removeVote() {
        for (Account account : accounts) {
            if (account.name.contains("@gmail")) {
                voteManager.removeVote(account.name);
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
            paint.setTextSize(rippleR);
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
