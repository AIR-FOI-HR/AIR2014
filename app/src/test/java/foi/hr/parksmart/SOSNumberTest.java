package foi.hr.parksmart;

import org.junit.Test;

import java.util.regex.Pattern;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SOSNumberTest {
    @Test
    public void SOSNumber_DefaultIsValid() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "+385112";

        org.junit.Assert.assertTrue(Pattern.matches(regexObrazac,numberForSosCheck));
    }
    @Test
    public void SOSNumber_Empty() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "";

        org.junit.Assert.assertTrue(!Pattern.matches(regexObrazac,numberForSosCheck));
    }
    @Test
    public void SOSNumber_MobileisValid() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "+385992715845";

        org.junit.Assert.assertTrue(Pattern.matches(regexObrazac,numberForSosCheck));
    }
    @Test
    public void SOSNumber_MobileIsInvalid() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "+3859512348";

        org.junit.Assert.assertTrue(!Pattern.matches(regexObrazac,numberForSosCheck));
    }
    @Test
    public void SOSNumber_PhoneIsValid() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "+38534229200";

        org.junit.Assert.assertTrue(Pattern.matches(regexObrazac,numberForSosCheck));
    }
    @Test
    public void SOSNumber_PhoneIsInvalid() {
        String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
        String numberForSosCheck = "+385229200";

        org.junit.Assert.assertTrue(!Pattern.matches(regexObrazac,numberForSosCheck));
    }

}