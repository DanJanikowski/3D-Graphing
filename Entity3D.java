import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Dan on 1/31/2016.
 */
public class Entity3D {

    public Vector3D OPos, Vcartesian, Vdraw, size;
    public Color c = Color.rgb(125, 255, 150, 0.9);
    public static Space space;

    public Entity3D(double x, double y, double z, Color ac, Space s) {
        space = s;
        OPos = new Vector3D(x/s.s, y/s.s, z/s.s);
        Vcartesian = new Vector3D(x*space.zm, y*space.zm, z*space.zm).rotate(space.TRot.x, space.TRot.y, space.TRot.z);
        size = new Vector3D(Main.width, Main.height, Main.depth).divide(2);
        Vdraw = new Vector3D(x*space.zm*space.Zoom, -y*space.zm*space.Zoom, z*space.zm*space.Zoom).add(size);
        if (ac != null)
            c = ac;
    }

    public void draw(GraphicsContext g) {
//        updatePos();
        g.setFill(c);
        g.fillOval(Vdraw.x - 1, Vdraw.y - 1, 2, 2);
    }

    public void updatePos() {
        Vcartesian.rotate(space.TRot.x, space.TRot.y, space.TRot.z);
        double dx = Vcartesian.x, dy = Vcartesian.y, dz = Vcartesian.z;
//        double thing = 1/(4*(dz+space.s));
//        if(dx > 0)
//            dx -= thing;
//        else if(dx < 0)
//            dx += thing;
//        if(dy > 0)
//            dy -= thing;
//        else if(dy < 0)
//            dy += thing;
        Vdraw = new Vector3D(dx*space.Zoom, -dy*space.Zoom, dz*space.Zoom).add(size);
    }
}
