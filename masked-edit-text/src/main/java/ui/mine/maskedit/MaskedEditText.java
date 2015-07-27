package ui.mine.maskedit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

public class MaskedEditText extends EditText {

    private String mask;

    public MaskedEditText(Context context) {
        super(context);
    }

    public MaskedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTextWatcher(context, attrs);
    }

    public MaskedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setTextWatcher(context, attrs);
    }

    /**
     * XML methods
     *
     * @param ctx
     * @param attrs
     */
    private void setTextWatcher(Context ctx, AttributeSet attrs) {
        TypedArray attributes = ctx.obtainStyledAttributes(attrs, R.styleable.MaskedEditText);
        mask = attributes.getString(R.styleable.MaskedEditText_mask);

        if (mask != null) {
            addTextChangedListener(Mask.insert(mask, this));
        }
    }

    public String getTextValueNumber() {
        String stringWithoutMask = this.getText().toString();
        stringWithoutMask = stringWithoutMask.replaceAll("[^0-9]", "");
        return stringWithoutMask;
    }
}
