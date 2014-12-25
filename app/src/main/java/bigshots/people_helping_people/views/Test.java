package bigshots.people_helping_people.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import bigshots.people_helping_people.R;

/**
 * Created by root on 25/12/14.
 */
public class Test extends View {
    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.material_red));
        paint.setColor(getResources().getColor(R.color.white));
    }

    public Test(Context context) {
        super(context);
        // Toast.makeText(getContext(), Utils.getTotal(getContext()), Toast.LENGTH_LONG).show();
        setBackgroundColor(getResources().getColor(R.color.material_red));
        paint.setColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpikes(canvas, true, 35, 100, 5, 5, 50, 5, 5, 5);
    }

    private void drawSpikes(
            Canvas canvas, boolean left, int w, int h, int x, int y,
            int spikesYOffset, int spikeW, int spikeH, int spikeNum
    ) {

        canvas.drawLine(left ? x : x + spikeW, y, left ? x + w : x + spikeW + w, y, paint);

        canvas.drawLine(left ? x + w : x + spikeW, y, left ? x + w : x + spikeW, y + spikesYOffset, paint);
        //Todo draw spikes

        int currY = y + spikesYOffset;
        int spikeX = left ? x + w : x + spikeW;

        for (int i = 0; i < spikeNum; i++) {
            canvas.drawLine(spikeX, currY, left ? spikeX + spikeW : x, currY + spikeH / 2, paint);
            canvas.drawLine(left ? spikeX + spikeW : x, currY + spikeH / 2, spikeX, currY + spikeH, paint);
            currY += spikeH;
        }

        canvas.drawLine(left ? x + w : x + spikeW, y + spikesYOffset + (spikeH * spikeNum), left ? x + w : x + spikeW, y + h, paint);
        canvas.drawLine(left ? x : x + spikeW, y + h, left ? x + w : x + spikeW + w, y + h, paint);
        canvas.drawLine(left ? x : x + spikeW + w, y + h, left ? x : x + spikeW + w, y, paint);
    }
}
