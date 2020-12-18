package tech.wcobalt.lab_impl.ui;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.wcobalt.lab_impl.app.*;
import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Session;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UI {
    private static final String MAIN_CSS = "/css/main.css";
    private static final String MODENA_CSS = "/css/modena.css";

    private static final String LOGIN_FXML = "/fxml/login.fxml";

    private static final String HOME_FXML = "/fxml/home.fxml";

    private static final String SECURITY_FXML = "/fxml/security.fxml";

    private static final String CREATE_FIRST_ADMINISTRATOR_FXML = "/fxml/create_first_administrator.fxml";

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
    private Category currentCategoryHome;

    private Stage stage;

    //elements
    @FXML
    private TextField cffanickname, cffapassword;

    @FXML
    private TextField signInNickname, signInPassword;

    @FXML
    private TextField securityOldPassword, securityNewPassword;

    @FXML
    private ListView<CategoryElement> homeList;

    private static final boolean IS_INFRASTRUCTURE_STUBBED = true;

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

        handleAuthentication();
    }

    private void handleAuthentication() {
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
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        homeList.setCellFactory(param -> {
            ListCell<CategoryElement> element = new ListCell<>() {
                @Override
                protected void updateItem(CategoryElement categoryElement, boolean empty) {
                    super.updateItem(categoryElement, empty);

                    if (empty || categoryElement == null)
                        setText(null);
                    else {
                        if (categoryElement.isCategory())
                            getStyleClass().add("category_element");

                        setText(categoryElement.isCategory() ? categoryElement.getCategory().getName()
                                : categoryElement.getEntry().getTitle());
                    }
                }
            };

            element.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        goToCategoryElement();
                    }
                }
            });

            return element;
        });

        try {
            currentCategoryHome = categoriesCRUDUseCase.loadRootCategory(currentFamilyMember);

            showCurrentCategoryHome();
        } catch (RightsViolationException e) {
            e.printStackTrace();

            showMessage(e.getMessage());
        }

        showStage();
    }

    private void showCurrentCategoryHome() {
        try {
            List<CategoryElement> categoryElements =
                    viewCategoryUseCase.loadCategoryElements(currentFamilyMember, currentCategoryHome.getId(), true);

            homeList.itemsProperty().getValue().clear();
            homeList.itemsProperty().getValue().addAll(categoryElements);
        } catch (RightsViolationException exc) {
            exc.printStackTrace();

            showMessage(exc.getMessage());
        }
    }

    private void goToCategoryElement() {
        CategoryElement categoryElement = homeList.getSelectionModel().getSelectedItem();

        if (categoryElement != null) {
            if (categoryElement.isCategory()) {
                currentCategoryHome = categoryElement.getCategory();

                showCurrentCategoryHome();
            } else {
                goToEntry(categoryElement.getEntry());
            }
        }
    }

    public void goToEntry(Entry entry) {

    }

    public void goToLogin() {
        stage.setScene(loadFXML(LOGIN_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goToCreateFirstAdministrator() {
        stage.setScene(loadFXML(CREATE_FIRST_ADMINISTRATOR_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goToFavorites() {

    }

    public void goToSearch() {

    }

    public void goToUsers() {

    }

    public void goToCollections() {

    }

    public void goToSecurity() {
        stage.setScene(loadFXML(SECURITY_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goBackCategoryHome() {
        if (currentCategoryHome.getParentCategory() != -1) {
            currentCategoryHome = categoriesCRUDUseCase.loadCategory(currentCategoryHome.getParentCategory());

            showCurrentCategoryHome();
        }
    }

    public void goOut() {
        saveLoadSessionsUseCase.removeSession();

        session = null;
        currentFamilyMember = null;

        goToLogin();
    }

    public void goToCreateEntry() {

    }

    public void goToCreateCategory() {
        String result = askQuestion("New category", "Name");

        if (result != null) {
            try {
                categoriesCRUDUseCase.createCategory(currentFamilyMember, new Category(0, currentCategoryHome.getId(), result));

                showCurrentCategoryHome();
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public void goToRemoveElement() {
        CategoryElement categoryElement = homeList.getSelectionModel().getSelectedItem();

        if (categoryElement != null) {
            try {
                if (categoryElement.isCategory())
                    categoriesCRUDUseCase.removeCategory(currentFamilyMember, categoryElement.getCategory().getId());
                else
                    entriesCRUDUseCase.removeEntry(currentFamilyMember, categoryElement.getEntry().getId());

                showCurrentCategoryHome();
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public void onSignIn() {
        try {
            session = authenticateUseCase.authenticateByPair(signInNickname.getText(), signInPassword.getText());

            if (session != null) {
                currentFamilyMember = familyMembersCRUDUseCase.loadFamilyMember(session.getFamilyMember());

                if (!IS_INFRASTRUCTURE_STUBBED)
                    saveLoadSessionsUseCase.saveSession(session);

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

    public void onChangePassword() {
        String oldPassword = securityOldPassword.getText();
        String newPassword = securityNewPassword.getText();

        try {
            changeMyPasswordUseCase.changeMyPassword(currentFamilyMember, oldPassword, newPassword);

            securityOldPassword.clear();
            securityNewPassword.clear();

            showMessage("Your password was successfully changed");
        } catch (RightsViolationException exc) {
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

    private String askQuestion(String title, String message) {
        TextInputDialog prompt = new TextInputDialog();

        prompt.initOwner(stage);
        prompt.setTitle(title);
        prompt.setHeaderText(message);
        prompt.setContentText(null);

        Optional<String> result = prompt.showAndWait();

        return result.isPresent() && !result.get().isEmpty() ? result.get() : null;
    }
}
