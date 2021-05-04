import java.awt.*;
import javax.swing.*;
import java.io.*; 

//  This will be the worst code you have ever seen. Sorry but I don't care.
public class Main {
    private static Converter converter;
    private static Window frame;
    public static void main(String[] args) {
        frame = new Window(converter);
        frame.setupFrame();
    }
}

