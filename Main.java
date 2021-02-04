//  This will be the worst code you have ever seen. Sorry but I don't care.
import java.awt.*;
import javax.swing.*;

public class Main {
    private static Converter converter;
    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame();    
        frame.setSize(600,400);  
        frame.setLayout(null);

        // Create path selector
        JButton fileOpen = new JButton("Open level.dat");
        fileOpen.setBounds(50,100,50,100);
         
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

