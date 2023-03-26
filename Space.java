import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 1/29/2016.
 */
public class Space {

    public static double BZoom = 1, Zoom = 1;

    public GraphicsContext g;
    public double width, height, depth;
    public ArrayList<Entity3D> entities, adding;
    public Label[] labels;

    public Vector3D TRot;

    private long now, framesTimer=0;
    private int framesCount = 0, framesCountAvg=0;

    public Space(GraphicsContext gx, int w, int h, int d) {
        g = gx;
        width = w;
        height = h;
        depth = d;
        entities = new ArrayList<>();
        adding = new ArrayList<>();

        TRot = new Vector3D(0, 0, 0);
//        addAxis();

        labels = new Label[] {
                new Label("Timestep: " + Main.TIMESTEP),
                new Label("FPS: " + fps()),
                new Label("Points: " + entities.size()),
                new Label("Zoom: " + (Zoom / BZoom) + "X")
        };
    }

    public void graph() {
        double x, y, z;
        int tbound = 600;
        int dbound = 100;
        int g = 4;
        switch (g) {
            case 0:
                //VERY CURLED HORN
                for (double u = 0; u < 2*Math.PI; u += 0.05) {
                    for (double v = -2*Math.PI; v < 2*Math.PI; v += 0.04) {
                        x = dbound*((5/4)*(1-(v/(2*Math.PI)))*Math.cos(2*v)*(1+Math.cos(u))+Math.cos(2*v));
                        y = dbound*((5/4)*(1-(v/(2*Math.PI)))*Math.sin(2*v)*(1+Math.cos(u))+Math.sin(2*v));
                        z = dbound*(((9*v)/(2*Math.PI))+(5/4)*(1-(v/(2*Math.PI)))*Math.sin(u));
                        int mag = (int) new Vector3D(x, y, z).magnitude();
                        adding.add(new Entity3D(x, y, z, Color.hsb(mag/32-dbound, 1, 1, 0.5), this));
                    }
                }
                break;
            case 1:
                //PARABALOID
                int bx = 4;
                for (double u = -bx; u < bx; u += 0.03) {
                    for (double v = -bx; v < bx; v += 0.03) {
                        z = Math.sin(u*u+v*v);
                        adding.add(new Entity3D(u, -z, v, Color.rgb((int) (50*(1+z)), 150, (int) (75*(1+z))), this));
                    }
                }
                break;
            case 2:
                //ASTROIDAL ELIPSOID
                for (double u = -1; u < 1; u += 0.015) {
                    for (double v = 0; v < 1; v += 0.015) {
//                        x = (a+Math.cos(0.5*u)*Math.sin(v)-Math.sin(0.5*u)*Math.sin(2*v))*Math.cos(u);
//                        y = (a+Math.cos(0.5*u)*Math.sin(v)-Math.sin(0.5*u)*Math.sin(2*v))*Math.sin(u);
//                        z = Math.sin(0.5*u)*Math.sin(v)+Math.cos(0.5*u)*Math.sin(2*v);
//                        x = (Math.cos(0.5*u)*(Math.sqrt(2)+Math.cos(v))+Math.sin(0.5*u)*Math.sin(v)*Math.cos(v))*Math.cos(u);
//                        y = (Math.cos(0.5*u)*(Math.sqrt(2)+Math.cos(v))+Math.sin(0.5*u)*Math.sin(v)*Math.cos(v))*Math.sin(u);
//                        z = -Math.sin(0.5*u)*(Math.sqrt(2)+Math.cos(v))+Math.cos(0.5*u)*Math.sin(v)*Math.cos(v);
//                        x = (-2d/15d)*Math.cos(u)*(3d*Math.cos(v)-30d*Math.sin(u)+90d*Math.pow(Math.cos(u),4)*Math.sin(u)-60d*Math.pow(Math.cos(u),6)*Math.sin(u)+5d*Math.cos(u)*Math.cos(v)*Math.sin(u));
//                        y = (-1d/15d)*Math.sin(u)*(3d*Math.cos(v)-3d*Math.pow(Math.cos(u),2)*Math.cos(v)-48d*Math.pow(Math.cos(u),4)*Math.cos(v)+48d*Math.pow(Math.cos(u),6)*Math.cos(v)-60d*Math.sin(u)+5d*Math.cos(u)*Math.cos(v)*Math.sin(u)-5d*Math.pow(Math.cos(u),3)*Math.cos(v)*Math.sin(u)-80d*Math.pow(Math.cos(u),5)*Math.cos(v)*Math.sin(u)+80d*Math.pow(Math.cos(u),7)*Math.cos(v)*Math.sin(u));
//                        z = (2d/15d)*(3d+5d*Math.cos(u)*Math.sin(u))*Math.sin(v);
//                        x = 0.5*Math.cos(u)*Math.sin(2*v);
//                        y = 0.5*Math.sin(u)*Math.sin(2*v);
//                        z = 0.5*(Math.pow(Math.cos(v),2)-Math.pow(Math.cos(u)*Math.sin(v),2));
                        x = u;
                        y = 0;
                        z = v;
                        int mag = (int) new Vector3D(x, y, z).magnitude();
                        adding.add(new Entity3D(x, -y, z, Color.hsb(mag*5-dbound, 1, 1, 0.5), this));
                    }
                }
                break;
            case 3:
                //Testing
                int db = 6;
                double step = 0.025;
                for (x = -db; x < db; x += step) {
                    for (y = -db; y < db; y += step) {
                        double x2 = Math.pow(x, 2);
                        double y2 = Math.pow(y, 2);
//                        double pow = Math.pow(y, x);
//                        z = Math.log1p(Math.sin(x*y)+Math.cos(x*y));
//                        z = Math.pow(Math.E, Math.cosh(x*y));
//                        z = x*Math.pow(y,3)-y*Math.pow(x,3);
//                        z=Math.cos(Math.abs(x)+Math.abs(y))*(Math.abs(x)+Math.abs(y));
//                        int mag = (int) new Vector3D(x, y, z).magnitude();
//                        z = (Math.floor(-Math.pow(Math.E,-x*y/1)*Math.cos((x2+y2)/10))+14*Math.log(10000/(x2+y2)+0.01))*Math.floor(Math.cos(x2+y2)/10)+3*(Math.ceil(x)-Math.floor(x))*(Math.ceil(y)-Math.floor(y));
//                        z = 5 - Math.pow(y, 2);
                        z = Math.log1p(x*y)/(x*y);
                        adding.add(new Entity3D(dbound*x/16, dbound*y/16, dbound*z/256, Color.RED, this));
                    }
                }
                break;
            case 4:
                implicit();
                break;
        }
    }

