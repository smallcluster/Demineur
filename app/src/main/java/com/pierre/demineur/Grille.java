package com.pierre.demineur;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class Grille extends View {
    int n, m;
    float cellSize;

    public Grille(Context context) {
        super(context);
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        m = 10;
        n = 18;
        cellSize = Math.min(width/(float) m, height / (float) n);

        float xOffset = (width - m*cellSize)/2.0f;
        float yOffset = (height - n*cellSize)/2.0f;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for(int i=0; i < n; i++){
            for(int j=0; j < m; j++){

                if( (i+j) % 2 == 0) paint.setColor(Color.rgb(155, 210, 64));
                else paint.setColor(Color.rgb(147, 202, 57));

                canvas.drawRect(j*cellSize+xOffset, i*cellSize+yOffset,
                        j*cellSize+cellSize+xOffset, i*cellSize + cellSize+yOffset,
                        paint);
            }
        }

        super.onDraw(canvas);
        }
}