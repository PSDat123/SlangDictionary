package com.psdat.slangdictionary;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.io.*;
import java.util.*;


public class SlangController {
    // buttons
    @FXML
    private Button lookup_slang_btn;
    @FXML
    private Button lookup_def_btn;
    @FXML
    private Button history_btn;
    @FXML
    private Button add_new_slang_btn;
    @FXML
    private Button edit_slang_btn;
    @FXML
    private Button delete_slang_btn;
    @FXML
    private Button reset_default_btn;
    @FXML
    private Button random_slang_btn;
    @FXML
    private Button slang_quiz_btn;
    @FXML
    private Button def_quiz_btn;
    // panes
    @FXML
    private AnchorPane title_pane;
    @FXML
    private AnchorPane lookup_slang_pane;
    @FXML
    private AnchorPane lookup_def_pane;
    @FXML
    private AnchorPane history_pane;
    @FXML
    private AnchorPane add_slang_pane;
    @FXML
    private AnchorPane edit_slang_pane;
    @FXML
    private AnchorPane delete_slang_pane;
    @FXML
    private AnchorPane reset_slang_pane;
    @FXML
    private AnchorPane random_slang_pane;
    @FXML
    private AnchorPane slang_quiz_pane;
    @FXML
    private AnchorPane def_quiz_pane;
    // Lookup Slang
    @FXML
    private TextField lookup_slang_inp;
    @FXML
    private TableView<String> def_table;
    @FXML
    private TableColumn<String, String> def_value_col;

    // Lookup Definition
    @FXML
    private TextField lookup_def_inp;
    @FXML
    private TableView<String> slang_table;
    @FXML
    private TableColumn<String, String> slang_value_col;

    // History
    @FXML
    private TableView<Pair<String, String>> history_table;
    @FXML
    private TableColumn<Pair<String, String>, String> history_col;
    @FXML
    private TableColumn<Pair<String, String>, String> timestamp_col;
    // Add New Slang
    @FXML
    private TextField add_slang_inp_slang;
    @FXML
    private TextField add_slang_inp_def;

    // Edit Slang
    @FXML
    private TextField edit_slang_inp_old;
    @FXML
    private TextField edit_slang_inp_new;
    // Delete Slang
    @FXML
    private TextField delete_slang_inp;
    // Random Slang
    @FXML
    private Label random_slang_text;
    @FXML
    private Label random_slang_def;
    // Slang Quiz
    @FXML
    private Label slang_quiz_question;
    @FXML
    private Button slang_quiz_btn_a;
    @FXML
    private Button slang_quiz_btn_b;
    @FXML
    private Button slang_quiz_btn_c;
    @FXML
    private Button slang_quiz_btn_d;
    @FXML
    private Label slang_quiz_result;
    // Definition Quiz
    @FXML
    private Label def_quiz_question;
    @FXML
    private Button def_quiz_btn_a;
    @FXML
    private Button def_quiz_btn_b;
    @FXML
    private Button def_quiz_btn_c;
    @FXML
    private Button def_quiz_btn_d;
    @FXML
    private Label def_quiz_result;
    private SlangQuiz currentSlangQuiz = null;
    private SlangQuiz currentDefQuiz = null;
    private SlangDictionary dict;
    public void initialize() {
        dict = new SlangDictionary();
        updateHistory();
    }

    private void updateHistory() {
        history_col.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        timestamp_col.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
        ObservableList<Pair<String, String>> items = FXCollections.observableArrayList(dict.getHistory());
        history_table.setItems(items);
    }
    @FXML
    public void changePane(ActionEvent actionEvent) {
        title_pane.setVisible(false);
        lookup_slang_pane.setVisible(false);
        lookup_def_pane.setVisible(false);
        history_pane.setVisible(false);
        add_slang_pane.setVisible(false);
        edit_slang_pane.setVisible(false);
        delete_slang_pane.setVisible(false);
        reset_slang_pane.setVisible(false);
        random_slang_pane.setVisible(false);
        slang_quiz_pane.setVisible(false);
        def_quiz_pane.setVisible(false);

        if (actionEvent.getSource() == lookup_slang_btn) {
            lookup_slang_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == lookup_def_btn) {
            lookup_def_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == history_btn) {
            history_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == add_new_slang_btn) {
            add_slang_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == edit_slang_btn) {
            edit_slang_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == delete_slang_btn) {
            delete_slang_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == reset_default_btn) {
            reset_slang_pane.setVisible(true);
        }
        else if (actionEvent.getSource() == random_slang_btn) {
            random_slang_pane.setVisible(true);
            String ranSlang = dict.getRandomSlang();
            random_slang_text.setText(ranSlang);
            random_slang_def.setText(String.join(" | ", dict.queryFromSlang(ranSlang)));
        }
        else if (actionEvent.getSource() == slang_quiz_btn) {
            slang_quiz_pane.setVisible(true);
            if (currentSlangQuiz == null) {
                generateSlangQuiz();
            }
        }
        else if (actionEvent.getSource() == def_quiz_btn) {
            def_quiz_pane.setVisible(true);
            if (currentDefQuiz == null) {
                generateDefQuiz();
            }
        }
    }

