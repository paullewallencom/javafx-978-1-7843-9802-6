package packt.taman.fx8.ch8;

import packt.taman.fx8.ch8.leap.LeapListener;
import packt.taman.fx8.ch8.leap.Pair;
import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 8. Interactive Leap Motion applications with JavaFX
 *
 * @author Mohamed Taman, Jose Pereda
 */
public class LeapJavaFX extends Application {

    private LeapListener listener = null;
    private Controller controller = null;
    private final Group root = new Group();
    private ImageView rawView;

    @Override
    public void start(Stage primaryStage) {
        listener = new LeapListener();
        controller = new Controller();
        controller.addListener(listener);
        final PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(60);
        camera.getTransforms().addAll(new Translate(-320, -480, -100));

        final PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateZ(-500);
        root.getChildren().addAll(pointLight);

        rawView = new ImageView();
        rawView.setScaleY(2); // Leap image 640x240 with robust mode off 

        Group root3D = new Group();
        root3D.getChildren().addAll(camera, root);
        SubScene subScene = new SubScene(root3D, 640, 480, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        StackPane pane = new StackPane(rawView, subScene);
        Scene scene = new Scene(pane, 640, 480);

        final PhongMaterial materialFinger = new PhongMaterial(Color.BURLYWOOD);
        final PhongMaterial materialArm = new PhongMaterial(Color.CORNSILK);

        listener.doneListProperty().addListener((ov, b, b1) -> {
            if (b1) {
                // First, get a fresh copy of the bones, arms & joints collection
                List<Bone> bones = listener.getBones();
                List<Arm> arms = listener.getArms();
                List<Pair> joints = listener.getJoints();
                List<WritableImage> images = listener.getRawImages();

                Platform.runLater(() -> {
                    // Now, on the JavaFX thread
                    if (images.size() > 0) {
                        // left camera
                        rawView.setImage(images.get(0));
                    }
                    if (root.getChildren().size() > 1) {
                        // clean old bones
                        root.getChildren().remove(1, root.getChildren().size() - 1);
                    }
                    // Iterate over the list adding the bones to the scene
                    // If the collection changes there won't be any concurrent exception
                    // as we are iterating over its copy.
                    bones.stream()
                            .filter(bone -> bone.isValid() && bone.length() > 0)
                            .forEach(bone -> {
                                final Vector p = bone.center();
                                // create bone as a vertical cylinder and locate it at its center position
                                Cylinder c = new Cylinder(bone.width() / 2, bone.length());
                                c.setMaterial(materialFinger);

                                // translate and rotate the cylinder towards its direction
                                final Vector v = bone.direction();
                                Vector cross = (new Vector(v.getX(), -v.getY(), -v.getZ())).cross(new Vector(0, -1, 0));
                                double ang = (new Vector(v.getX(), -v.getY(), -v.getZ())).angleTo(new Vector(0, -1, 0));
                                c.getTransforms().addAll(
                                        new Translate(p.getX(), -p.getY(), -p.getZ()),
                                        new Rotate(-Math.toDegrees(ang),
                                                0, 0, 0,
                                                new Point3D(cross.getX(), -cross.getY(), cross.getZ()))
                                );

                                // add bone to scene
                                root.getChildren().add(c);

                                // add sphere at the end of the bone
                                Sphere s = new Sphere(bone.width() / 2f);
                                s.setMaterial(materialFinger);
                                s.getTransforms().addAll(
                                        new Translate(p.getX(), -p.getY() + bone.length() / 2d, -p.getZ()),
                                        new Rotate(-Math.toDegrees(ang),
                                                0, -bone.length() / 2d, 0,
                                                new Point3D(cross.getX(), -cross.getY(), cross.getZ())));

                                // add sphere to scene
                                root.getChildren().add(s);

                                // add sphere at the beginning of the bone
                                Sphere s2 = new Sphere(bone.width() / 2f);
                                s2.setMaterial(materialFinger);
                                s2.getTransforms().addAll(
                                        new Translate(p.getX(), -p.getY() - bone.length() / 2d, -p.getZ()),
                                        new Rotate(-Math.toDegrees(ang),
                                                0, bone.length() / 2d, 0,
                                                new Point3D(cross.getX(), -cross.getY(), cross.getZ())));

                                // add sphere to scene
                                root.getChildren().add(s2);
                            });

                    joints.stream()
                            .forEach(joint -> {
                                double length = joint.getV0().distanceTo(joint.getV1());
                                Cylinder c = new Cylinder(bones.get(0).width() / 3, length);
                                c.setMaterial(materialArm);

                                final Vector p = joint.getCenter();
                                final Vector v = joint.getDirection();
                                Vector cross = (new Vector(v.getX(), -v.getY(), -v.getZ())).cross(new Vector(0, -1, 0));
                                double ang = (new Vector(v.getX(), -v.getY(), -v.getZ())).angleTo(new Vector(0, -1, 0));
                                c.getTransforms().addAll(
                                        new Translate(p.getX(), -p.getY(), -p.getZ()),
                                        new Rotate(-Math.toDegrees(ang),
                                                0, 0, 0,
                                                new Point3D(cross.getX(), -cross.getY(), cross.getZ()))
                                );

                                // add joint to scene
                                root.getChildren().add(c);
                            });

                    arms.stream()
                            .filter(arm -> arm.isValid())
                            .forEach(arm -> {
                                final Vector p = arm.center();
                                // create arm as a cylinder and locate it at its center position
                                Cylinder c = new Cylinder(arm.width() / 2, arm.elbowPosition().minus(arm.wristPosition()).magnitude());
                                c.setMaterial(materialArm);

                                // rotate the cylinder towards its direction
                                final Vector v = arm.direction();
                                Vector cross = (new Vector(v.getX(), -v.getY(), -v.getZ())).cross(new Vector(0, -1, 0));
                                double ang = (new Vector(v.getX(), -v.getY(), -v.getZ())).angleTo(new Vector(0, -1, 0));
                                c.getTransforms().addAll(
                                        new Translate(p.getX(), -p.getY(), -p.getZ()),
                                        new Rotate(-Math.toDegrees(ang),
                                                0, 0, 0,
                                                new Point3D(cross.getX(), -cross.getY(), cross.getZ()))
                                );

                                // add arm to scene
                                root.getChildren().add(c);
                            });
                });
            }
        });

        primaryStage.setTitle("Skeletal Tracking with Leap Motion v2, Raw Images and JavaFX 3D");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    @Override
    public void stop() {
        controller.removeListener(listener);
    }

    
    public static void main(String[] args) {
        launch(args);
    }

}
