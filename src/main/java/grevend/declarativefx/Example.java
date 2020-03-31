package grevend.declarativefx;

import grevend.declarativefx.components.Layout;
import javafx.application.Application;
import javafx.stage.Stage;

import static grevend.declarativefx.components.Compat.Binding;
import static grevend.declarativefx.components.Compat.Root;
import static grevend.declarativefx.components.Controls.Button;
import static grevend.declarativefx.components.Layout.*;

public class Example extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setWidth(175);
        stage.setHeight(100);

        var root = Root(
            HBox(
                VBox(
                    Binding("num", "text", (num, text) ->
                        VBox(
                            Text(text.compute(num, () -> "Value: " + num.get(0))),
                            Button("Increment", event -> {
                                num.set((int) num.get(0) + 1);
                            }),
                            VBox(
                                Binding("num", Layout::Text),
                                VBox(
                                    Text("1"),
                                    Text("2")
                                )
                            ),
                            VBox(
                                VBox(
                                    Text("3")
                                )
                            )
                        )
                    )
                )
            )
        );
        root.construct();
        System.out.println(root.stringifyHierarchy());
        root.launch(stage);
    }

}