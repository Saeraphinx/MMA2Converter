import java.awt.event.*;
import javax.swing.*;
import java.io.*; 

//  This will be the worst code you have ever seen. Sorry but I don't care.
public class Main {
    private static Converter converter;
    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame();    
        frame.setSize(600,400);  
        frame.setLayout(null);

        // Create path selector
        JButton fileRead = new JButton("Open level.dat");
        JButton fileOut = new JButton("Set Output");
        fileRead.setBounds(5,5,120,20);
        fileOut.setBounds(130,5,120,20);

        fileRead.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {      
                String name = JOptionPane.showInputDialog(frame,"Enter Name");  
            }  
        });  


        frame.add(fileRead);
        frame.add(fileOut);
         
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

