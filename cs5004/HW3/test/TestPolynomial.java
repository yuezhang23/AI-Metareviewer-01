import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Polynomial ADT implementation using specific format of strings.
 */
public class TestPolynomial {
  private Polynomial polyList;

  @Before
  public void setup() {
    polyList = new PolynomialImpl();
  }

  @Test
  public void testPolyImplConstructor1() {
    assertEquals("0", polyList.toString());
    assertEquals(0, polyList.getDegree());
    assertEquals(0, polyList.getCoefficient(0));
    assertEquals(0, polyList.evaluate(5), 0.01);

    polyList.addTerm(0, 0);
    assertEquals("0", polyList.toString());

    polyList.addTerm(2, 0);
    assertEquals("2", polyList.toString());
    assertEquals(0, polyList.getDegree());
    assertEquals(2, polyList.getCoefficient(0));
    assertEquals(2, polyList.evaluate(5), 0.01);

    polyList.removeTerm(0);
    assertEquals("0", polyList.toString());
  }

  @Test
  public void testPolyImplConstructor2() {
    Polynomial poly11 = new PolynomialImpl("-3x^4 +2x^5 -5 +11x^1");
    assertEquals("2x^5 -3x^4 +11x^1 -5", poly11.toString());

    Polynomial poly12 = new PolynomialImpl("5x^2 +4x^1 -2");
    assertEquals("5x^2 +4x^1 -2", poly12.toString());

    Polynomial poly13 = new PolynomialImpl("-33x^1 +2x^5 -5 +11x^1 +8");
    assertEquals("2x^5 -22x^1 +3", poly13.toString());

    Polynomial poly14 = new PolynomialImpl("8");
    assertEquals("8", poly14.toString());

    Polynomial poly16 = new PolynomialImpl("-8");
    assertEquals("-8", poly16.toString());

    Polynomial poly15 = new PolynomialImpl("0");
    assertEquals("0", poly15.toString());

  }

  @Test
  public void testAddPoly() {
    polyList.addTerm(22, 3);
    polyList.addTerm(-63, 5);
    polyList.addTerm(0, 2);
    polyList.addTerm(-15, 6);
    polyList.addTerm(18, 7);
    polyList.addTerm(11, 2);
    assertEquals("18x^7 -15x^6 -63x^5 +22x^3 +11x^2", polyList.toString());

    Polynomial poly2 = new PolynomialImpl();
    poly2.addTerm(-1, 5);
    poly2.addTerm(-15, 3);
    poly2.addTerm(0, 4);
    poly2.addTerm(-1,0 );
    poly2.addTerm(28, 3);
    poly2.addTerm(-8, 2);
    poly2.addTerm(-8, 0);
    Polynomial poly3 = poly2.add(polyList);
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -9", poly3.toString());
    assertEquals("-1x^5 +13x^3 -8x^2 -9", poly2.toString());
    assertEquals("18x^7 -15x^6 -63x^5 +22x^3 +11x^2", polyList.toString());

    Polynomial poly21 = new PolynomialImpl();
    poly21.addTerm(3,0);
    poly21.addTerm(-5, 5);
    poly21.addTerm(6, 5);
    Polynomial poly31 = poly2.add(poly21);
    assertEquals("13x^3 -8x^2 -6", poly31.toString());
    assertEquals("-1x^5 +13x^3 -8x^2 -9", poly2.toString());
    assertEquals("1x^5 +3", poly21.toString());

    Polynomial poly22 = new PolynomialImpl();
    poly22.addTerm(6, 0);
    poly22.addTerm(8, 2);
    poly22.addTerm(-13, 3);
    Polynomial poly32 = poly31.add(poly22);
    assertEquals("13x^3 -8x^2 -6", poly31.toString());
    assertEquals("-13x^3 +8x^2 +6", poly22.toString());
    assertEquals("0", poly32.toString());

    Polynomial poly4 = poly3.add(new PolynomialImpl());
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -9", poly4.toString());
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -9", poly3.toString());

    Polynomial poly5 = poly3.add(new PolynomialImpl("1"));
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -8", poly5.toString());
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -9", poly3.toString());

    Polynomial poly6 = poly3.add(new PolynomialImpl("2x^3 +3x^1"));
    assertEquals("18x^7 -15x^6 -64x^5 +37x^3 +3x^2 +3x^1 -9", poly6.toString());
    assertEquals("18x^7 -15x^6 -64x^5 +35x^3 +3x^2 -9", poly3.toString());
  }

