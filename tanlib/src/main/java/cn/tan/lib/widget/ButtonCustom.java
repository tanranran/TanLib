package cn.tan.lib.widget;

import cn.tan.lib.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ButtonCustom extends TextView {

    private static final int DEFAULT_BUTTON_RADIUS = 0;
    private static final int DEFAULT_BUTTON_GAP = 3;
    private static final int DEFAULT_BG_PRESS_COLOR = Color.BLACK;
    private static final int DEFAULT_BG_UNPRESS_COLOR = Color.RED;
    private static final int DEFAULT_TXT_PRESS_COLOR = Color.WHITE;
    private static final int DEFAULT_TXT_UNPRESS_COLOR = Color.BLACK;
    private static final int DEFAULT_DASHED_COLOR = Color.GREEN;
    private int press_color = DEFAULT_BG_PRESS_COLOR;
    private int unpress_color = DEFAULT_BG_UNPRESS_COLOR;
    private int txt_press_color = DEFAULT_TXT_PRESS_COLOR;
    private int txt_unpress_color = DEFAULT_TXT_UNPRESS_COLOR;
    private int dashed_press_color = DEFAULT_DASHED_COLOR;
    private int dashed_unpress_color = DEFAULT_DASHED_COLOR;
    private int radius = DEFAULT_BUTTON_RADIUS;
    private int dashed_width = DEFAULT_BUTTON_GAP;
    private int dashed_gap = DEFAULT_BUTTON_GAP;
    private int dashed_height=DEFAULT_BUTTON_GAP;
    private boolean isSquare = false;
    private boolean isDashed = false;
    private boolean isSrc = false;
    private Drawable press_res, unpress_res;
    private Drawable drawable_press, drawable_unpress;
    private Drawable src_press, src_unpress;
    private int pressed = android.R.attr.state_pressed;
    private TextView textView;
    public ButtonCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ButtonCustom, defStyle, 0);
        press_color = a.getColor(R.styleable.ButtonCustom_press_color, DEFAULT_BG_PRESS_COLOR);
        unpress_color = a.getColor(R.styleable.ButtonCustom_unpress_color, DEFAULT_BG_UNPRESS_COLOR);
        txt_press_color = a.getColor(R.styleable.ButtonCustom_txt_press_color, DEFAULT_TXT_PRESS_COLOR);
        txt_unpress_color = a.getColor(R.styleable.ButtonCustom_txt_unpress_color, DEFAULT_TXT_UNPRESS_COLOR);
        dashed_press_color = a.getColor(R.styleable.ButtonCustom_dashed_press_color, DEFAULT_DASHED_COLOR);
        dashed_unpress_color=a.getColor(R.styleable.ButtonCustom_dashed_unpress_color, DEFAULT_DASHED_COLOR);
        radius = a.getDimensionPixelSize(R.styleable.ButtonCustom_radius, DEFAULT_BUTTON_RADIUS);
        press_res = a.getDrawable(R.styleable.ButtonCustom_press_res);
        unpress_res = a.getDrawable(R.styleable.ButtonCustom_unpress_res);
        isSquare = a.getBoolean(R.styleable.ButtonCustom_square, false);
        isDashed = a.getBoolean(R.styleable.ButtonCustom_dashed, false);
        dashed_width = a.getDimensionPixelSize(R.styleable.ButtonCustom_dashed_width, DEFAULT_BUTTON_GAP);
        dashed_height=a.getDimensionPixelSize(R.styleable.ButtonCustom_dashed_height, DEFAULT_BUTTON_GAP);
        dashed_gap = a.getDimensionPixelSize(R.styleable.ButtonCustom_dashed_gap, DEFAULT_BUTTON_GAP);
        src_press = a.getDrawable(R.styleable.ButtonCustom_src_press);
        src_unpress = a.getDrawable(R.styleable.ButtonCustom_src_unpress);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ButtonCustom_press_color) {
                GradientDrawable gradientDrawablepress = new GradientDrawable();
                gradientDrawablepress.setCornerRadius(radius);
                gradientDrawablepress.setColor(press_color);
                if (isDashed) {
                    gradientDrawablepress.setShape(GradientDrawable.RECTANGLE);
                    gradientDrawablepress.setStroke(dashed_height, dashed_press_color, dashed_width, dashed_gap);
                }
                drawable_press = gradientDrawablepress;
            } else if (attr == R.styleable.ButtonCustom_unpress_color) {
                GradientDrawable gradientDrawableunPress = new GradientDrawable();
                gradientDrawableunPress.setCornerRadius(radius);
                gradientDrawableunPress.setColor(unpress_color);
                if (isDashed) {
                    gradientDrawableunPress.setShape(GradientDrawable.RECTANGLE);
                    gradientDrawableunPress.setStroke(dashed_height, dashed_unpress_color, dashed_width, dashed_gap);
                }
                drawable_unpress = gradientDrawableunPress;
            } else if (attr == R.styleable.ButtonCustom_txt_press_color) {
            } else if (attr == R.styleable.ButtonCustom_txt_unpress_color) {
            } else if (attr == R.styleable.ButtonCustom_radius) {
            } else if (attr == R.styleable.ButtonCustom_press_res) {
                drawable_press = press_res;
            } else if (attr == R.styleable.ButtonCustom_unpress_res) {
                drawable_unpress = unpress_res;
            } else if (attr == R.styleable.ButtonCustom_src_press) {
                isSrc = true;
            } else if (attr == R.styleable.ButtonCustom_src_unpress) {
                isSrc = true;
            }
        }
        a.recycle();
        initView();
    }

    public ButtonCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonCustom(Context context) {
        this(context, null);
    }

    @SuppressWarnings("deprecation")
    public void initView() {
        textView=new TextView(getContext());
        textView.setClickable(true);
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, textView.getPaint());
        int[] colors = new int[]{txt_press_color, txt_unpress_color};
        int[][] states = new int[][]{new int[]{pressed}, new int[]{-pressed}};
        ColorStateList colorStateList = new ColorStateList(states, colors);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{pressed}, drawable_press);
        stateListDrawable.addState(new int[]{-pressed}, drawable_unpress);
        if (isSrc) {
            StateListDrawable stateListSrc = new StateListDrawable();
            stateListSrc.addState(new int[]{pressed}, src_press);
            stateListSrc.addState(new int[]{-pressed}, src_unpress);
            textView.setCompoundDrawablesWithIntrinsicBounds(stateListSrc, null, null, null);
        }
        if(src_press!=null||src_press==null){
            setBackgroundDrawable(stateListDrawable);
        }
        textView.setTextColor(colorStateList);
    }
    protected void onDraw(Canvas canvas) {
        if (isSrc) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null) {
                Drawable drawableLeft = drawables[0];
                if (drawableLeft != null) {
                    float textWidth = textView.getPaint().measureText(textView.getText().toString());
                    int drawablePadding = textView.getCompoundDrawablePadding();
                    int drawableWidth = 0;
                    drawableWidth = drawableLeft.getIntrinsicWidth();
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    canvas.translate((getWidth() - bodyWidth) / 2, 0);
                }
            }
        }
        super.onDraw(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSquare) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            int childHeightSize = getMeasuredHeight();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
