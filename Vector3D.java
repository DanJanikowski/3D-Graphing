/**
 * Created by Dan on 1/31/2016.
 */
public class Vector3D implements Cloneable {

    public double x;
    public double y;
    public double z;

    //Component form
    public Vector3D(double cx, double cy, double cz) {
        x = cx;
        y = cy;
        z = cz;
    }

    public Vector3D(Vector3D v) {
        this(v.x, v.y, v.z);
    }

    //Polar form
    public static Vector3D fromPolar(double mag, double theta, double phi) {
        return new Vector3D(mag * Math.cos(theta) * Math.sin(phi), mag * Math.sin(theta) * Math.sin(phi), mag * Math.cos(phi));
    }

    public void set(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3D add(Vector3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3D subtract(Vector3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3D multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    public Vector3D divide(double d) {
        x /= d;
        y /= d;
        z /= d;
        return this;
    }

    public Vector3D projectOnTo(Vector3D v) {
        return this.multiply((this.dotProduct(v) / Math.pow(this.magnitude(), 2)));
    }


    public double dotProduct(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double getTheta() {
        return Math.atan2(y, x);
    }

    public double getPhi() {
        return Math.acos(z / magnitude());
    }

    public Vector3D setMagnitude(double m) {
        return fromPolar(m, getTheta(), getPhi());
    }

    public Vector3D unitize() {
        divide(magnitude());
        return this;
    }

    public Vector3D unitangent() {
        unitize();
        double xt = x;
        x =- y;
        y = xt;
        return this;
    }

    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    public boolean isEqual(Vector3D v) {
        if (this.x == v.x && this.y == v.y && this.z == v.z)
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return String.format("[%.3f,%.3f,%.3f]", x, y, z);
    }

    public Vector3D rotate(double t1, double t2, double t3) {
        t1 = Math.toRadians(t1);
        t2 = Math.toRadians(t2);
        t3 = Math.toRadians(t3);
        Vector3D[] matrix = new Vector3D[3];

        matrix[0] = new Vector3D(1, 0, 0);
        matrix[1] = new Vector3D(0, Math.cos(t1), -Math.sin(t1));
        matrix[2] = new Vector3D(0, Math.sin(t1), Math.cos(t1));
        this.set(new Vector3D(matrix[0].dotProduct(this), matrix[1].dotProduct(this), matrix[2].dotProduct(this)));

        matrix[0] = new Vector3D(Math.cos(t2), 0, Math.sin(t2));
        matrix[1] = new Vector3D(0, 1, 0);
        matrix[2] = new Vector3D(-Math.sin(t2), 0, Math.cos(t2));
        this.set(new Vector3D(matrix[0].dotProduct(this), matrix[1].dotProduct(this), matrix[2].dotProduct(this)));

        matrix[0] = new Vector3D(Math.cos(t3), -Math.sin(t3), 0);
        matrix[1] = new Vector3D(Math.sin(t3), Math.cos(t3), 0);
        matrix[2] = new Vector3D(0, 0, 1);
        this.set(new Vector3D(matrix[0].dotProduct(this), matrix[1].dotProduct(this), matrix[2].dotProduct(this)));

        return this;
    }
}
