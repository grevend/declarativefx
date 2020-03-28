package grevend.declarativefx.components.fx;
import grevend.declarativefx.components.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.function.Consumer;

public class TextField extends Component<javafx.scene.control.TextField> {
    private javafx.scene.control.TextField textField;
    private Consumer<String[]> callback;


    /**
     * creates a javafx TextField with a placeholder
     * @param placeholder String to be placed before a user inputs something
     * @param callback String[] (the callback that  will receive the id of the element and the text in it)
     *                 the first element of the string[] is the ID and the second the text
     * @param id the ID of the element to tell the difference between more textfields
     */
    public TextField(String placeholder, Consumer<String[]> callback, String id) {
        this.textField = new javafx.scene.control.TextField();
        this.textField.setOnAction((e)-> onChange());
        this.callback = callback;
        this.textField.setPromptText(placeholder);
        this.textField.setId(id);
    }

    /**
     * creates a javafx TextField without a placeholder
     * @param callback String[] (the callback that  will receive the id of the element and the text in it)
     *                 the first element of the string[] is the ID and the second the text
     * @param id the ID of the element to tell the difference between more textfields
     */
    public TextField(Consumer<String[]> callback, String id) {
        this.textField = new javafx.scene.control.TextField();
        this.textField.setOnAction((e)-> onChange());
        this.callback = callback;
        this.textField.setId(id);
    }

    public javafx.scene.control.TextField construct() {
        return this.textField;
    }

    private void onChange(){
        String[] temp = {this.textField.getId(), this.textField.getText()};
        this.callback.accept(temp);
    }

    /**
     * Set or update the text-fields placeholder
     * @param placeHolder (String)
     */
    public void setPlaceHolder(String placeHolder){
        this.textField.setPromptText(placeHolder);
    }

    /**
     * gets the text the user has entered
     * @return String (text inside the input)
     */
    public String getText(){
        return this.textField.getText();
    }
}
