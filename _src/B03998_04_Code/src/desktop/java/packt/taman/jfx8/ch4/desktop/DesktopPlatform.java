package packt.taman.jfx8.ch4.desktop;

import packt.taman.jfx8.ch4.Platform;

/**
 *
 * @author jpereda
 */
public class DesktopPlatform implements Platform {
    
    @Override
    public void callNumber(String number) {
        System.out.println("Dialing "+number);
    }
    
}
