package bigshots.people_helping_people.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.VoteManager;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class CharityListItemPedestal extends FrameLayout {
    public static final VoteManager voteManager = new VoteManager();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static VoteCharityAdapter voteCharityAdapter;
    private static TextView charityName, description, votesText;
    private static FrameLayout likeButtoncontainer;
    private static LikeButton likeButton;
    private static Charity charity;
    private static int votes;
    private static int clickedX, clickedY;
    private static boolean touchDown = false, votedFor, trusted;
    private static int pos;
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

    public CharityListItemPedestal(Context context, VoteCharityAdapter voteCharityAdapter) {
        super(context);
        this.voteCharityAdapter = voteCharityAdapter;
        init();
    }

    public CharityListItemPedestal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {

        likeButton.setIsVotedFor(votedFor);
        invalidatePoster();
    }

    public void setCharity(final Charity charity) {
        if (charity == null)
            return;
        post(new Runnable() {
            @Override
            public void run() {
                CharityListItemPedestal.charity = charity;
                charityName.setText(charity.getName());
                description.setText(charity.getDescription());
                setVotes(charity.getVotes());
                setVotedFor(charity.getUrl().equals(voteCharityAdapter.getVotedFor()));
                likeButton.invalidatePoster();
                invalidatePoster();
            }
        });
    }

    public void setVotes(int votes) {
        this.votes = votes;
        setVotesText(votes);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setCharityName(String text) {
        charityName.setText(text);
        invalidatePoster();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        final int padding = dpToPixels(12);
        final int buttonWidth = dpToPixels(52);
        final View v = inflate(getContext(), R.layout.pedestal, null);
        v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        v.setOnClickListener(onClickListener);

        likeButtoncontainer = (FrameLayout) v.findViewById(R.id.like_button_container);
        charityName = (TextView) v.findViewById(R.id.charity);
        description = (TextView) v.findViewById(R.id.charity_description);
        votesText = (TextView) v.findViewById(R.id.votes);

        likeButton = new LikeButton(getContext());
        likeButton.setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
        likeButtoncontainer.addView(likeButton);

        setWillNotDraw(false);
        paint.setColor(0x25000000);
        try {
            removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addView(v);
        setCharity(charity);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
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

    public void setVotesText(int text) {
        final String append = text == 1 ? " vote" : " votes";
        votesText.setText(String.valueOf(text) + append);
    }

    private void castVote() {
        voteManager.removeVote(voteCharityAdapter.getVotedFor(), MainMenu.email);
        voteManager.castVote(charity.getUrl(), MainMenu.email);
        voteCharityAdapter.setVotedFor(charity.getUrl());
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
            if (charity.getUrl() != null && charity.getUrl().length() > 3) {
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(charity.getUrl()));//currentCharity));
                getContext().startActivity(browserIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            final int p = dpToPixels(7);
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
            try {
                if (charity.getUrl().equals(voteCharityAdapter.getVotedFor())) {
                    removeThisVote();
                    setVotedFor(false);
                    try {
                        votes--;
                        setVotes(votes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (voteCharityAdapter != null) {
                        voteCharityAdapter.setVotedFor("");
                    }
                } else {
                    try {
                        votes++;
                        setVotes(votes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    castVote();
                    setVotedFor(true);
                    if (voteCharityAdapter != null) {
                        voteCharityAdapter.setVotedFor(charity.getUrl());
                    }
                }

                if (voteCharityAdapter != null) {
                    voteCharityAdapter.notifyDataSetChanged();
                }
                CharityListItemPedestal.this.invalidatePoster();
            } catch (Exception e) {
                e.printStackTrace();
            }

            VoteFragment.notifyDataSetChanged();

        }

        @Override
        public void invalidate() {
            super.invalidate();
        }

    }
}