    double sx, sy, sz, step = 0.05, bound = 0.4;
    int zm = 350, s = 4;
    public void implicit() {
        ArrayList<Vector3D> pos = new ArrayList<>((int) Math.pow(2*s/step, 3));
        for (sx = -s; sx <= s; sx += step) {
            for (sy = -s; sy <= s; sy += step) {
                for (sz = -s; sz <= s; sz += step) {
                    pos.add(new Vector3D(sx, sy, sz));
                }
            }
        }
        pos.parallelStream().forEach(p -> {
            double x=p.x, y=p.y, z=p.z, a=0, b=-2, c=1.5, f, t = (1.0 + Math.pow(5, 0.5))/2.0, t2, x2, y2, z2, exp;
            t2 = Math.pow(t, 2);
            x2 = Math.pow(x, 2);
            y2 = Math.pow(y, 2);
            z2 = Math.pow(z, 2);
            exp = Math.cos(x/y+x/z);
//            f = Math.sin(x*exp)+Math.sin(y*exp)+Math.sin(z*exp)-2;
//            f = Math.pow(x, y)*Math.pow(y, z)*Math.pow(z, x)-1;
//            f = 2*y*(y*y-3*x*x)*(1-z*z)+Math.pow(x*x+y*y,2)-(9*z*z-1)*(1-z*z);
//            f = Math.pow(x,4)+Math.pow(y,4)+Math.pow(z,4)+a*Math.pow(x*x+y*y+z*z,2)+b*(x*x+y*y+z*z)+c;
//            f = Math.cos(x)+Math.cos(y)+Math.cos(z);
//            f = 4*(t2*x2-y2)*(t2*y2-z2)*(t2*z2-x2)-(1+2*t)*Math.pow(x2+y2+z2-1,2);
            f = Math.cos(x2)+Math.cos(y2)+Math.cos(z2);
//            f = x*Math.pow(y,3)-y*Math.pow(x,3);
//            f = Math.sin(y2*Math.pow(x,3))-Math.sin(x2*Math.pow(y,3));
//            f = Math.pow(x,3)+Math.pow(y,3)+Math.pow(z,3)-x-y-z;
//            f = Math.pow(2.5*x2*(1.0-x2)-2.5*y2, 2)+3.0*z2-0.025*(1.0+1.0*(x2+y2+z2));
//            f = Math.sin(x%(1/y))+Math.sin(y%(1/z))+Math.sin(z%(1/x));
//            f = x + y + z;
            if(f <= bound && f > -bound)
                adding.add(new Entity3D(x, y, z, Color.hsb(new Vector3D(x, y, z).magnitude()*20, 1, 1, 0.5), this));

//            f = Math.pow(3*x/2,4)+Math.pow(3*y,4)+Math.pow(z,4)-1;
//            double cc = 0.8;
//            f = Math.pow(x2+y2-cc, 2)+Math.pow(z2-1, 2);
//            if(f <= bound && f > -bound)
//                adding.add(new Entity3D(x, y, z, Color.hsb(new Vector3D(x, y, z).magnitude()*20, 1, 1, 0.5), this));
////            f = Math.pow(3*y/2,4)+Math.pow(3*z,4)+Math.pow(x,4)-1;
//            f = Math.pow(y2+z2-cc, 2)+Math.pow(x2-1, 2);
//            if(f <= bound && f > -bound)
//                adding.add(new Entity3D(x, y, z, Color.hsb(new Vector3D(x, y, z).magnitude()*20, 1, 1, 0.5), this));
////            f = Math.pow(3*z/2,4)+Math.pow(3*x,4)+Math.pow(y,4)-1;
//            f = Math.pow(z2+x2-cc, 2)+Math.pow(y2-1, 2);
//            if(f <= bound && f > -bound)
//                adding.add(new Entity3D(x, y, z, Color.hsb(new Vector3D(x, y, z).magnitude()*20, 1, 1, 0.5), this));
        });
        pos.clear();
    }

