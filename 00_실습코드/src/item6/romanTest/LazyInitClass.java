package item6.romanTest;

import java.util.regex.Pattern;

public class LazyInitClass {
    private static Pattern ROMAN;

    private static synchronized Pattern getROMAN() {
        if(ROMAN == null) {
            ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})"
                            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
        }
        return ROMAN;
    }
    static boolean isRomanNumeral(String s) {
        return getROMAN().matcher(s).matches();
    }
}
