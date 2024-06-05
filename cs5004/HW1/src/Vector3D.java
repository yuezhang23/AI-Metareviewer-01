/**
 * This class represents a vector in 3D.
 * The vector has three components, namely a value in x-direction,
 * a value in y-direction and a value in z-direction.
 */
public class Vector3D {
  private final double x;
  private final double y;
  private final double z;

  /**
   * Constructs a Vector3D object by initializing instance
   * variables of x, y and z.
   * @param x the value of this vector in x direction
   * @param y the value of this vector in y direction
   * @param z the value of this vector in z direction
   */
  public Vector3D(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Get the x of this vector3D.
   * @return the value of this vector in x direction
   */
  public double getX() {
    return x;
  }

  /**
   * Get the y of this vector3D.
   * @return the value of this vector in y direction
   */
  public double getY() {
    return y;
  }

  /**
   * Get the z of this vector3D.
   * @return the value of this vector in z direction
   */
  public double getZ() {
    return z;
  }

  /**
   * Return the specific string format of this vector.
   * @return the string format of this vector.
   */
  public String toString() {
    return String.format("(" + "%.2f,%.2f,%.2f" + ")",
            this.getX(), this.getY(), this.getZ());
  }

  /**
   * Return the length of the vector by mathematical definition.
   * @return the length of the vector
   */
  public double getMagnitude() {
    double magnitudePower = Math.pow(this.getX(), 2)
            + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2);
    return Math.sqrt(magnitudePower);
  }

  /**
   * Return this vector with each component divided by its magnitude.
   * @return the unit representation of this vector.
   * @throws IllegalArgumentException if a vector with 0 magnitude is
   *        passed as an argument.
   */
  public Vector3D normalize() throws IllegalArgumentException {
    if (this.getMagnitude() == 0) {
      throw new IllegalArgumentException("this vector cannot be normalized");
    } else {
      double newX = this.getX() / this.getMagnitude();
      double newY = this.getY() / this.getMagnitude();
      double newZ = this.getZ() / this.getMagnitude();
      return new Vector3D(newX, newY, newZ);
    }
  }

  /**
   * Return vector addition by mathematical definition.
   * @param other as the other of two vectors
   * @return the vector after addition
   */
  public Vector3D add(Vector3D other) {
    double addedX = this.getX() + other.getX();
    double addedY = this.getY() + other.getY();
    double addedZ = this.getZ() + other.getZ();
    return new Vector3D(addedX, addedY, addedZ);
  }

  /**
   * Return this vector multiplied by a constant.
   * @param x the multiplying factor
   * @return the multiplied vector
   */
  public Vector3D multiply(double x) {
    return new Vector3D(this.getX() * x, this.getY() * x, this.getZ() * x);
  }

  /**
   * Return dot product value of two vectors.
   * @param other as the other of two vectors
   * @return the dot product value of two vectors
   */
  public double dotProduct(Vector3D other) {
    return this.getX() * other.getX() + this.getY() * other.getY()
            + this.getZ() * other.getZ();
  }

  /**
   * Return the smaller angle between two vectors.
   * @param other as the other of two vectors
   * @return the smaller angle between two vectors
   * @throws IllegalArgumentException if any vector with 0 magnitude is
   *        passed as an argument.
   */
  public double angleBetween(Vector3D other) throws IllegalArgumentException {
    if (this.getMagnitude() == 0 || other.getMagnitude() == 0) {
      throw new IllegalArgumentException("invalid cosine value");
    } else {
      double valueCosine = Math.abs(this.normalize().dotProduct(other.normalize()));
      return Math.acos(valueCosine) / Math.PI * 180 ;
    }
  }
}









