package packt.taman.jfx8.ch4.ios;

import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import packt.taman.jfx8.ch4.Platform;

/**
 *
 * @author jpereda
 */
public class IosPlatform implements Platform {

    @Override
    public void callNumber(String number) {
        if (!number.equals("")) {
            NSURL nsURL = new NSURL("telprompt://" + number);
            UIApplication.getSharedApplication().openURL(nsURL);
        }
    }
}
