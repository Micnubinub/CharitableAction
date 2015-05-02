package bigshots.people_helping_people.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.VoteManager;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;
import bigshots.people_helping_people.views.CharityListItem.QueItem;
import bigshots.people_helping_people.views.CharityListItem.QueItemType;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class CharityListItemPedestal extends FrameLayout {
    public static final VoteManager voteManager = new VoteManager();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static ArrayList<CharityListItem.QueItem> queItems;
    public static String currentVote;
    private static VoteCharityAdapter voteCharityAdapter;
    private static TextView charityName, description, votesText;
    private static FrameLayout likeButtoncontainer;
    private static LikeButton likeButton;
    private static Charity charity;
    private static int votes;
    private static int clickedX, clickedY;
    private static String link;
    private static boolean touchDown = false, votedFor, trusted;
    private static int pos;
    private static String name;
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

    public static String getCurrentVote() {
        return currentVote;
    }

    public static void setCurrentVote(String currentVote) {
        CharityListItemPedestal.currentVote = currentVote;
    }

    public static boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {
        likeButton.setIsVotedFor(votedFor);
        invalidatePoster();
    }

    public void setCharity(Charity charity) {
        if (charity == null)
            return;
        //Todo
        CharityListItemPedestal.charity = charity;
        charityName.setText(charity.getName());
        description.setText(charity.getDescription());
        setVotes(charity.getVotes());
        Log.e("setChaity", charity.toString());

    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setCharityName(String text) {
        name = text;
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
        likeButton.setLayoutParams(new LayoutParams(buttonWidth, buttonWidth));
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
        queItems = CharityListItem.queItems;
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

    public void setLink(String link) {
        this.link = link;
    }

    private void castVote() {
        queItems.add(new QueItem(QueItemType.CAST, link));
        Log.e("remove", String.format("queItemSize : %d", queItems.size()));
        currentVote = link;
        MainMenu.getCurrentCharity();
    }

    private void removeThisVote() {
        currentVote = "";
        queItems.add(new QueItem(QueItemType.REMOVE, ""));
        Log.e("remove", String.format("queItemSize : %d", queItems.size()));
        MainMenu.getCurrentCharity();
    }

    private void removeCurrentVote() {
        currentVote = "";
        queItems.add(new CharityListItem.QueItem(CharityListItem.QueItemType.REMOVE, ""));
        Log.e("remove", String.format("queItemSize : %d", queItems.size()));
        MainMenu.getCurrentCharity();
//        if (currentVote == null) {
//            MainMenu.getCurrentCharity();
//            Toast.makeText(MainMenu.context, "failed to cast vote, try again later", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (MainMenu.email.length() > 3)
//            voteManager.removeVote(currentVote, MainMenu.email);

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
            if (link.equals(currentVote)) {
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
                    voteCharityAdapter.notifyDataSetChanged();
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
                    voteCharityAdapter.setVotedFor(link);
                    voteCharityAdapter.notifyDataSetChanged();
                }
            }
            CharityListItemPedestal.this.invalidatePoster();
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
