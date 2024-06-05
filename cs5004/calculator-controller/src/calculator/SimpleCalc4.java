package calculator;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Demonstrates a simple command-line-based calculator. The calculator is
 * factored out into a model and controller.
 */
public class SimpleCalc4 {
  public static void main(String[] args) {
    new Controller4(System.in, System.out).go(new Calculator());
  }
}

/**
 * A controller for the calculator. The controller receives all its inputs
 * from an InputStream object and transmits all outputs to a PrintStream
 * object. The PrintStream object would be provided by a view (not shown in
 * this example). This design allows us to test. See TestController4.
 */
class Controller4 implements CalcController {
  final InputStream in;
  final PrintStream out;
  Controller4(InputStream in, PrintStream out) {
    this.in = in;
    this.out = out;
  }
  public void go(Calculator calc) {
    Objects.requireNonNull(calc);
    int num1, num2;
    Scanner scan = new Scanner(this.in);
    num1 = scan.nextInt();
    num2 = scan.nextInt();
    this.out.printf("%d", calc.add(num1, num2));
  }
}

