package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ViewGUI extends JFrame implements ActionListener,
        ListSelectionListener, KeyListener {

  private JPanel mainPanel;
  private JPanel idPanel;
  private JPanel loadPanel;
  private JPanel savePanel;
  private JPanel comboboxPanel;
  private JPanel messageDialogPanel;
  private JTextArea textArea;

  private JPanel canvas;
  private JScrollPane mainScrollPane;
  private Map<String, BufferedImage> bufferMap;
  private JComboBox<String> comboboxID;
  private JComboBox<String> comboID0;
  private JComboBox<String> comboID1;
  private JComboBox<String> comboboxTransform;

  private JLabel[] imageLabel;
  private JLabel fileOpenDisplay;
  private JLabel fileSaveDisplay;
  private JLabel renderDisplay;
  private JLabel colorTransform;
  private JLabel renderID;

  private List<ViewListener> subscribers = new ArrayList<>();

  //  1. button release
  //  2. combobox length do not change
  //  3. error message
  //  4. anymore to put to canvas or controller

  public ViewGUI() {
    // main panel
    super();
    setTitle("Image Processing");
    setSize(800, 500);
    mainPanel = new JPanel();
    idPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);
    this.bufferMap = new HashMap<>();
    this.comboboxID = new JComboBox<String>();
    this.comboID0 = new JComboBox<String>();
    this.comboID1 = new JComboBox<String>();
    this.comboboxTransform = new JComboBox<String>();

    // canvas
    canvas = new JPanel();
    canvas.setBorder(BorderFactory.createTitledBorder("image view"));
    canvas.setLayout(new GridLayout(1, 0, 10, 10));
    mainPanel.add(canvas);
    imageLabel = new JLabel[2];
    JScrollPane[] imageScrollPane = new JScrollPane[2];
    for (int i = 0; i < imageLabel.length; i++) {
      imageLabel[i] = new JLabel();
      imageScrollPane[i] = new JScrollPane(imageLabel[i]);
      imageScrollPane[i].setBackground(Color.gray);
      imageScrollPane[i].setPreferredSize(new Dimension(200, 400));
      canvas.add(imageScrollPane[i]);
    }
    canvas.setVisible(false);

    // ID
    idPanel.setLayout(new GridLayout(1, 0, 10, 10));
    JPanel[] comboPanel = new JPanel[2];
    mainPanel.add(idPanel);
    comboPanel[0] = new JPanel();
    comboPanel[0].setBorder(BorderFactory.createTitledBorder("Left Image"));
    comboID0.addItem("** select **");
    comboPanel[0].add(comboID0);
    comboPanel[1] = new JPanel();
    comboPanel[1].setBorder(BorderFactory.createTitledBorder("Right Image"));
    comboID1.addItem("** select **");
    comboPanel[1].add(comboID1);
    for (int i = 0; i < 2; i++) {
      comboPanel[i].setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
      idPanel.add(comboPanel[i]);
    }

    // dialog boxes
    JPanel dialogBoxesPanel = new JPanel();
    dialogBoxesPanel.setBorder(BorderFactory.createTitledBorder("Dialog boxes"));
    dialogBoxesPanel.setLayout(new BoxLayout(dialogBoxesPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(dialogBoxesPanel);

    // Load
    loadPanel = new JPanel();
    loadPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    dialogBoxesPanel.add(loadPanel);
    JButton fileOpenButton = new JButton("Load an image");
    fileOpenButton.setActionCommand("Load file");
    fileOpenButton.addActionListener(this);
    loadPanel.add(fileOpenButton);
    fileOpenDisplay = new JLabel("File path will appear here");
    loadPanel.add(fileOpenDisplay);

    // save
    savePanel = new JPanel();
    savePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    dialogBoxesPanel.add(savePanel);
    JButton fileSaveButton = new JButton("Save < Right Image >");
    fileSaveButton.setActionCommand("Save file");
    fileSaveButton.addActionListener(this);
    savePanel.add(fileSaveButton);
    fileSaveDisplay = new JLabel("File path will appear here");
    savePanel.add(fileSaveDisplay);

    // Render Options
    comboboxPanel = new JPanel();
    comboboxPanel.setBorder(BorderFactory.createTitledBorder("Render Options"));
    comboboxPanel.setLayout(new BoxLayout(comboboxPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(comboboxPanel);

    // render message
    messageDialogPanel = new JPanel();
    messageDialogPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    comboboxPanel.add(messageDialogPanel);
    JButton messageButton = new JButton("Render an image");
    messageButton.setActionCommand("Render");
    messageButton.addActionListener(this);
    messageDialogPanel.add(messageButton);
    renderDisplay = new JLabel("Render information will appear here");
    messageDialogPanel.add(renderDisplay);

    // ID
    JPanel idPanel = new JPanel();
    idPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    comboboxPanel.add(idPanel);
    comboboxID.addItem("** select **");
    idPanel.add(comboboxID);
    renderID = new JLabel("Render ID");
    idPanel.add(renderID);

    // color transform/filter
    JPanel transPanel = new JPanel();
    transPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    comboboxPanel.add(transPanel);
    String[] options = {"** select **", "Brighten", "Sepia", "GreyScale-Red", "GreyScale-Green", "GreyScale-Blue",
            "GreyScale-Luma", "GreyScale-Intensity", "GreyScale-Value", "Blur", "Sharpen"};
    for (String option : options) {
      comboboxTransform.addItem(option);
    }
    transPanel.add(comboboxTransform);
    colorTransform = new JLabel("Color Transform / Filtering");
    transPanel.add(colorTransform);

    // brighten
    JPanel brPanel = new JPanel();
    brPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
    comboboxPanel.add(brPanel);
    textArea = new JTextArea(2, 12);
    textArea.setBorder(BorderFactory.createTitledBorder("Brighten Increment"));
    brPanel.add(textArea);
    textArea.setEnabled(false);

    comboboxID.setActionCommand("Option");
    comboboxID.addActionListener(this);
    comboID0.setActionCommand("Left View");
    comboID0.addActionListener(this);
    comboID1.setActionCommand("Right View");
    comboID1.addActionListener(this);
    comboboxTransform.setActionCommand("Option");
    comboboxTransform.addActionListener(this);

  }

  public String actLoad() {
    final JFileChooser choice = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Images", "jpg", "jpeg", "png", "ppm");
    choice.setFileFilter(filter);
    int suffix = choice.showOpenDialog(ViewGUI.this);
    if (suffix == JFileChooser.APPROVE_OPTION) {
      File f = choice.getSelectedFile();
      fileOpenDisplay.setText("Loaded: " + f.getName());
      return f.getName() + "  " + f.getAbsolutePath();
    } else {
      displayErrorMessage("wrong file to open");
      return "";
    }
  }

  private void displayErrorMessage(String message) {

  }

  public String actSave() {
    final JFileChooser choice = new JFileChooser(".");
    int suffix = choice.showSaveDialog(ViewGUI.this);
    if (suffix == JFileChooser.APPROVE_OPTION) {
      File f = choice.getSelectedFile();
      fileSaveDisplay.setText("Saved: " + f.getName());
      return comboID1.getSelectedItem() + "   " + f.getAbsolutePath();
    } else {
      displayErrorMessage("wrong format to save");
      return "";
    }
  }

  public void addViewListener(ViewListener subscriber) {
    this.subscribers.add(subscriber);
  }

  public void imageRefresh(BufferedImage image, String id) {
    if (!bufferMap.containsKey(id)) {
      comboboxID.addItem(id);
      comboID0.addItem(id);
      comboID1.addItem(id);
    }
    bufferMap.put(id, image);
    imageLabel[0].setIcon(new ImageIcon(bufferMap.get(id.split("-")[0])));
    imageLabel[1].setIcon(new ImageIcon(image));
    canvas.setVisible(true);
    comboID0.setSelectedItem(id.split("-")[0]);
    comboID1.setSelectedItem(id);
  }

  // make public
  private void emitLoadEvent() {
    for (ViewListener listener : subscribers) {
      listener.handleLoadEvent();
    }
  }

  private void emitSaveEvent() {
    for (ViewListener listener : subscribers) {
      listener.handleSaveEvent();
    }
  }

  public void emitRenderEvent(String event) {
    if (event.split(" ").length < 2 || event.split(" ")[0].equals("")) {
      displayErrorMessage("Render Options should all be selected");
    } else {
      for (ViewListener listener : subscribers) {
        listener.handleRender(event);
      }
    }
  }

  public void emitBrightenEvent(String event) {
    String[] info = event.split(" ");
    if (info.length < 3 || info[0].equals("")) {
      displayErrorMessage("Render Options should all be selected");
    }
    else {
      try {
        int incr = Integer.parseInt(info[2]);
        for (ViewListener listener : subscribers) {
          listener.handleBrighten(event);
        }
      } catch (NumberFormatException e) {
        displayErrorMessage("brighten increment should be in integer");
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    switch (arg0.getActionCommand()) {
      case "Load file": {
        emitLoadEvent();
      }
      break;

      case "Save file": {
        emitSaveEvent();
      }
      break;

      case "Option": {
        renderID.setText("ID Selected: " + comboboxID.getSelectedItem());
        colorTransform.setText("Render selected: " + comboboxTransform.getSelectedItem());
        if (comboboxTransform.getSelectedItem().equals("Brighten")) {
          textArea.setEnabled(true);
        } else {
          textArea.setText("");
          textArea.setEnabled(false);
        }
      }
      break;
      case "Render": {
        String info = comboboxID.getSelectedItem()
                + " " + comboboxTransform.getSelectedItem();
        if (comboboxTransform.getSelectedItem().equals("Brighten")) {
          info += " " + textArea.getText();
          emitBrightenEvent(info.replace("** select **", ""));
        } else {
          emitRenderEvent(info.replace("** select **", ""));
        }
        renderDisplay.setText("Done: " + info.replace("** select **", ""));
      }
      break;

      case "Left View": {
        String in = comboID0.getSelectedItem().toString();
        imageLabel[0].setIcon(new ImageIcon(bufferMap.get(in)));
      }
      break;

      case "Right View": {
        String in = comboID1.getSelectedItem().toString();
        imageLabel[1].setIcon(new ImageIcon(bufferMap.get(in)));
      }
      break;
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

}
