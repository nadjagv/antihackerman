package antihackerman.backendapp.util;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;

import java.util.regex.Pattern;

public class InputValidationUtil {

    public static boolean isEmailValid(String email){
        //OWASP validation regex
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public static boolean isPasswordValid(String password){
//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])  # a special character must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,}             # anything, at least eight places though
//        $                 # end-of-string
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        boolean regexOk = Pattern.compile(regexPattern)
                .matcher(password)
                .matches();

        //check length and blacklist
        PasswordPolicy policy = new PasswordPolicy(BreachDatabase.top100K(), 8, 64);
        Status result = policy.check(password);

        return regexOk && result.equals(Status.OK);
    }

    public static boolean isUsernameValid(String username){
        //6-30 chars, only alphanum and underscore allowed, first character must be alphabetic
        String regexPattern = "^[A-Za-z]\\w{5,29}$";
        return Pattern.compile(regexPattern)
                .matcher(username)
                .matches();
    }
}
