package tech.wcobalt.lab_impl.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.wcobalt.lab_impl.app.*;
import tech.wcobalt.lab_impl.domain.*;

import java.io.IOException;
import java.util.*;

public class UI {
    private static final String MAIN_CSS = "/css/main.css";
    private static final String MODENA_CSS = "/css/modena.css";

    private static final String LOGIN_FXML = "/fxml/login.fxml";

    private static final String HOME_FXML = "/fxml/home.fxml";

    private static final String SECURITY_FXML = "/fxml/security.fxml";

    private static final String CREATE_FIRST_ADMINISTRATOR_FXML = "/fxml/create_first_administrator.fxml";

    private static final String USERS_FXML = "/fxml/users.fxml";

    private static final String NEW_USER_FXML = "/fxml/new_user.fxml";

    private static final String SHOW_ENTRY_FXML = "/fxml/show_entry.fxml";

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

    interface EntryTypeUI {
        void showEntry(Entry entry, VBox where);
    }

    private Map<String, EntryTypeUI> entryTypesUIs;

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
    private TextField newUserNickname, newUserPassword;

    @FXML
    private RadioButton newUserMember;

    @FXML
    private ListView<CategoryElement> homeList;

    @FXML
    private ListView<FamilyMember> familyMembersList;

    @FXML
    private VBox whereToShowEntry;

    private static final boolean IS_INFRASTRUCTURE_STUBBED = true;

    abstract class BaseEntryTypeUI implements EntryTypeUI {
        @FXML
        private Label author, title, comment;

        protected void showEntry(Entry entry) {
            author.setText(entry.getAuthor());
            title.setText(entry.getTitle());
            comment.setText(entry.getComment());
        }

        protected Parent loadFXML(String fxmlResourceFile) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setController(this);

