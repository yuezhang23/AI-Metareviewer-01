package cs3500.turtle.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

import cs3500.turtle.model.Position2D;
import cs3500.turtle.tracingmodel.Line;

/**
 * This is an implementation of the IView interface
 * that uses Java Swing to draw the results of the
 * turtle. It shows any error messages using a
 * pop-up dialog box, and shows the turtle position
 * and heading
 */
public class TurtleGraphicsView extends JFrame implements IView {
  private JButton commandButton, quitButton;
  private JPanel buttonPanel;
  private JPanel turtlePanel;
  private JScrollPane scrollPane;
  private JTextField input;
  Consumer<String> commandCallback;

  public TurtleGraphicsView() {
    super();
    this.setTitle("Turtles!");
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //use a borderlayout with drawing panel in center and button panel in south
    this.setLayout(new BorderLayout());
    turtlePanel = new JPanel();
    turtlePanel.setPreferredSize(new Dimension(500, 500));
    scrollPane = new JScrollPane(turtlePanel);
    this.add(scrollPane, BorderLayout.CENTER);

    //button panel
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.SOUTH);

    //input textfield
    input = new JTextField(15);
    buttonPanel.add(input);

    //buttons
    commandButton = new JButton("Execute");
    commandButton.addActionListener((ActionEvent e) ->
    {
      if (commandCallback != null) { //if there is a command callback
        commandCallback.accept(input.getText()); //send command to be processed
        input.setText(""); //clear the input text field
      }
    });
    buttonPanel.add(commandButton);

    //quit button
    quitButton = new JButton("Quit");
    quitButton.addActionListener((ActionEvent e) -> {
      System.exit(0);
    });
    buttonPanel.add(quitButton);

    commandCallback = null;

    this.pack();


  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }


  @Override
  public void setCommandCallback(Consumer<String> callback) {
    commandCallback = callback;
  }


  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);

  }

}