  @Test
  public void testLinkedList() {
    polyList.addTerm(5, 0);
    assertEquals("5", polyList.toString());
    assertEquals(0, polyList.getDegree());
    assertEquals(5, polyList.getCoefficient(0));
    assertEquals(5, polyList.evaluate(5), 0.01);

    polyList.addTerm(4, 3);
    assertEquals("4x^3 +5", polyList.toString());
    assertEquals(3, polyList.getDegree());
    assertEquals(5, polyList.getCoefficient(0));
    assertEquals(4, polyList.getCoefficient(3));
    assertEquals(37, polyList.evaluate(2), 0.01);

    polyList.addTerm(-2, 1);
    assertEquals("4x^3 -2x^1 +5", polyList.toString());
    assertEquals(3, polyList.getDegree());
    assertEquals(5, polyList.getCoefficient(0));
    assertEquals(-2, polyList.getCoefficient(1));
    assertEquals(33, polyList.evaluate(2), 0.01);

    polyList.removeTerm(0);
    assertEquals("4x^3 -2x^1", polyList.toString());
    assertEquals(3, polyList.getDegree());
    assertEquals(0, polyList.getCoefficient(0));
    assertEquals(28, polyList.evaluate(2), 0.01);

    polyList.removeTerm(1);
    polyList.removeTerm(3);
    assertEquals("0", polyList.toString());
    assertEquals(0, polyList.getDegree());
    assertEquals(0, polyList.getCoefficient(0));
    assertEquals(0, polyList.evaluate(2), 0.01);

    polyList.addTerm(0, 11);
    polyList.addTerm(-51, 4);
    polyList.addTerm(22, 3);
    polyList.addTerm(-63, 5);
    polyList.addTerm(0, 2);
    polyList.addTerm(-15, 6);
    polyList.addTerm(18, 7);
    polyList.addTerm(11, 2);
    polyList.addTerm(10, 0);
    polyList.addTerm(10, 4);
    polyList.addTerm(-20, 3);
    polyList.addTerm(26, 8);
    assertEquals("26x^8 +18x^7 -15x^6 -63x^5 -41x^4 +2x^3 +11x^2 +10", polyList.toString());
    assertEquals(8, polyList.getDegree());
    assertEquals(26, polyList.getCoefficient(8));
    assertEquals(0, polyList.getCoefficient(-1));
    assertEquals(10, polyList.getCoefficient(0));
    assertEquals(-63, polyList.getCoefficient(5));
    assertEquals(10, polyList.evaluate(0), 0.01);
    assertEquals(-52, polyList.evaluate(1), 0.01);

    polyList.removeTerm(5);
    assertEquals("26x^8 +18x^7 -15x^6 -41x^4 +2x^3 +11x^2 +10", polyList.toString());
    polyList.removeTerm(0);
    assertEquals("26x^8 +18x^7 -15x^6 -41x^4 +2x^3 +11x^2", polyList.toString());
    polyList.removeTerm(1);
    assertEquals("26x^8 +18x^7 -15x^6 -41x^4 +2x^3 +11x^2", polyList.toString());
    polyList.removeTerm(-1);
    assertEquals("26x^8 +18x^7 -15x^6 -41x^4 +2x^3 +11x^2", polyList.toString());

    assertEquals(8, polyList.getDegree());
    assertEquals(26, polyList.getCoefficient(8));
    assertEquals(0, polyList.getCoefficient(0));
    assertEquals(-41, polyList.getCoefficient(4));
    assertEquals(0, polyList.evaluate(0), 0.01);
    assertEquals(1, polyList.evaluate(1), 0.01);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPow() {
    polyList.addTerm(-5, -2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly1() {
    Polynomial poly0 = new PolynomialImpl("5x^-2 +3x^3 +4x^-1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly2() {
    Polynomial poly0 = new PolynomialImpl("1x^2 -2x^0 +4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly3() {
    Polynomial poly0 = new PolynomialImpl("+3x^2 -3x^3 +4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly4() {
    Polynomial poly0 = new PolynomialImpl("1x^2 3x^3 -4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly5() {
    Polynomial poly0 = new PolynomialImpl("1x^2 0x^3 -4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly6() {
    Polynomial poly0 = new PolynomialImpl("");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly7() {
    Polynomial poly0 = new PolynomialImpl("5.1x^2 +3x^3 +4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly8() {
    Polynomial poly0 = new PolynomialImpl("5x^2 +3x^3.1 +4x^1 -2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPoly9() {
    Polynomial poly0 = new PolynomialImpl("5x^2 +3x^3 +4x^1 -2.2");
  }

}