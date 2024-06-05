
public class Main {
  public static void main(String[] args) {
    Polynomial poly1 = new PolynomialImpl();
    poly1.addTerm(2,6);
    poly1.addTerm(4,4);
    poly1.addTerm(-5,1);
    poly1.addTerm(1,1);
    poly1.addTerm(3,3);
    poly1.addTerm(-3,3);
    poly1.addTerm(10,0);
    System.out.println(poly1);

    Polynomial poly2 = new PolynomialImpl(poly1.toString());

    poly2.addTerm(2,6);
    poly2.addTerm(0,1);
    poly2.addTerm(-6,4);
    poly2.addTerm(-6,3);
    poly2.addTerm(8, 0);
    poly2.addTerm(-1,5);
    System.out.println(poly2);
    System.out.printf("degree is %d\n",poly2.getDegree());
    System.out.printf("Coe at power 4 is %d\n",poly2.getCoefficient(4));

    poly2.removeTerm(4);
    poly2.removeTerm(0);
    System.out.println(poly2);

    System.out.println(poly2.evaluate(2));

    Polynomial poly3 = poly2.add(poly1);

    System.out.println(poly1);
    System.out.println(poly2);
    System.out.println(poly3);




    // "", "5", "5x^-2", "+5x^2","-5x^-8"
    Polynomial poly5 = new PolynomialImpl();
    System.out.println(poly5);
    System.out.printf("degree is %d\n",poly5.getDegree());
    System.out.printf("Coe at power 4 is %d\n",poly5.getCoefficient(4));











  }
}