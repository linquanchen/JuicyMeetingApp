package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pufanjiang on 12/6/15.
 */
public class JuicyFont {
    public final static String OPEN_SANS_REGULAR = "OPEN_SANS_REGULAR";

    private static Map<String, Typeface> faceMap;
    private static Typeface face;

    private static JuicyFont juicyFont;
    private JuicyFont(){}
    public static void initialize(Context context) {
        juicyFont = new JuicyFont();
        faceMap = new HashMap<String, Typeface>();

        face = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        faceMap.put(OPEN_SANS_REGULAR, face);
    }

    public static JuicyFont getInstance() {
        return juicyFont;
    }

    public void setFont(TextView tv, String fontName) {
        tv.setTypeface(faceMap.get(fontName));
    }
}