    @FXML
    public void lookUpSlang() {
        String slang = lookup_slang_inp.getText().trim();
        if (slang.isEmpty()) return;
        def_value_col.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        def_table.getItems().setAll(dict.queryFromSlang(slang, true));
        updateHistory();
    }
    @FXML
    public void lookUpDef() {
        String def = lookup_def_inp.getText().trim();
        if (def.isEmpty()) return;
        slang_value_col.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        slang_table.getItems().setAll(dict.queryFromDefinition(def));
    }
    private void popUpAlert(String status, String message) {
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(status);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void addNewSlang() {
        String slang = add_slang_inp_slang.getText().trim();
        String def = add_slang_inp_def.getText().trim();
        if (slang.isEmpty() || def.isEmpty()) return;
        List<String> definitions = Arrays.asList(def.split("\\s*\\|\\s*"));
        if (dict.containsSlang(slang)) {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Slang word already existed. Click YES to overwrite, NO to duplicate, CANCEL to close this message");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent()) {
                if (option.get().equals(ButtonType.YES)) {
                    if (dict.addSlang(slang, definitions, true)) popUpAlert("Success", "Successfully added slang word!");
                    else popUpAlert("Error", "Something went wrong while adding slang");
                } else if (option.get().equals(ButtonType.NO)) {
                    if(dict.addSlang(slang, definitions)) popUpAlert("Success", "Successfully added slang word!");
                    else popUpAlert("Error", "Something went wrong while adding slang");
                }
            }
        }
        else {
            dict.addSlang(slang, definitions);
            popUpAlert("Success", "Successfully added slang word!");
        }
    }
    @FXML
    public void editSlang() {
        String oldSlang = edit_slang_inp_old.getText().trim();
        String newSlang = edit_slang_inp_new.getText().trim();
        if (oldSlang.isEmpty() || newSlang.isEmpty()) return;
        if (!dict.containsSlang(oldSlang)) popUpAlert("Error", "Slang word does not exist");
        else if (dict.editSlang(oldSlang, newSlang)) popUpAlert("Success", "Successfully edited slang word!");
        else popUpAlert("Error", "Something went wrong");
    }
    @FXML
    public void deleteSlang() {
        String slang = delete_slang_inp.getText().trim();
        if (slang.isEmpty()) return;
        if (!dict.containsSlang(slang)) {
            popUpAlert("Error", "Slang word does not exist");
            return;
        }
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the slang " + slang + "?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent()) {
            if (option.get().equals(ButtonType.YES)) {
                if (dict.deleteSlang(slang)) popUpAlert("Success", "Successfully deleted slang word!");
                else popUpAlert("Error", "Something went wrong while deleting slang");
            }
        }
    }
    @FXML
    public void resetSlang() {
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to reset the slang word database?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent()) {
            if (option.get().equals(ButtonType.YES)) {
                if (dict.factoryReset()) popUpAlert("Success", "Successfully reset slang word database!");
                else popUpAlert("Error", "Something went wrong while resetting slang");
            }
        }
    }
    @FXML
    public void generateSlangQuiz() {
        slang_quiz_result.setVisible(false);
        currentSlangQuiz = dict.getSlangQuiz(4);
        List<String> answers = currentSlangQuiz.getAnswers();
        slang_quiz_question.setText(currentSlangQuiz.getQuestion());
        slang_quiz_btn_a.setText(answers.get(0));
        slang_quiz_btn_b.setText(answers.get(1));
        slang_quiz_btn_c.setText(answers.get(2));
        slang_quiz_btn_d.setText(answers.get(3));
    }
    @FXML
    public void slangQuizChooseA() {
        slang_quiz_result.setVisible(true);
        if (currentSlangQuiz.checkAnswer(0)) {
            slang_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            slang_quiz_result.setText("CORRECT!");
        }
        else {
            slang_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            slang_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void slangQuizChooseB() {
        slang_quiz_result.setVisible(true);
        if (currentSlangQuiz.checkAnswer(1)) {
            slang_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            slang_quiz_result.setText("CORRECT!");
        }
        else {
            slang_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            slang_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void slangQuizChooseC() {
        slang_quiz_result.setVisible(true);
        if (currentSlangQuiz.checkAnswer(2)) {
            slang_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            slang_quiz_result.setText("CORRECT!");
        }
        else {
            slang_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            slang_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void slangQuizChooseD() {
        slang_quiz_result.setVisible(true);
        if (currentSlangQuiz.checkAnswer(3)) {
            slang_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            slang_quiz_result.setText("CORRECT!");
        }
        else {
            slang_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            slang_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void generateDefQuiz() {
        def_quiz_result.setVisible(false);
        currentDefQuiz = dict.getDefinitionQuiz(4);
        List<String> answers = currentDefQuiz.getAnswers();
        def_quiz_question.setText(currentDefQuiz.getQuestion());
        def_quiz_btn_a.setText(answers.get(0));
        def_quiz_btn_b.setText(answers.get(1));
        def_quiz_btn_c.setText(answers.get(2));
        def_quiz_btn_d.setText(answers.get(3));
    }
    @FXML
    public void defQuizChooseA() {
        def_quiz_result.setVisible(true);
        if (currentDefQuiz.checkAnswer(0)) {
            def_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            def_quiz_result.setText("CORRECT!");
        }
        else {
            def_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            def_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void defQuizChooseB() {
        def_quiz_result.setVisible(true);
        if (currentDefQuiz.checkAnswer(1)) {
            def_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            def_quiz_result.setText("CORRECT!");
        }
        else {
            def_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            def_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void defQuizChooseC() {
        def_quiz_result.setVisible(true);
        if (currentDefQuiz.checkAnswer(2)) {
            def_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            def_quiz_result.setText("CORRECT!");
        }
        else {
            def_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            def_quiz_result.setText("WRONG!");
        }
    }
    @FXML
    public void defQuizChooseD() {
        def_quiz_result.setVisible(true);
        if (currentDefQuiz.checkAnswer(3)) {
            def_quiz_result.setTextFill(Color.color(0, 0.9, 0));
            def_quiz_result.setText("CORRECT!");
        }
        else {
            def_quiz_result.setTextFill(Color.color(0.9, 0, 0));
            def_quiz_result.setText("WRONG!");
        }
    }
}