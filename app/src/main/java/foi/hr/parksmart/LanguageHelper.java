package foi.hr.parksmart;

import android.content.res.Resources;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageHelper {

    public static void changeLocale(Resources res, String locale) {
        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch(locale) {
            case"en":
                config.locale = Locale.ENGLISH;
                break;
            case"de":
                config.locale = Locale.GERMAN;
                break;
            default:
                config.locale = new Locale("hr");
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
