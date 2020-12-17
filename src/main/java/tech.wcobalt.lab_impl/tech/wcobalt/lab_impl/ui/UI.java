package tech.wcobalt.lab_impl.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.wcobalt.lab_impl.app.*;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Session;
import tech.wcobalt.lab_impl.persistence.CDAudiosRepository;
import tech.wcobalt.lab_impl.persistence.CDVideosRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UI {
    private static final String MAIN_CSS = "/css/main.css";
    private static final String MODENA_CSS = "/css/modena.css";

    private static final String LOGIN_FXML = "/fxml/login.fxml";
    private static final String LOGIN_CSS = "/css/login.css";

    private static final String HOME_FXML = "/fxml/home.fxml";
    private static final String HOME_CSS = "/css/home.css";

    private static final String CREATE_FIRST_ADMINISTRATOR_FXML = "/fxml/create_first_administrator.fxml";
    private static final String CREATE_FIRST_ADMINISTRATOR_CSS = "/css/create_first_administrator.css";

    private static final String TITLE = "Home Library";

    private static final int MESSAGE_BOX_WIDTH = 230;

    private AuthenticateUseCase authenticateUseCase;
    private CategoriesCRUDUseCase categoriesCRUDUseCase;
    private EntriesCRUDUseCase entriesCRUDUseCase;
    private BooksCRUDUseCase booksCRUDUseCase;
    private CDAudiosCRUDUseCase cdAudiosCRUDUseCase;
    private CDVideosCRUDUseCase cdVideosCRUDUseCase;
    private ChangeMyPasswordUseCase changeMyPasswordUseCase;
    private CommentsCRUDUseCase commentsCRUDUseCase;
    private CreateFirstFamilyAdministratorUseCase createFirstFamilyAdministratorUseCase;
    private FamilyMembersCRUDUseCase familyMembersCRUDUseCase;
    private SaveLoadSessionsUseCase saveLoadSessionsUseCase;
    private ViewCategoryUseCase viewCategoryUseCase;

    //model
    private Session session;
    private FamilyMember currentFamilyMember;

    private Stage stage;

    //elements
    @FXML
    private TextField cffanickname, cffapassword;
    @FXML
    private TextField signInNickname, signInPassword;

    public UI(AuthenticateUseCase authenticateUseCase, CategoriesCRUDUseCase categoriesCRUDUseCase,
              EntriesCRUDUseCase entriesCRUDUseCase, BooksCRUDUseCase booksCRUDUseCase,
              CDAudiosCRUDUseCase cdAudiosCRUDUseCase, CDVideosCRUDUseCase cdVideosCRUDUseCase,
              ChangeMyPasswordUseCase changeMyPasswordUseCase, CommentsCRUDUseCase commentsCRUDUseCase,
              CreateFirstFamilyAdministratorUseCase createFirstFamilyAdministratorUseCase,
              FamilyMembersCRUDUseCase familyMembersCRUDUseCase, SaveLoadSessionsUseCase saveLoadSessionsUseCase,
              ViewCategoryUseCase viewCategoryUseCase) {
        this.authenticateUseCase = authenticateUseCase;
        this.categoriesCRUDUseCase = categoriesCRUDUseCase;
        this.entriesCRUDUseCase = entriesCRUDUseCase;
        this.booksCRUDUseCase = booksCRUDUseCase;
        this.cdAudiosCRUDUseCase = cdAudiosCRUDUseCase;
        this.cdVideosCRUDUseCase = cdVideosCRUDUseCase;
        this.changeMyPasswordUseCase = changeMyPasswordUseCase;
        this.commentsCRUDUseCase = commentsCRUDUseCase;
        this.createFirstFamilyAdministratorUseCase = createFirstFamilyAdministratorUseCase;
        this.familyMembersCRUDUseCase = familyMembersCRUDUseCase;
        this.saveLoadSessionsUseCase = saveLoadSessionsUseCase;
        this.viewCategoryUseCase = viewCategoryUseCase;
    }

    public void startTheSystem() {
        PortableApplication application = new PortableApplication(this);
        application.run();
    }

    public void init() {
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle(TITLE);

        Session loadedSession = saveLoadSessionsUseCase.loadSession();

        if (loadedSession != null) {
            session = loadedSession;
            currentFamilyMember = familyMembersCRUDUseCase.loadFamilyMember(loadedSession.getFamilyMember());

            goToHome();
        } else {
            if (familyMembersCRUDUseCase.doesAtLeastOneFamilyAdministratorExist())
                goToLogin();
            else
                goToCreateFirstAdministrator();
        }
    }

    public void goToHome() {
        stage.setScene(loadFXML(HOME_FXML, Arrays.asList(
                getClass().getResource(HOME_CSS).toExternalForm(),
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goToLogin() {
        stage.setScene(loadFXML(LOGIN_FXML, Arrays.asList(
                getClass().getResource(LOGIN_CSS).toExternalForm(),
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goToCreateFirstAdministrator() {
        stage.setScene(loadFXML(CREATE_FIRST_ADMINISTRATOR_FXML, Arrays.asList(
                getClass().getResource(CREATE_FIRST_ADMINISTRATOR_CSS).toExternalForm(),
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void onSignIn() {
        try {
            session = authenticateUseCase.authenticateByPair(signInNickname.getText(), signInPassword.getText());

            if (session != null) {
                currentFamilyMember = familyMembersCRUDUseCase.loadFamilyMember(session.getFamilyMember());

                goToHome();
            } else
                showMessage("Wrong pair");
        } catch (Exception exc) {
            exc.printStackTrace();

            showMessage(exc.getMessage());
        }
    }

    public void onCreateFirstFamilyAdministrator() {
        try {
            createFirstFamilyAdministratorUseCase.createFirstFamilyAdministrator(cffanickname.getText(), cffapassword.getText());

            showMessage("You've successfully created the first family administrator. Now you need to perform authentication");

            goToLogin();
        } catch (Exception exc) {
            exc.printStackTrace();

            showMessage(exc.getMessage());
        }
    }

    private Scene loadFXML(String fxmlResourceFile, List<String> stylesheets) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load(getClass().getResourceAsStream(fxmlResourceFile));

            Scene scene = new Scene(parent);

            if (stylesheets != null)
                scene.getStylesheets().addAll(stylesheets);

            return scene;
        } catch (IOException exc) {
            exc.printStackTrace();

            return null;
        }
    }

    private void showStage() {
        stage.show();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message);

        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setPrefWidth(MESSAGE_BOX_WIDTH);
        alert.setTitle(TITLE);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }
}
