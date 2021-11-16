package com.burg113;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Program extends JFrame {
    JPanel controlPanel;
    JPanel contentPanel;

    BufferedImage image;

    JTextField horizontalInput;
    JTextField verticalInput;


    public Program(String name){
        // creates JFrame
        super(name);
        setSize(100,100);

        // adds control panel at the top of the screen
        controlPanel = new JPanel();
        controlPanel.setMaximumSize(new Dimension(1000,50));
        add(controlPanel,BorderLayout.NORTH);

        // adds content panel at the bottom of the screen
        contentPanel = new JPanel();
        add(contentPanel,BorderLayout.SOUTH);

        // adds all the buttons to the control panel
        addButtons();

        pack();
        setVisible(true);
    }

    // adds all the buttons to the button panel
    private void addButtons(){
        // adds the paste button
        JButton pasteButton = new JButton("Paste Clipboard");
        pasteButton.addActionListener(new PasteButtonClicked());
        controlPanel.add(pasteButton);

        // adding horizontal and vertical pixel input fields
        controlPanel.add(new JTextArea("horizontal pixels:"));
        horizontalInput = new JTextField("16");
        controlPanel.add(horizontalInput);
        controlPanel.add(new JTextArea("vertical pixels:"));
        verticalInput = new JTextField("16");
        controlPanel.add(verticalInput);

        // adding animate button
        JButton animateButton = new JButton("animate");
        animateButton.addActionListener(new AnimateButtonClicked());
        controlPanel.add(animateButton);

    }

    // loads an image from the clipboard and displays it
    public void loadImageFromClipboard(){
        try {
            image = (BufferedImage)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
            JLabel label=new JLabel(new ImageIcon(image));
            contentPanel.add(label);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        pack();
    }

    // opens the animation window with the current slice settings
    public void animate(){
        System.out.println("opening animation window");

        int spliceWidth = Integer.parseInt(verticalInput.getText());
        int spliceHeight = Integer.parseInt(verticalInput.getText());

        ArrayList<BufferedImage> images = new ArrayList<>();

        // gets all the sub-images
        for(int y=0;y<=image.getHeight()-spliceHeight;y+=spliceHeight){
            for(int x=0;x<=image.getWidth()-spliceWidth;x+=spliceWidth){
                images.add(image.getSubimage(x,y,spliceWidth,spliceHeight));
            }
        }

        // opens new animation window with the sliced images
        new AnimationWindow(images);

    }


    // called on button presses:

    private class PasteButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(contentPanel.getComponents().length>0)
                contentPanel.removeAll();
            loadImageFromClipboard();
        }
    }

    private class AnimateButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            animate();
        }
    }

}
