package controller.commands;

import java.util.Scanner;
import model.ImageDatabase;

/**
 * This represents running a command through the controller
 * using a collection of commands available to the user.
 */
public interface Command {
  /**
   * Run one command and process the command with the model from
   * command instructions read from a scanner object.
   * @param sc the scanner to read input command data
   * @param model current model to run
   * @throws IllegalStateException if inputs are not invalid by given command settings
   */
  void runCommand(Scanner sc, ImageDatabase model) throws IllegalStateException;
}
