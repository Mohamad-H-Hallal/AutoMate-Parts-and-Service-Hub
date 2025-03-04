package com.example.project.Controller;

import android.text.InputFilter;
import android.text.Spanned;

public class TwoDecimalPlacesInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if (source.length() == 0) {
            return null;
        }

        char firstChar = source.charAt(0);
        if (firstChar == '-') {
            return "";
        }

        if (!Character.isDigit(firstChar) && firstChar != '.') {
            return "";
        }

        int dotPos = -1;
        int length = dest.length();
        for (int i = 0; i < length; ++i) {
            if (dest.charAt(i) == '.') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0 && dest.length() - dotPos > 2) {
            return "";
        }
        return null;
    }
}
