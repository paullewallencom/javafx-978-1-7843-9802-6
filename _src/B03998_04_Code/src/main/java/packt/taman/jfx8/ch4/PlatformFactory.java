package packt.taman.jfx8.ch4;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jpereda
 */
public class PlatformFactory {
    
    public static Platform getPlatform() {
        try {
            return (Platform) Class.forName(getPlatformClassName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(PlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static String getPlatformClassName() {
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "packt.taman.jfx8.ch4.android.AndroidPlatform";
            case "ios": return "packt.taman.jfx8.ch4.ios.IosPlatform";
            default : return "packt.taman.jfx8.ch4.desktop.DesktopPlatform";
        }
    }
    
    public static String getName() {
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "Android";
            case "ios": return "iOS";
            default : return "Desktop";
        }
    }
}
