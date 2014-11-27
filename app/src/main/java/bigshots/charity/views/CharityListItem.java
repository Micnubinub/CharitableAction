package bigshots.charity.views;

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

import bigshots.charity.R;
import bigshots.charity.io.VoteManager;

/**
 * Created by root on 19/11/14.
 */
public class CharityListItem extends ViewGroup {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 800;
    private static AccountManager manager;
    private static Account[] accounts;
    private static VoteManager voteManager = new VoteManager();
    private static String currentVote;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private RippleText textView;
    private PlusButton plusButton;
    private int width;
    private int height;
    //private float animated_value;
    private int clickedX, clickedY;
    private String link;
    private boolean touchDown = false, animateRipple, votedFor;//Todo write code for voted for
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

                animator.start();

                touchDown = true;
                animateRipple = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchDown = false;

                if (!animator.isRunning()) {
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
        //Todo consider 16 and 14 (in the guidelines)
        final int padding = dpToPixels(16);
        final int buttonWidth = dpToPixels(72);

        textView = new RippleText(getContext());
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        textView.setTextSize(22);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("1,2 testing");

        plusButton = new PlusButton(getContext());
        plusButton.setLayoutParams(new LayoutParams(buttonWidth, buttonWidth));
        plusButton.setPadding(padding, padding, padding, padding);

        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);
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
        animator.setDuration(duration);
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

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();
        checkViewParams(view, layoutWidth, layoutHeight);
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void castVote() {
        //Todo cast vote

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
        //Todo open link
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

        private OnClickListener listener, onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
                if (listener != null)
                    listener.onClick(v);
            }
        };

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
            animator.setDuration(duration);
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
                paint.setColor(getColor(!votedFor));
                canvas.drawCircle(cx, cy, rippleR, paint);
                paint.setColor(getColor(votedFor));
                canvas.drawCircle(cx, cy, rippleR * ripple_animated_value, paint);
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

        @Override
        public void setOnClickListener(OnClickListener l) {
            listener = l;
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


        private void click() {
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
