package controller.commands.strategy.matrixstrategy;

import controller.commands.Command;
import model.ImageState;

public interface ColorChange extends Command {

  ImageState colorChange(Kernel matrix, ImageState image);
}
