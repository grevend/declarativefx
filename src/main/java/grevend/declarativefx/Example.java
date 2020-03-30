package grevend.declarativefx;

import grevend.declarativefx.components.Components;
import grevend.declarativefx.components.fx.TextArea;
import grevend.declarativefx.components.fx.TextField;
import javafx.application.Application;
import javafx.stage.Stage;

import static grevend.declarativefx.components.Components.*;

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
                Provider("num", 0),
                VBox(
                    Consumer("num", "text", (num, text) ->
                        VBox(
                            Text(text.compute(num, () -> "Value: " + num.get(0))),
                            Button("Increment", event -> {
                                num.set((int) num.get(0) + 1);
                            }),
                            VBox(
                                Consumer("num", Components::Text),
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
                    ),
                    HBox(
                        new TextField("placeholder!", (arr)->{
                            System.out.println(arr[0]);
                            System.out.println(arr[1]);
                        }, "test"),
                        new TextArea((arr)->{
                            System.out.println(arr[0]);
                            System.out.println(arr[1]);
                        }, "textarea")
                    )
                )
            )
        );
        root.construct();
        System.out.println(root.stringifyHierarchy());
        root.launch(stage);
    }

}