                return fxmlLoader.load(getClass().getResourceAsStream(fxmlResourceFile));
            } catch (IOException exc) {
                exc.printStackTrace();

                return null;
            }
        }

        protected String getYear(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return String.valueOf(calendar.get(Calendar.YEAR));
        }
    }

    class BookTypeUI extends BaseEntryTypeUI {
        private static final String FXML = "/fxml/show_book.fxml";

        @FXML
        private ImageView cover;

        @FXML
        private Label publisher, publishingYear;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                Book book = booksCRUDUseCase.loadBook(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(FXML));

                showEntry(entry);

                cover.setImage(new Image(book.getCoverUrl()));
                publisher.setText(book.getPublisher());
                publishingYear.setText(getYear(book.getPublishingYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    class CDAudioTypeUI extends BaseEntryTypeUI {
        private static final String FXML = "/fxml/show_cdaudio.fxml";

        @FXML
        private ImageView cover;

        @FXML
        private Label label, releaseYear;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                CDAudio cdAudio = cdAudiosCRUDUseCase.loadCDAudio(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(FXML));

                showEntry(entry);

                cover.setImage(new Image(cdAudio.getCoverUrl()));
                label.setText(cdAudio.getLabel());
                releaseYear.setText(getYear(cdAudio.getReleaseYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    class CDVideoTypeUI extends BaseEntryTypeUI {
        private static final String FXML = "/fxml/show_cdvideo.fxml";

        @FXML
        private ImageView cover;

        @FXML
        private Label releaseYear;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                CDVideo cdVideo = cdVideosCRUDUseCase.loadCDVideo(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(FXML));

                showEntry(entry);

                cover.setImage(new Image(cdVideo.getCoverUrl()));
                releaseYear.setText(getYear(cdVideo.getReleaseYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public UI(AuthenticateUseCase authenticateUseCase, CategoriesCRUDUseCase categoriesCRUDUseCase,
              EntriesCRUDUseCase entriesCRUDUseCase, BooksCRUDUseCase booksCRUDUseCase,
              CDAudiosCRUDUseCase cdAudiosCRUDUseCase, CDVideosCRUDUseCase cdVideosCRUDUseCase,
              ChangeMyPasswordUseCase changeMyPasswordUseCase, CommentsCRUDUseCase commentsCRUDUseCase,
              CreateFirstFamilyAdministratorUseCase createFirstFamilyAdministratorUseCase,
              FamilyMembersCRUDUseCase familyMembersCRUDUseCase, SaveLoadSessionsUseCase saveLoadSessionsUseCase,
              ViewCategoryUseCase viewCategoryUseCase, Map<String, EntryTypeUI> entryTypesUIs) {
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

        this.entryTypesUIs = entryTypesUIs;
        this.entryTypesUIs.put(Book.TYPE_ID, new BookTypeUI());
        this.entryTypesUIs.put(CDAudio.TYPE_ID, new CDAudioTypeUI());
        this.entryTypesUIs.put(CDVideo.TYPE_ID, new CDVideoTypeUI());
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
        goToHome(null);
    }

    public void goToHome(Category category) {
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
            currentCategoryHome = category == null ? categoriesCRUDUseCase.loadRootCategory(currentFamilyMember) : category;

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
                goToShowEntry(categoryElement.getEntry());
            }
        }
    }

    public void goToEditEntry() {

    }

    public void goToComments() {

    }

    public void goBackFromShowEntry() {
        goToHome(currentCategoryHome);
    }

    public void goToAddToFavorites() {

    }

    public void goToAddToCollection() {

    }

    public void goToShowEntry(Entry entry) {
        stage.setScene(loadFXML(SHOW_ENTRY_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        EntryTypeUI entryTypeUI = entryTypesUIs.get(entry.getTypeId());
        entryTypeUI.showEntry(entry, whereToShowEntry);

        showStage();
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
        stage.setScene(loadFXML(USERS_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        familyMembersList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(FamilyMember familyMember, boolean empty) {
                super.updateItem(familyMember, empty);

                if (empty || familyMember == null)
                    setText(null);
                else {
                    setText(familyMember.getNickname() + " (" +
                            (familyMember.getRights() == Rights.FAMILY_ADMINISTRATOR ? "admin" : "member") + ")");
                }
            }
        });

        refreshFamilyMembersList();

        showStage();
    }

    private void refreshFamilyMembersList() {
        try {
            List<FamilyMember> familyMembers = familyMembersCRUDUseCase.loadAllFamilyMembers(currentFamilyMember);

            familyMembersList.itemsProperty().getValue().clear();
            familyMembersList.itemsProperty().getValue().addAll(familyMembers);
        } catch (RightsViolationException exc) {
            exc.printStackTrace();

            showMessage(exc.getMessage());
        }
    }

    public void goToCreateUser() {
        stage.setScene(loadFXML(NEW_USER_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        showStage();
    }

    public void goToRemoveUser() {
        FamilyMember familyMember = familyMembersList.getSelectionModel().getSelectedItem();

        if (familyMember != null) {
            if (familyMember.getId() != currentFamilyMember.getId()) {
                try {
                    familyMembersCRUDUseCase.removeFamilyMember(currentFamilyMember, familyMember.getId());

                    refreshFamilyMembersList();
                } catch (RightsViolationException exc) {
                    exc.printStackTrace();

                    showMessage(exc.getMessage());
                }
            } else
                showMessage("You cannot delete yourself");
        }
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

    public void onCreateFamilyMember() {
        try {
            String nickname = newUserNickname.getText();
            String password = newUserPassword.getText();

            Rights rights = newUserMember.isSelected() ? Rights.FAMILY_MEMBER : Rights.FAMILY_ADMINISTRATOR;

            familyMembersCRUDUseCase.createFamilyMember(currentFamilyMember, new FamilyMember(nickname, password, 0, rights));

            newUserNickname.clear();
            newUserPassword.clear();
            newUserMember.setSelected(true);

            showMessage("The user was successfully created");
        } catch (RightsViolationException exc) {
            exc.printStackTrace();

            showMessage(exc.getMessage());
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
