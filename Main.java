import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by Dan on 1/29/2016.
 */
public class Main extends Application {

    public static final long TIMESTEP = 16;
    public static long multiplier;
    public static int width, height, depth;
    public Vector iPos, fPos;
    public static double scroll;
    public String selection;
    public Space space;
    public BorderPane border, topBorder;
    public Scene scene;
    public Canvas canvas;
    public ChoiceBox chbox;

    @Override
    public void start(Stage stage) throws Exception {
        width = 900;
        height = 900;
        depth = 900;

        multiplier = 2;
        selection = "";

        //Borders
        border = new BorderPane();
        topBorder = new BorderPane();
        border.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        border.setTop(topBorder);
        scene = new Scene(border);
        //Canvas + Space
        canvas = new Canvas(width, height);
        border.setCenter(canvas);
        GraphicsContext g = canvas.getGraphicsContext2D();
        space = new Space(g, width, height, depth);
        space.graph();
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY)
                        iPos = new Vector(e.getX(), e.getY());
                }
            });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    fPos = new Vector(e.getX(), e.getY());
                    iPos.subtract(fPos);
                    space.TRot.x += -iPos.y*0.8;
                    space.TRot.y += -iPos.x*0.8;
                    iPos.set(fPos);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    fPos = new Vector(e.getX(), e.getY());
                    iPos.subtract(fPos);
                    space.TRot.z += iPos.y*0.8;
                    iPos.set(fPos);
                }
            }
        });
        final double scaleC = 1.1;
        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                scroll = e.getDeltaY() / 80;
                if(scroll > 0)
                    space.Zoom *= scaleC;
                else space.Zoom /= scaleC;
            }
        });
        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                String c = e.getCharacter();
                switch(c) {
                    case "q":
                        space.graph();
                        break;
                    case "e":
                        space.ExportPointCloud();
                        break;
                    case "r":
                        space.Zoom = 1;
                        space.TRot.multiply(0);
                        break;

                    case "a":
                        space.addAxis();
                        break;

                    case "x":
                        System.exit(0);
                        break;
                    case "c":
                        space.entities = new ArrayList<>();
                        g.clearRect(0, 0, width, height);
//                        space.addAxis();
                        break;
                }
            }
        });
        //InfoCanvas
        border.setLeft(space.addVBox());
        //CheckBox
        scene.setRoot(border);
        stage.setScene(scene);

        stage.show();

        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::tick, 0, TIMESTEP, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        CountDownLatch drawLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            space.draw3D();
            space.updateText();
            drawLatch.countDown();
        });
        try {
            drawLatch.await();
        } catch (InterruptedException ignore) {}
        space.postUpdate3D();
    }
    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
