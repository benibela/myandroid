package de.benibela.myandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EyeTestView extends View {


    public EyeTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorMode = 0;
    }

    private int colorMode;
    private int borderdelta = 0;

    private void setColor(int mode, Paint p){
        switch ( mode ) {
            case 0: p.setARGB(255, 0, 0, 0); break;
            case 1: p.setARGB(255,255,255,255); break;
            case 2: p.setARGB(255,255,0,0); break;
            case 3: p.setARGB(255,0,255,0); break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect r = new Rect();
        getDrawingRect(r);


        Paint foreground = new Paint(); setColor( (colorMode < 3) ? colorMode + 1 : 0, foreground);
        Paint background = new Paint(); setColor( (colorMode >= 3) ? colorMode - 2 : 0, background);

        canvas.drawRect(r, background);

        float fborder = 5 + borderdelta;
        while (r.width() > 5*fborder && r.height() > 5*fborder) {
            int border = (int)fborder;
            r.inset(3*border, 3*border);
            canvas.drawRect(r, foreground);
            r.inset(border, border);
            canvas.drawRect(r, background);
            fborder -= 0.25;
            if (fborder < 1) fborder = 1;
        }
    }

    private float sx, sy;
    private boolean dragging;
    private int oldborderdelta;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float dx = event.getX() - sx;
        float dy = event.getY() - sy;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sx = event.getX();
                sy = event.getY();
                dragging = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(dx) + Math.abs(dy) > 30 ) {
                    if (!dragging) oldborderdelta = borderdelta;
                    dragging = true;
                }
                if (dragging) {
                    int oldoldborderdelta = borderdelta;
                    borderdelta = (oldborderdelta + (int)(dx+dy) / 20);
                    if (borderdelta < -5) borderdelta = -5;
                    if (borderdelta > 30) borderdelta = 30;
                    if (borderdelta != oldoldborderdelta) invalidate();
                    return true;
                }
                return false;
            case MotionEvent.ACTION_UP:
                if (!dragging) {
                    colorMode++;
                    colorMode = colorMode % 6;
                }
                invalidate();
                return true;
        }
        return false;
    }
}
