package packt.taman.jfx8.ch2;

import static java.lang.System.out;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 2. Creating custom UI and some essentials
 * @author Mohamed Taman
 */
public class LambdaTest {
 
    public static void main(String[] args) {
        
        Calculator area = (width, height) -> width * height; //Area = w × h
        
        Calculator perimeter = (width, height) -> 2 * (height + width); //Perimeter = 2(w+h)
        
        out.println("Rectangle area: "+ area.calculate(4, 5)+" cm.");
        
        out.println("Rectangle perimeter: "+ perimeter.calculate(4, 5)+" cm.");
        
    }
    
}
