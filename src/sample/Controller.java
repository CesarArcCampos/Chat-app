package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private Button button_close;
    @FXML
    private ToggleButton tbutton;

    @FXML
    private final StringProperty textValue = new SimpleStringProperty("Connect");

    private Server server;
    private ServerSocket socket;
    private boolean flag = true;
    private URL url;
    private final int port = 1234;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.url = url;

        try {
            server = new Server();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        if(flag) {
            tbutton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    setTextValue("Disconnect");
                    tbutton.requestLayout();
                    try {
                        socket = new ServerSocket(port);
                        server.connectionSocket(socket, this);
                        System.out.println("> Client is connected to Server.");
                        server.receiveMessageFromClient(vbox_messages);
                    }  catch (IOException e) {
                        System.out.println("> Server is not connected.");
                    }
                } else {
                    setTextValue("Connect");
                    tbutton.requestLayout();
                    server.closeSocket(socket);
                }
            });
        }

        if (!flag) {
            try {
                socket = new ServerSocket(port);
                server.connectionSocket(socket, this);
                System.out.println("> Client is reconnected to Server.");
                server.receiveMessageFromClient(vbox_messages);
                flag = true;
            }  catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Server is not connected.");
            }
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        tf_message.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setMessageEvent();
                }
            }
        });

        button_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setMessageEvent();
            }
        });
    }

    public static void addLabel(String messageFromClient, VBox vbox) {

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                " -fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }

    private void setMessageEvent() {
        String messageToSend = tf_message.getText();
        if (!messageToSend.isEmpty()) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(5,5,5,10));

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                    "-fx-background-color: rgb(15,125,242);" +
                    " -fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hbox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hbox);

            server.sendMessageToClient(messageToSend);
            tf_message.clear();
        }
    }

    public String getTextValue() {
        return textValue.get();
    }

    public void setTextValue(String textValue) {
        this.textValue.set(textValue);
    }

    public VBox getVbox_messages() {
        return vbox_messages;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public URL getUrl() {
        return url;
    }
}
