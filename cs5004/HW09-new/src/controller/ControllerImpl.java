package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import controller.commands.Command;
import controller.commands.io.Load;
import controller.commands.io.LoadPPM;
import controller.commands.io.LoadScript;
import controller.commands.io.SaveJPG;
import controller.commands.io.SavePNG;
import controller.commands.io.SavePPM;
import controller.commands.strategy.RunBlue;
import controller.commands.strategy.matrixstrategy.Blur;
import controller.commands.strategy.matrixstrategy.Filter;
import controller.commands.strategy.RunBrighten;
import controller.commands.strategy.RunGreen;
import controller.commands.strategy.matrixstrategy.GreyScaleBlue;
import controller.commands.strategy.matrixstrategy.GreyScaleGreen;
import controller.commands.strategy.matrixstrategy.GreyScaleIntensity;
import controller.commands.strategy.matrixstrategy.GreyScaleLuma;
import controller.commands.strategy.matrixstrategy.GreyScaleRed;
import controller.commands.strategy.RunIntensity;
import controller.commands.strategy.RunLuma;
import controller.commands.strategy.RunMax;
import controller.commands.strategy.RunRed;
import controller.commands.strategy.matrixstrategy.Sepia;
import controller.commands.strategy.matrixstrategy.Sharpen;
import model.ImageDatabase;
import view.View;

/**
 * This class implements the Controller interface with the processing method.
 */
public class ControllerImpl implements Controller {
  private Map<String, Command> command = new HashMap<>();
  private final ImageDatabase model;
  private final Readable in;
  private final View view;

  /**
   * Construct the controller by setting field values with a collection of available
   * commands to choose from, a collection of model coupled with IDs, a tool to
   * interact with user and a View to display error message.
   * @param modelMap collect models from use when running the model
   * @param input read inputs from the user
   * @param view a View object to render message when running the model
   */
  public ControllerImpl(ImageDatabase modelMap, Readable input, View view) {
    this.model = modelMap;
    this.in = input;
    this.view = view;
    command.put("value-component", new RunMax());
    command.put("luma-component", new RunLuma());
    command.put("intensity-component", new RunIntensity());
    command.put("red-component", new RunRed());
    command.put("green-component", new RunGreen());
    command.put("blue-component", new RunBlue());
    command.put("brighten", new RunBrighten());
    command.put("savePPM", new SavePPM()); // changed
    command.put("loadPPM", new LoadPPM());

    command.put("filter", new Filter());
    command.put("greyscale-red", new GreyScaleRed());
    command.put("greyscale-green", new GreyScaleGreen());
    command.put("greyscale-blue", new GreyScaleBlue());
    command.put("greyscale-luma", new GreyScaleLuma());
    command.put("greyscale-intensity", new GreyScaleIntensity());
    command.put("sepia", new Sepia());
    command.put("sharpen", new Sharpen());
    command.put("blur", new Blur());

    command.put("script", new LoadScript());
    command.put("loadJPG", new Load());
    command.put("loadPNG", new Load());
    command.put("saveJPG", new SaveJPG());
    command.put("savePNG", new SavePNG());
  }

  private void write(String message) {
    try {
      this.view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("write to view failed");
    }
  }

  @Override
  public void processing() {
    Scanner sc = new Scanner(this.in);
    Command command;
    while (sc.hasNext()) {
      String s = sc.next();
      if (s.contains("quit")) {
        write("user terminated");
        break;
      }
      if (s.contains("script")) {
        command = this.command.get("script");
        sc = new Scanner(s);
      } else {
        command = this.command.getOrDefault(s, null);
      }

      if (command == null) {
        write("wrong input" + System.lineSeparator());
        continue;
      }
      try {
        command.runCommand(sc, model);
      } catch (IllegalStateException e) {
        write(e.getMessage());
      }
    }
  }
}