package grevend.declarativefx;

import javafx.application.Application;
import javafx.stage.Stage;

import static grevend.declarativefx.Components.*;

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
                                Binding("num", Components::Text),
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