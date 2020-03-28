package grevend.declarativefx.components.fx;
import grevend.declarativefx.components.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.function.Consumer;

public class TextArea extends Component<javafx.scene.control.TextArea> {
    private javafx.scene.control.TextArea textArea;
    private Consumer<String[]> callback;


    /**
     * creates a javafx TextField with a placeholder
     * @param placeholder String to be placed before a user inputs something
     * @param callback String[] (the callback that  will receive the id of the element and the text in it)
     *                 the first element of the string[] is the ID and the second the text
     * @param id the ID of the element to tell the difference between more textfields
     */
    public TextArea(String placeholder, Consumer<String[]> callback, String id) {
        this.textArea = new javafx.scene.control.TextArea();
        textArea.textProperty().addListener((observable, oldValue, newValue) -> { // new method of viewing changes
            onChange();
        });
        this.callback = callback;
        this.textArea.setPromptText(placeholder);
        this.textArea.setId(id);
    }

    /**
     * creates a javafx TextField without a placeholder
     * @param callback String[] (the callback that  will receive the id of the element and the text in it)
     *                 the first element of the string[] is the ID and the second the text
     * @param id the ID of the element to tell the difference between more textfields
     */
    public TextArea(Consumer<String[]> callback, String id) {
        this.textArea = new javafx.scene.control.TextArea();
        textArea.textProperty().addListener((observable, oldValue, newValue) -> { // new method of viewing changes
            onChange();
        });
        this.callback = callback;
        this.textArea.setId(id);
    }

    public javafx.scene.control.TextArea construct() {
        return this.textArea;
    }

    private void onChange(){
        String[] temp = {this.textArea.getId(), this.textArea.getText()};
        this.callback.accept(temp);
    }

    /**
     * Set or update the text-fields placeholder
     * @param placeHolder (String)
     */
    public void setPlaceHolder(String placeHolder){
        this.textArea.setPromptText(placeHolder);
    }

    /**
     * gets the text the user has entered
     * @return String (text inside the input)
     */
    public String getText(){
        return this.textArea.getText();
    }
}
