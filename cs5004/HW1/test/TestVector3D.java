import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the Vector3D class.
 */
public class TestVector3D {
  private Vector3D vector1;
  private Vector3D vector2;
  private Vector3D vector3;
  private Vector3D vector4;
  private Vector3D vector5;
  private Vector3D vector6;
  private Vector3D vector7;

  @Before
  public void setUp() {
    this.vector1 = new Vector3D(5.235, 6.22, 12.344);
    this.vector2 = new Vector3D(5, 6.1, 2.01);
    this.vector3 = new Vector3D(3, 4, 12);
    this.vector4 = new Vector3D(3, 4, 5);
    this.vector5 = new Vector3D(0, 0, 0);
    this.vector6 = new Vector3D(-3, -4, 5);
    this.vector7 = new Vector3D(-2, -5, 0);
  }

  @Test
  public void testGet() {
    Random r = new Random();
    for (int i = 0; i < 10000; i ++) {
      double x = Math.abs(r.nextDouble());
      double y = Math.abs(r.nextDouble());
      double z = Math.abs(r.nextDouble());

      // x +ve, y +ve, z +ve.
      Vector3D vectorP = new Vector3D(x, y, z);
      assertEquals(x, vectorP.getX(), 0.001);
      assertEquals(y, vectorP.getY(), 0.001);
      assertEquals(z, vectorP.getZ(), 0.001);

      int signX = r.nextInt(2) - 1;
      int signY = r.nextInt(2) - 1;
      int signZ = r.nextInt(2) - 1;

      // x -ve or 0, y -ve or 0, z -ve or 0.
      Vector3D vectorN = new Vector3D(signX * x, signY * y, signZ * z);
      assertEquals(signX * x, vectorN.getX(), 0.001);
      assertEquals(signY * y, vectorN.getY(), 0.001);
      assertEquals(signZ * z, vectorN.getZ(), 0.001);
    }
  }

  @Test
  public void testToString() {
    // +ve, shorten decimal places by format.
    assertEquals("(5.24,6.22,12.34)", vector1.toString());

    // +ve, extend decimal places by format.
    assertEquals("(5.00,6.10,2.01)", vector2.toString());

    // -ve or 0, extend decimal places by format.
    assertEquals("(-2.00,-5.00,0.00)", vector7.toString());
  }

  @Test
  public void testGetMagnitude() {
    // magnitude is positive.
    assertEquals(7.07, Math.round(vector4.getMagnitude() * 100) / 100.0, 0.001);

    // magnitude is 0.
    assertEquals(0, Math.round(vector5.getMagnitude() * 100) / 100.0, 0.001);

    // +ve or -ve, same absolute field value of two vectors have the same magnitude.
    assertEquals(vector4.getMagnitude(), vector6.getMagnitude(), 0.001);

    // field values in different directions of two vectors have the same magnitude.
    assertEquals(vector4.getMagnitude(), new Vector3D(5, 3, 4).getMagnitude(), 0.001);
  }

  @Test
  public void testValidNormalize() {
    // x +ve, y +ve, z +ve.
    Vector3D unitVector3 = vector3.normalize();
    assertEquals(0.231, unitVector3.getX(), 0.001);
    assertEquals(0.308, unitVector3.getY(), 0.001);
    assertEquals(0.923, unitVector3.getZ(), 0.001);

    // x -ve, y -ve, z +ve.
    Vector3D unitVector6 = vector6.normalize();
    assertEquals(-0.424, unitVector6.getX(), 0.001);
    assertEquals(-0.566, unitVector6.getY(), 0.001);
    assertEquals(0.707, unitVector6.getZ(), 0.001);

    // x -ve, y -ve, z 0.
    Vector3D unitVector7 = vector7.normalize();
    assertEquals(-0.371, unitVector7.getX(), 0.001);
    assertEquals(-0.928, unitVector7.getY(), 0.001);
    assertEquals(0.000, unitVector7.getZ(), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNormalize() {
    // vector5 is (0, 0, 0).
    vector5.normalize();
  }

  @Test
  public void testAdd() {
    // all fields +ve.
    assertEquals(10.235, vector1.add(vector2).getX(), 0.001);
    assertEquals(12.32, vector1.add(vector2).getY(), 0.001);
    assertEquals(14.354, vector1.add(vector2).getZ(), 0.001);

    // vector5 is (0, 0, 0).
    assertEquals(vector3.add(vector5).toString(), vector3.toString());

    // some fields +ve, some -ve.
    assertEquals(2, vector2.add(vector6).getX(), 0.001);
    assertEquals(2.1, vector2.add(vector6).getY(), 0.001);
    assertEquals(7.01, vector2.add(vector6).getZ(), 0.001);
  }

  @Test
  public void testMultiply() {
    // constant +ve.
    assertEquals(15, vector3.multiply(5).getX(), 0.001);
    assertEquals(20, vector3.multiply(5).getY(), 0.001);
    assertEquals(60, vector3.multiply(5).getZ(), 0.001);

    // constant 0.
    assertEquals(0, vector3.multiply(0).getX(), 0.001);
    assertEquals(0, vector3.multiply(0).getY(), 0.001);
    assertEquals(0, vector3.multiply(0).getZ(), 0.001);

    // constant -ve.
    assertEquals(-15, vector3.multiply(-5).getX(), 0.001);
    assertEquals(-20, vector3.multiply(-5).getY(), 0.001);
    assertEquals(-60, vector3.multiply(-5).getZ(), 0.001);
  }

  @Test
  public void testDotProduct() {
    // one of two vectors is (0, 0, 0) and dot.product is 0.
    assertEquals(0, vector4.dotProduct(vector5), 0.001);

    // neither of two vectors is (0, 0, 0) and dot.product is 0.
    assertEquals(0, vector4.dotProduct(vector6), 0.001);

    // dot.product is negative.
    assertEquals(-26, vector4.dotProduct(vector7), 0.001);

    // dot.product is positive.
    assertEquals(85, vector4.dotProduct(vector3), 0.001);
  }

  @Test
  public void testValidCosine() {
    // dot.product is positive.
    assertEquals(22.38, vector4.angleBetween(vector3), 0.01);

    // dot.product is negative.
    assertEquals(46.94, vector4.angleBetween(vector7), 0.01);

    // neither of two vectors is (0, 0, 0) and dot.product is 0.
    assertEquals(90, vector4.angleBetween(vector6), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCosine() {
    // vector 5 is (0, 0, 0).
    vector4.angleBetween(vector5);
  }
}
