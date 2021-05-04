import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
import java.util.*;

// Just yoinked the code from my ScuffedWalls Project
public class Window implements ActionListener {
    private static JFrame frame;
    private JTextField inField;
    private JButton checkButton, loadButton, backupButton, outputButton, checkLevelButton, convertButton;
    private JTextArea log;
    private JScrollPane logScrollPane;
    private Converter converter;
    private boolean loadFlag = false;
    
    public Window(Converter converter) {
        this.converter = converter;
        frame = new JFrame("MMA2 360 Converter");    
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void setupFrame() {
        setupFrameFileSelect();
        setupFrameLog();
        setupFrameButtons();

        frame.getContentPane().setBackground(new Color(100,100,100));
        frame.setLayout(null); 
        frame.setVisible(true);
    }

    /**
     * Setup Menu Bar
     */
    private void setupFrameFileSelect() {
        inField = new JTextField(" Full File Path");
        inField.setBounds(5,5,480,25); 
        inField.setBackground(new Color(50,50,50));
        inField.setForeground(Color.WHITE);

        frame.add(inField);
    }

    private void setupFrameLog() {
        log = new JTextArea();
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        logScrollPane = new JScrollPane(log);
        logScrollPane.setBounds(5, 35, 480, 320);
        log.setBackground(new Color(50,50,50));
        log.setForeground(Color.WHITE);

        frame.add(logScrollPane);
    }

    private void setupFrameButtons() {
        checkButton = new JButton("Check");
        checkButton.setBounds(490, 5, 90, 25);
        checkButton.setBackground(new Color(50,50,50));
        checkButton.setForeground(Color.WHITE);

        loadButton = new JButton("Load");
        loadButton.setBounds(490, 35, 90, 25);
        loadButton.setBackground(new Color(50,50,50));
        loadButton.setForeground(Color.WHITE);

        backupButton = new JButton("Backup");
        backupButton.setBounds(490, 300, 90, 25);
        backupButton.setBackground(new Color(50,50,50));
        backupButton.setForeground(Color.WHITE);

        
        convertButton = new JButton("Convert");
        convertButton.setBounds(490, 170, 90, 25);
        convertButton.setBackground(new Color(50,50,50));
        convertButton.setForeground(Color.ORANGE);

        outputButton = new JButton("Output");
        outputButton.setBounds(490, 330, 90, 25);
        outputButton.setBackground(new Color(50,50,50));
        outputButton.setForeground(Color.WHITE);


        checkLevelButton = new JButton("Check lvl");
        checkLevelButton.setBounds(490, 140, 90, 25);
        checkLevelButton.setBackground(new Color(50,50,50));
        checkLevelButton.setForeground(Color.GREEN);

        loadButton.setEnabled(false); convertButton.setEnabled(false);
        backupButton.setEnabled(false); checkLevelButton.setEnabled(false);
        outputButton.setEnabled(false);
        loadButton.addActionListener(this); convertButton.addActionListener(this);
        checkButton.addActionListener(this); backupButton.addActionListener(this);
        outputButton.addActionListener(this); checkLevelButton.addActionListener(this);


        frame.add(checkButton); frame.add(loadButton); 
        frame.add(convertButton); frame.add(backupButton);
        frame.add(checkLevelButton); frame.add(outputButton);
    }

    /*
     *   *****************
     *   ** After-Setup **
     *   *****************
     */  

    public void actionPerformed(ActionEvent e) {
        // CHECK
        if (e.getSource() == checkButton) {
            try {
                File test = new File(inField.getText());
                if (test.exists() && test.isFile()) { // && test.getPath().indexOf(".dat") != -1 && true
                    inField.setEditable(false);
                    checkButton.setEnabled(false);
                    loadButton.setEnabled(true);
                    log.append("Valid File Found!\n");
                } else {
                    log.append("ERROR: INVALID FILE PATH\n");
                    log.append("\n");
                    log.append("Please input a valid file. (e.g. ExpertStandard.dat\n");
                    checkButton.setForeground(Color.RED);
                }
            } catch (Exception err) {
                log.append("ERROR: " + err);
                log.append(" \n");
                log.append("Please input a valid file.\n");
                checkButton.setForeground(Color.RED);
            }

        } else if (e.getSource() == loadButton) {
            converter = new Converter(inField.getText());
            log.append("File Loaded!");
            checkLevelButton.setEnabled(true); backupButton.setEnabled(true); 
            convertButton.setEnabled(true); loadButton.setEnabled(false);
        } else if (e.getSource() == checkLevelButton) {
            converter.checkEvents(log);
            convertButton.setForeground(Color.GREEN);
            loadFlag = true;
        } else if (e.getSource() == convertButton) {
            convertButton.setEnabled(false);
            if (loadFlag) {
                converter.replaceEvents(log);
            } else {
                converter.checkEvents(log);
                converter.replaceEvents(log);
            }
            checkLevelButton.setEnabled(false); convertButton.setEnabled(false);
            outputButton.setEnabled(true);
        } else if (e.getSource() == backupButton) {
            converter.backupLevelData(log);
        } else if (e.getSource() == outputButton) {
            converter.outputLevelData(log);
        }
    }
}