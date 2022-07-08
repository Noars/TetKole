package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsPane extends BorderPane {

    Main main;
    HBox hbox;

    Locale fr_local;
    Locale en_local;
    ResourceBundle languages;
    String actualLanguageUse = "Francais";

    Label chooseLanguageLabel;

    /**
     * Initialize the constructor of this class
     * And create the gridPane with all buttons
     * By default the language on the start of the application is French
     *
     * @param main
     */
    public SettingsPane(Main main){
        super();

        this.main = main;
        this.initLanguage();

        MenuButton languageMenuButton = chooseLanguageMenuButton();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        {
            chooseLanguageLabel = new Label(languages.getString("ChooseLanguage"));
            gridPane.add(chooseLanguageLabel, 1, 0);
            chooseLanguageLabel.getStyleClass().add("textLabel");

            gridPane.add(languageMenuButton, 2,0);
        }

        hbox = new HBox(gridPane);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }

    /**
     * Function that create all language we will use
     * And set the French language by default
     */
    public void initLanguage(){
        fr_local = new Locale("fr", "FR");
        en_local = new Locale("en", "EN");
        languages = ResourceBundle.getBundle("multilinguism/language_fr_FR", fr_local);
    }

    /**
     * Function that change the language according to the 2 values passed in parameter
     *
     * @param languageValue
     * @param local
     */
    public void changeLanguage(String languageValue, Locale local){
        this.languages = ResourceBundle.getBundle("multilinguism/language_" + languageValue, local);
    }

    /**
     * Function notified by the language change
     * Set the text with is new language key
     */
    public void changeLabel(){
        chooseLanguageLabel.setText(languages.getString("ChooseLanguage"));
    }

    /**
     * Function that create the menuButton button
     * This button permit to change the language between French and English actually
     *
     * @return the button "menuButton"
     */
    public MenuButton chooseLanguageMenuButton(){
        MenuItem menuItemFR = new MenuItem(this.languages.getString("Fr"));
        MenuItem menuItemEN = new MenuItem(this.languages.getString("En"));
        MenuButton menuButton = new MenuButton(this.actualLanguageUse);

        menuItemFR.setOnAction(eventMenuLanguages -> {
            changeLanguage("fr_FR", fr_local);
            menuItemFR.setText(this.languages.getString("Fr"));
            menuItemEN.setText(this.languages.getString("En"));
            this.actualLanguageUse = menuItemFR.getText();
            menuButton.setText(this.actualLanguageUse);
            notifyLanguageChanged();
        });

        menuItemEN.setOnAction(eventMenuLanguages -> {
            changeLanguage("en_EN", en_local);
            menuItemFR.setText(this.languages.getString("Fr"));
            menuItemEN.setText(this.languages.getString("En"));
            this.actualLanguageUse = menuItemEN.getText();
            menuButton.setText(this.actualLanguageUse);
            notifyLanguageChanged();
        });

        menuButton.getItems().addAll(menuItemFR, menuItemEN);

        return menuButton;
    }

    /**
     * Function that give the actual language used in the application
     *
     * @return the language
     */
    public ResourceBundle getLanguage(){
        return this.languages;
    }

    /**
     * Function that notify all the class, who contains text, a change of language
     */
    public void notifyLanguageChanged(){
        this.changeLabel();
        main.getRecordPane().changeLabel(this.languages);
        main.getListenPane().changeLabel(this.languages);
    }
}
