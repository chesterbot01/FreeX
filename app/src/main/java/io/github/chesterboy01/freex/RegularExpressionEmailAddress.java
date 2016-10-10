package io.github.chesterboy01.freex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 10/9/2016.
 */

public class RegularExpressionEmailAddress {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailAddressMatched(String emailAddress){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailAddress);
        return matcher.find();
    }
}
