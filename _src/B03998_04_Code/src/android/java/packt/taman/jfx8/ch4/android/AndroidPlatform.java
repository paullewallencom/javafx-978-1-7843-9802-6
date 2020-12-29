package packt.taman.jfx8.ch4.android;

import android.content.Intent;
import android.net.Uri;
import javafxports.android.FXActivity;
import packt.taman.jfx8.ch4.Platform;

/**
 *
 * @author jpereda
 */
public class AndroidPlatform implements Platform {

    @Override
    public void callNumber(String number) {
        if (!number.equals("")) {
            Uri uriNumber = Uri.parse("tel:" + number);
            Intent dial = new Intent(Intent.ACTION_CALL, uriNumber);
            FXActivity.getInstance().startActivity(dial);
         }
    }
    
}
