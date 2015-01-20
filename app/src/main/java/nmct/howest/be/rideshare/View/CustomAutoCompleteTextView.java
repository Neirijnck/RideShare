package nmct.howest.be.rideshare.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

import nmct.howest.be.rideshare.R;

/* Make AutoCompleteTextView return a place description */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    private Drawable imgCross = getResources().getDrawable(R.drawable.cross);

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        /*ImageButton ib = new ImageButton(getContext());
        ib.setImageResource(R.drawable.cross);
        ib.setBackgroundColor(Color.TRANSPARENT);
        ib.setRight(1);

        ib.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getContext(),
                        "ImageButton is clicked!", Toast.LENGTH_SHORT).show();

            }

        });*/


       /* */






    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }


    void init() {
        //imgCross.setBounds(0, 0, imgCross.getIntrinsicWidth(), imgCross.getIntrinsicHeight());
        imgCross.setBounds(0, 0, 70, 70);

        handleClearButton();

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CustomAutoCompleteTextView et = CustomAutoCompleteTextView.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgCross.getIntrinsicWidth()) {
                    et.setText("");
                    CustomAutoCompleteTextView.this.handleClearButton();
                }
                return false;
            }
        });

        //if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CustomAutoCompleteTextView.this.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    void handleClearButton() {
        if (this.getText().toString().equals(""))
        {
            // add clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        }
        else
        {
            //remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], imgCross, this.getCompoundDrawables()[3]);
        }
    }

}