package choongyul.android.com.soundplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class SlideOutView extends TextView {

    int strikeColor;
    boolean strikable = true;
    boolean isStriked = false;
    public SlideOutView(Context context) {
        super(context);
    }

    public SlideOutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideOutView/*스타일러블 셋 전체를 가져올 것이기 때문에 <declare-styleable name="SlideOutView"> 에 붙은 이름을 가져와야한ㄷ. */);
        strikeColor = ta.getColor(R.styleable.SlideOutView_strike_color, Color.RED);
        strikable = ta.getBoolean(R.styleable.SlideOutView_strikable, true);

        // gc 의 활동량을 줄이기 위해 recycle 하여 쓴다.
        ta.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isStriked) {
            Paint paint = new Paint();
            paint.setColor(strikeColor);
            canvas.drawLine(getX()-getPaddingLeft(), getY()+getHeight()/2, getX() + getWidth() - getPaddingRight(), getY()+getHeight()/2 , paint);
            Log.e("onDraw", "그리고있다.");
        }

    }

    public void strike(boolean isStriked) {
        this.isStriked = isStriked;
        invalidate();
        Log.e("onDraw", "다시그렸다.");

    }
}
