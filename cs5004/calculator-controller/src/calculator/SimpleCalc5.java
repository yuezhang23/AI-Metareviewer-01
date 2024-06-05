package calculator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * Demonstrates a simple command-line-based calculator
 */
public class SimpleCalc5 {
  public static void main(String[] args) {
    try {
      new Controller5(new InputStreamReader(System.in), System.out).go(new Calculator());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

/**
 * A controller for the calculator. The controller receives all its inputs
 * from a Readable object and transmits all outputs to an Appendable
 * object. The Appendable object would be provided by a view (not shown in
 * this example). This design allows us to test. See TestController5.
 */
class Controller5 implements CalcController {
  final Readable in;
  final Appendable out;
  Controller5(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }
  public void go(Calculator calc) throws IOException {
    Objects.requireNonNull(calc);
    int num1, num2;
    Scanner scan = new Scanner(this.in);
    num1 = scan.nextInt();
    num2 = scan.nextInt();
    this.out.append(String.format("%d\n", calc.add(num1, num2)));
  }
}

