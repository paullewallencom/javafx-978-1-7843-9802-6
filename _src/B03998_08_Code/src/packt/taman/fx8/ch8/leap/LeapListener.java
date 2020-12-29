package packt.taman.fx8.ch8.leap;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 8. Interactive Leap Motion applications with JavaFX
 *
 * @author Mohamed Taman, Jose Pereda
 */
public class LeapListener extends Listener {
    
    private final BooleanProperty doneList = new SimpleBooleanProperty(false);
    // Since we'll be listening to changes only in doneList, we don't need
    // bones collection to be observable too
    private final List<Bone> bones = new ArrayList<>();
    private final List<Arm> arms = new ArrayList<>();
    private final List<Pair> joints = new ArrayList<>();
    private final List<WritableImage> rawImages =new ArrayList<>();
    
     @Override
    public void onInit(Controller controller){
        // For privacy reasons, the user must also enable the feature in the 
        // Leap Motion control panel for any application to get the raw camera images.
        controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
    }
    
    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        
        doneList.set(false);
        rawImages.clear();
        // Check https://developer.leapmotion.com/documentation/java/devguide/Leap_Images.html
        ImageList images = frame.images();
        for(Image image : images){
            int width = (int)image.width();
            int height = (int)image.height();
            WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
            WritableImage wi=new WritableImage(width, height);
            PixelWriter pw = wi.getPixelWriter();
            //Get byte array containing the image data from Image object
            byte[] imageData = image.data();

            //Copy image data into display object
            int[] newPixels = new int[width * height];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    int r = (imageData[i*width+j] & 0xFF) << 16; //convert to unsigned and shift into place
                    int g = (imageData[i*width+j] & 0xFF) << 8;
                    int b = imageData[i*width+j] & 0xFF;
                    // flip image 
                    newPixels[i*width+(width-j-1)] = 1 - (r | g | b); 
                }
            }
            pw.setPixels(0, 0, width, height, pixelFormat, newPixels, 0, width);
            rawImages.add(wi);
        }
        
        bones.clear();
        arms.clear();
        joints.clear();
        if (!frame.hands().isEmpty()) {
            Screen screen = controller.locatedScreens().get(0);
            if (screen != null && screen.isValid()){
                for(Finger finger : frame.fingers()){
                    if(finger.isValid()){
                        for(Bone.Type b : Bone.Type.values()) {
                            if((!finger.type().equals(Finger.Type.TYPE_RING) && 
                                !finger.type().equals(Finger.Type.TYPE_MIDDLE)) || 
                                !b.equals(Bone.Type.TYPE_METACARPAL)){
                                bones.add(finger.bone(b));
                            }
                        }
                    }
                }
                for(Hand h: frame.hands()){
                    if(h.isValid()){
                        // arm
                        arms.add(h.arm());
                        
                        FingerList fingers = h.fingers();
                        Finger index=null, middle=null, ring=null, pinky=null;
                        for(Finger f: fingers){
                            if(f.isFinger() && f.isValid()){
                                switch(f.type()){
                                    case TYPE_INDEX: index=f; break;
                                    case TYPE_MIDDLE: middle=f; break;
                                    case TYPE_RING: ring=f; break;
                                    case TYPE_PINKY: pinky=f; break;
                                }
                            }
                        }
                        // joints
                        if(index!=null && middle!=null){
                            Pair p=new Pair(index.bone(Bone.Type.TYPE_METACARPAL).nextJoint(),
                                            middle.bone(Bone.Type.TYPE_METACARPAL).nextJoint());
                            joints.add(p);
                        }
                        if(middle!=null && ring!=null){
                            Pair p=new Pair(middle.bone(Bone.Type.TYPE_METACARPAL).nextJoint(),
                                            ring.bone(Bone.Type.TYPE_METACARPAL).nextJoint());
                            joints.add(p);
                        }
                        if(ring!=null && pinky!=null){
                            Pair p=new Pair(ring.bone(Bone.Type.TYPE_METACARPAL).nextJoint(),
                                            pinky.bone(Bone.Type.TYPE_METACARPAL).nextJoint());
                            joints.add(p);
                        }
                        if(index!=null && pinky!=null){
                            Pair p=new Pair(index.bone(Bone.Type.TYPE_METACARPAL).prevJoint(),
                                            pinky.bone(Bone.Type.TYPE_METACARPAL).prevJoint());
                            joints.add(p);
                        }        
                    }
                }
            }
        }
        
        doneList.set(!bones.isEmpty() || !arms.isEmpty());
    }
    
    public List<Bone> getBones(){ 
        // Returns a fresh copy of the bones collection 
        // to avoid concurrent exceptions iterating this list
        return bones.stream().collect(Collectors.toList());
    }
    public List<Arm> getArms(){ 
        // Returns a fresh copy of the arms collection 
        // to avoid concurrent exceptions iterating this list
        return arms.stream().collect(Collectors.toList());
    }
    public List<Pair> getJoints(){ 
        // Returns a fresh copy of the joints collection 
        // to avoid concurrent exceptions iterating this list
        return joints.stream().collect(Collectors.toList());
    }
    public List<WritableImage> getRawImages(){ 
        // Returns a fresh copy of the raw collection 
        // to avoid concurrent exceptions iterating this list
        return rawImages.stream().collect(Collectors.toList());
    }
    
    public BooleanProperty doneListProperty() { 
        return doneList; 
    }
    
}
