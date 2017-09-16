package com.nandy.vkchanllenge;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by yana on 16.09.17.
 */

public abstract class SimpleTextWatcher implements TextWatcher {

    public abstract void onTextChanged(CharSequence text, int length);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onTextChanged(charSequence, charSequence.length());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
