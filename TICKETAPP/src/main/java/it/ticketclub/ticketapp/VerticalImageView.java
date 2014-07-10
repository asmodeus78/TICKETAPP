package it.ticketclub.ticketapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Gio on 08/07/2014.
 */
public class VerticalImageView extends ImageView {
    private Rect bounds = new Rect();
    private ImageView textPaint;

    private int color;

    public VerticalImageView(Context context) {
        super(context);
    }

    public VerticalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //color = getCurrentTextColor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //textPaint = getPaint();
       // textPaint.getTextBounds((String) getText(), 0, getText().length(), bounds);
        setMeasuredDimension((int) (bounds.height() ), bounds.width());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //textPaint.setColor(color);
        canvas.rotate(-90, bounds.width(), 0);
        //canvas.drawBitmap();

        //canvas.drawText((String) getText(), 0, -bounds.width() + bounds.height(), textPaint);

    }
}
