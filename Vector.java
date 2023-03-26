/**
 * Created by mas.dicicco on 5/8/14.
 */
public class Vector implements Cloneable {

    public double x;
    public double y;

    //Component form
    public Vector(double cx, double cy) {
        x = cx;
        y = cy;
    }

    public Vector(Vector v) {
        this(v.x, v.y);
    }

    //Polar form
    public static Vector fromPolar(double mag, double theta) {
        return new Vector((mag * Math.cos(theta)), (mag * Math.sin(theta)));
    }

    public void set(Vector v) {
        x = v.x;
        y = v.y;
    }

    public Vector add(Vector v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector subtract(Vector v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector multiply(double m) {
        x *= m;
        y *= m;
        return this;
    }

    public Vector divide(double d) {
        x /= d;
        y /= d;
        return this;
    }

    public Vector projectOnTo(Vector v) {
        return this.multiply((this.dotProduct(v) / Math.pow(this.magnitude(), 2)));
    }


    public double dotProduct(Vector v) {
        return x * v.x + y * v.y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector setMagnitude(double m) {
        double theta=getTheta();
        return fromPolar(theta, m);
    }

    public Vector unitize() {
        divide(magnitude());
        return this;
    }

    public Vector unitangent() {
        unitize();
        double xt = x;
        x =- y;
        y = xt;
        return this;
    }

    public double getTheta() {
        return (double) Math.atan2(y, x);
    }

    public Vector clone() {
        return new Vector(x, y);
    }

    @Override
    public String toString() {
        return String.format("[%.3f,%.3f]", x, y);
    }
}