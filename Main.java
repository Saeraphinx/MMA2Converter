//  This will be the worst code you have ever seen. Sorry but I don't care.
import java.awt.*;
import javax.swing.*;

public class Main {
    private static Converter converter;
    private static JFrame f;
    public static void main(String[] args) {
        f = new JFrame();    
        f.setSize(600,400);  
        f.setLayout(null);

        // Create path / settings panel
        JTextField t1,t2;  
        t1 = new JTextField("Input");  
        t1.setBounds(50,100, 480,30);  
        t2 = new JTextField("Output *Optional*");  
        t2.setBounds(50,150, 480,30);  
        f.add(t1); f.add(t2);

        f.setVisible(true);

    }
}