    public void ExportPointCloud() {
        FileWriter fw;
        try {
            fw = new FileWriter(new File("C:\\MyStuff\\Programming\\PointClouds\\torus.ply"));

            fw.write(String.format("ply"));
            fw.write(System.lineSeparator());
            fw.write(String.format("format ascii 1.0"));
            fw.write(System.lineSeparator());
            fw.write(String.format("comment made by Daniel Janikowski"));
            fw.write(System.lineSeparator());
            int count = 0;
            for (Entity3D e : entities) {
                if(e != null)
                    count++;
            }
            fw.write(String.format("element vertex "+count));
            fw.write(System.lineSeparator());
            fw.write(String.format("property float x"));
            fw.write(System.lineSeparator());
            fw.write(String.format("property float y"));
            fw.write(System.lineSeparator());
            fw.write(String.format("property float z"));
            fw.write(System.lineSeparator());
            fw.write(String.format("element face 0"));
            fw.write(System.lineSeparator());
            fw.write(String.format("property list uchar uint vertex_indices"));
            fw.write(System.lineSeparator());
            fw.write(String.format("end_header"));
            fw.write(System.lineSeparator());
            for (Entity3D e : entities) {
                if(e != null) {
                    double a = e.OPos.x, b = e.OPos.y, c = e.OPos.z;
                    fw.write(String.format((Math.round(10000d*a)/10000d)+" "+(Math.round(10000d*b)/10000d)+" "+(Math.round(10000d*c)/10000d)));
                    fw.write(System.lineSeparator());
                }
            }
            fw.close();
            System.out.println("File Created");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void draw3D() {
//        System.out.println(TRot);
        g.clearRect(0, 0, width, height);
//        addLines();
        entities.parallelStream().forEach(entity3D -> {
            if (entity3D != null)
                entity3D.updatePos();
        });
        entities.forEach(entity3D -> {
            if (entity3D != null)
                entity3D.draw(g);
        });
        TRot.multiply(0);
    }
    public void addAxis() {
        entities.add(new Entity3D(0, 0, 0, Color.WHITE, this));
        entities.add(new Entity3D(25, 0, 0, Color.RED, this));
        entities.add(new Entity3D(0, 25, 0, Color.GREEN, this));
        entities.add(new Entity3D(0, 0, 25, Color.BLUE, this));
    }
    public void addLines() {
        g.setStroke(Color.RED);
        g.strokeLine(entities.get(0).Vdraw.x, entities.get(0).Vdraw.y, entities.get(1).Vdraw.x, entities.get(1).Vdraw.y);
        g.setStroke(Color.GREEN);
        g.strokeLine(entities.get(0).Vdraw.x, entities.get(0).Vdraw.y, entities.get(2).Vdraw.x, entities.get(2).Vdraw.y);
        g.setStroke(Color.BLUE);
        g.strokeLine(entities.get(0).Vdraw.x, entities.get(0).Vdraw.y, entities.get(3).Vdraw.x, entities.get(3).Vdraw.y);
    }
    public void postUpdate3D() {
        entities.addAll(adding);
        adding.clear();
    }
    public int fps() {
        now=System.currentTimeMillis();
        framesCount++;
        if(now-framesTimer>1000) {
            framesTimer = now;
            framesCountAvg = framesCount;
            framesCount = 0;
        }
        return framesCountAvg;
    }
    public void updateText() {
        labels[0].setText("Timestep: " + Main.TIMESTEP);
        labels[1].setText("FPS: " + fps());
        labels[2].setText("Points: " + entities.size());
        labels[3].setText("Zoom: " + (Zoom / BZoom) + "X");
    }
    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setPrefWidth(175);
        vbox.setBackground(new Background(new BackgroundFill(Color.grayRgb(25), CornerRadii.EMPTY, new Insets(5))));
        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
            labels[i].setTextFill(Color.grayRgb(150));
            vbox.getChildren().add(labels[i]);
        }

        return vbox;
    }
}
