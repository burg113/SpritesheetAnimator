package com.burg113;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimationWindow extends JFrame {
    JPanel controlPanel;
    JPanel contentPanel;

    JTextArea currIndexBox;
    JCheckBox runAutomaticallyBox;
    JTextField fpsField;
    JTextField scaleField;

    ArrayList<BufferedImage> images;
    int currImageIndex;

    float scale = 1;

    Update update = new Update();

    public AnimationWindow(ArrayList<BufferedImage> images) {
        // creates the JFrame
        super("animation");
        setSize(100, 100);

        this.images = images;

        // adds control panel at the top of the screen
        controlPanel = new JPanel();
        controlPanel.setMaximumSize(new Dimension(1000, 50));
        add(controlPanel, BorderLayout.NORTH);

        // adds content panel at the bottom of the screen
        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.SOUTH);

        // adds all the buttons to the control panel
        addButtons();

        // displays the first image
        displayImage(0);

        // configures program loop so that execute in Update is called on every frame
        ProgramLoop.frameRate = 6;
        ProgramLoop.executables.add(update);


        pack();
        setSize(getWidth() + 100, getHeight());
        setVisible(true);

        // after this window was closed the program loop should no longer call the update
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ProgramLoop.executables.remove(update);
            }
        });
    }

    // adds all the buttons to the control panel
    private void addButtons() {
        // adds the next button
        JButton nextButton = new JButton("next");
        nextButton.addActionListener(new NextButtonPressed());
        controlPanel.add(nextButton);

        // adds the prev button
        JButton prevButton = new JButton("prev");
        prevButton.addActionListener(new PrevButtonPressed());
        controlPanel.add(prevButton);

        // adds box that displays the current frame-index and total amount of frames
        currIndexBox = new JTextArea("");
        updateCurrIndexBox();
        controlPanel.add(currIndexBox);

        // adds the run settings
        controlPanel.add(new JTextArea("run settings:"));

        // run button
        runAutomaticallyBox = new JCheckBox("run");
        controlPanel.add(runAutomaticallyBox);

        // fps text and input-field
        controlPanel.add(new JTextArea("fps"));
        fpsField = new JTextField("6");
        fpsField.addActionListener(new FPSChanged());
        controlPanel.add(fpsField);

        // scale text and input-field
        scale = (float) 512 / images.get(0).getWidth();
        controlPanel.add(new JTextArea("scale"));
        scaleField = new JTextField("" + scale);
        scaleField.addActionListener(new ScaleChanged());
        controlPanel.add(scaleField);
    }

    // displays the image at the given index
    private void displayImage(int num) {
        System.out.println("now displaying: " + num);

        // updates the current index
        currImageIndex = num;
        updateCurrIndexBox();

        // updates the image
        contentPanel.removeAll();
        BufferedImage image = images.get(num);
        Image scaledImage = image.getScaledInstance((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), Image.SCALE_DEFAULT);
        contentPanel.add(new JLabel(new ImageIcon(scaledImage)));

        pack();
        setSize(getWidth() + 100, getHeight());
    }

    // displays the next image
    private void displayNextImage() {
        displayImage((currImageIndex + 1) % images.size());
    }

    // displays the previous image
    private void displayPrevImage() {
        displayImage((currImageIndex - 1 + images.size()) % images.size());
    }

    // updates the box containing the current frame-index as well as total amount of frames
    private void updateCurrIndexBox() {
        currIndexBox.setText(currImageIndex + " of " + images.size());
    }


    private class Update implements Executable {
        // called every frame of the program-loop
        @Override
        public void execute() {
            if (runAutomaticallyBox.isSelected())
                displayNextImage();
        }
    }


    // called on button presses:

    private class NextButtonPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayNextImage();
        }
    }

    private class PrevButtonPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayPrevImage();
        }
    }

    private class FPSChanged implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ProgramLoop.frameRate = Float.parseFloat(fpsField.getText());
            } catch (NumberFormatException e2) {
                System.out.println("The given scale is not a number!");
            }
            System.out.println("changed framerate: " + ProgramLoop.frameRate);
        }
    }

    private class ScaleChanged implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                scale = Float.parseFloat(scaleField.getText());
            } catch (NumberFormatException e2) {
                System.out.println("The given scale is not a number!");
            }
            displayImage(currImageIndex);
        }
    }

}
