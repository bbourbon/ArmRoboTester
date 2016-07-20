package br.org.cesar.armrobotester.model;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.org.cesar.armrobotester.content.TestContent;

/**
 * Created by bcb on 03/07/16.
 */
public class MotionTest {

    // Pre-configured motion
    public static final int MOTION_A = 1000;
    public static final int MOTION_B = 1001;
    public static final int MOTION_C = 1002;
    public static final int MOTION_D = 1003;

    public static final int ROTATION_0 = 0;

    public static final int ROTATION_15 = 15;
    public static final int ROTATION_30 = 30;
    public static final int ROTATION_45 = 45;
    public static final int ROTATION_60 = 60;
    public static final int ROTATION_75 = 75;

    public static final int ROTATION_90 = 90;

    public static final int ROTATION_105 = 105;
    public static final int ROTATION_120 = 120;
    public static final int ROTATION_135 = 135;
    public static final int ROTATION_150 = 150;
    public static final int ROTATION_165 = 165;

    public static final int ROTATION_180 = 180;

    public static final int ROTATION_195 = 195;
    public static final int ROTATION_210 = 210;
    public static final int ROTATION_225 = 225;
    public static final int ROTATION_240 = 240;
    public static final int ROTATION_255 = 255;

    public static final int ROTATION_270 = 270;

    public static final int ROTATION_285 = 285;
    public static final int ROTATION_300 = 300;
    public static final int ROTATION_315 = 315;
    public static final int ROTATION_330 = 330;
    public static final int ROTATION_345 = 345;

    public static final int ROTATION_360 = 360;
    private static final Map<Integer, String> sMapTypes;

    static {
        sMapTypes = new HashMap<Integer, String>();
        sMapTypes.put(ROTATION_0, "Rotation to 0");
        sMapTypes.put(ROTATION_15, "Rotation to 15");
        sMapTypes.put(ROTATION_30, "Rotation to 30");
        sMapTypes.put(ROTATION_45, "Rotation to 45");
        sMapTypes.put(ROTATION_60, "Rotation to 60");
        sMapTypes.put(ROTATION_75, "Rotation to 75");
        sMapTypes.put(ROTATION_90, "Rotation to 90");
        sMapTypes.put(ROTATION_105, "Rotation to 105");
        sMapTypes.put(ROTATION_120, "Rotation to 120");
        sMapTypes.put(ROTATION_135, "Rotation to 135");
        sMapTypes.put(ROTATION_150, "Rotation to 150");
        sMapTypes.put(ROTATION_165, "Rotation to 165");
        sMapTypes.put(ROTATION_180, "Rotation to 180");
        sMapTypes.put(ROTATION_195, "Rotation to 195");
        sMapTypes.put(ROTATION_210, "Rotation to 210");
        sMapTypes.put(ROTATION_225, "Rotation to 225");
        sMapTypes.put(ROTATION_240, "Rotation to 240");
        sMapTypes.put(ROTATION_255, "Rotation to 255");
        sMapTypes.put(ROTATION_270, "Rotation to 270");
        sMapTypes.put(ROTATION_285, "Rotation to 285");
        sMapTypes.put(ROTATION_300, "Rotation to 300");
        sMapTypes.put(ROTATION_315, "Rotation to 315");
        sMapTypes.put(ROTATION_330, "Rotation to 330");
        sMapTypes.put(ROTATION_345, "Rotation to 345");
        sMapTypes.put(ROTATION_360, "Rotation to 360");

        sMapTypes.put(MOTION_A, "Motion A");
        sMapTypes.put(MOTION_B, "Motion B");
        sMapTypes.put(MOTION_C, "Motion C");
        sMapTypes.put(MOTION_D, "Motion D");
    }

    private int mType;


    public MotionTest(int type) {
        if (!isValid(type)) {
            throw new IllegalArgumentException();
        }
        mType = type;
    }

    @NonNull
    public static Collection<String> validTypes() {
        return sMapTypes.values();
    }

    @NonNull
    public static Set<Integer> validKeys() {
        return sMapTypes.keySet();
    }

    public static Comparator<? super TestContent.MotionTestItem> getComparator() {
        Comparator<MotionTest> testComparator = new Comparator<MotionTest>() {
            @Override
            public int compare(MotionTest lhs, MotionTest rhs) {
                if (lhs != null && rhs != null) {
                    return (lhs.mType - rhs.mType);
                }
                return 0;
            }
        };
        return testComparator;
    }

    public int getType() {
        return this.mType;
    }

    @Override
    public String toString() {
        return sMapTypes.get(mType);
    }

    private boolean isValid(int type) {
        boolean result = false;
        if (sMapTypes != null) {
            result = sMapTypes.containsKey(type);
        }
        return result;
    }
}
