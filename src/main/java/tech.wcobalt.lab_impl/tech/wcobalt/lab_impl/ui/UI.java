package tech.wcobalt.lab_impl.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.wcobalt.lab_impl.app.*;
import tech.wcobalt.lab_impl.domain.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    private static final String COMMENTS_FXML = "/fxml/comments.fxml";

    private static final String COMMENT_FXML = "/fxml/comment.fxml";

    private static final String CREATE_ENTRY_FXML = "/fxml/create_entry.fxml";

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

        void showEntryWhenCreating(VBox where);

        void onCreateEntry(Entry entry);
    }

    private Map<String, EntryTypeUI> entryTypesUIs;

    //model
    private Session session;
    private FamilyMember currentFamilyMember;
    private Category currentCategoryHome;
    private Entry currentEntryToShow;

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
    private VBox whereToShowEntry, commentsBlock;

    @FXML
    private HBox createEntrySelectTypeBlock;

    @FXML
    private VBox createEntrySpecificityBlock;

    @FXML
    private TextField createEntryTitle, createEntryAuthor;

    @FXML
    private TextArea createEntryComment;

    private ToggleGroup createEntryToggleGroup;

    private FileChooser fileChooser;

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

    public class BookTypeUI extends BaseEntryTypeUI {
        private static final String SHOW_FXML = "/fxml/show_book.fxml";
        private static final String CREATE_FXML = "/fxml/create_book.fxml";

        @FXML
        private ImageView cover, coverInput;

        @FXML
        private TextField publishingYearInput, publisherInput;

        @FXML
        private Label publisher, publishingYear;

        private String coverUrl;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                Book book = booksCRUDUseCase.loadBook(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(SHOW_FXML));

                showEntry(entry);

                cover.setImage(new Image(book.getCoverUrl()));
                publisher.setText(book.getPublisher());
                publishingYear.setText(getYear(book.getPublishingYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        @Override
        public void showEntryWhenCreating(VBox where) {
            where.getChildren().addAll(loadFXML(CREATE_FXML));
        }

        @Override
        public void onCreateEntry(Entry entry) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(publishingYearInput.getText()), Calendar.JANUARY, 1);
                Book book = new Book(entry.getAddDate(), entry.getComment(), entry.getAuthor(), -1, entry.getCategory(),
                        entry.getTitle(), calendar.getTime(), publisherInput.getText(), coverUrl);

                booksCRUDUseCase.createBook(currentFamilyMember, book);
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        public void onCoverChoose() {
            try {
                File result = fileChooser.showOpenDialog(stage);

                if (result != null) {
                    coverUrl = result.toURI().toURL().toExternalForm();
                    coverInput.setImage(new Image(coverUrl));
                }
            } catch (MalformedURLException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public class CDAudioTypeUI extends BaseEntryTypeUI {
        private static final String SHOW_FXML = "/fxml/show_cdaudio.fxml";
        private static final String CREATE_FXML = "/fxml/create_cdaudio.fxml";

        @FXML
        private ImageView cover, coverInput;;

        @FXML
        private TextField releaseYearInput, labelInput;

        @FXML
        private Label label, releaseYear;

        private String coverUrl;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                CDAudio cdAudio = cdAudiosCRUDUseCase.loadCDAudio(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(SHOW_FXML));

                showEntry(entry);

                cover.setImage(new Image(cdAudio.getCoverUrl()));
                label.setText(cdAudio.getLabel());
                releaseYear.setText(getYear(cdAudio.getReleaseYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        @Override
        public void showEntryWhenCreating(VBox where) {
            where.getChildren().addAll(loadFXML(CREATE_FXML));
        }

        @Override
        public void onCreateEntry(Entry entry) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(releaseYearInput.getText()), Calendar.JANUARY, 1);
                CDAudio cdAudio = new CDAudio(entry.getAddDate(), entry.getComment(), entry.getAuthor(), -1, entry.getCategory(),
                        entry.getTitle(), calendar.getTime(), labelInput.getText(), coverUrl);

                cdAudiosCRUDUseCase.createCDAudio(currentFamilyMember, cdAudio);
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        public void onCoverChoose() {
            try {
                File result = fileChooser.showOpenDialog(stage);

                if (result != null) {
                    coverUrl = result.toURI().toURL().toExternalForm();
                    coverInput.setImage(new Image(coverUrl));
                }
            } catch (MalformedURLException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public class CDVideoTypeUI extends BaseEntryTypeUI {
        private static final String SHOW_FXML = "/fxml/show_cdvideo.fxml";
        private static final String CREATE_FXML = "/fxml/create_cdvideo.fxml";

        @FXML
        private ImageView cover, coverInput;

        @FXML
        private TextField releaseYearInput;

        @FXML
        private Label releaseYear;

        private String coverUrl;

        @Override
        public void showEntry(Entry entry, VBox where) {
            try {
                CDVideo cdVideo = cdVideosCRUDUseCase.loadCDVideo(currentFamilyMember, entry.getId());

                where.getChildren().addAll(loadFXML(SHOW_FXML));

                showEntry(entry);

                cover.setImage(new Image(cdVideo.getCoverUrl()));
                releaseYear.setText(getYear(cdVideo.getReleaseYear()));
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        @Override
        public void showEntryWhenCreating(VBox where) {
            where.getChildren().addAll(loadFXML(CREATE_FXML));
        }

        @Override
        public void onCreateEntry(Entry entry) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(releaseYearInput.getText()), Calendar.JANUARY, 1);
                CDVideo cdVideo = new CDVideo(entry.getAddDate(), entry.getComment(), entry.getAuthor(), -1, entry.getCategory(),
                        entry.getTitle(), calendar.getTime(), coverUrl);

                cdVideosCRUDUseCase.createCDVideo(currentFamilyMember, cdVideo);
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        public void onCoverChoose() {
            try {
                File result = fileChooser.showOpenDialog(stage);

                if (result != null) {
                    coverUrl = result.toURI().toURL().toExternalForm();
                    coverInput.setImage(new Image(coverUrl));
                }
            } catch (MalformedURLException exc) {
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

        fileChooser = new FileChooser();
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

            element.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    goToCategoryElement();
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

    public class CommentUI {
        private Comment comment;

        @FXML
        private Label commentContent, commentAuthor;

        public CommentUI(Comment comment) {
            this.comment = comment;
        }

        public void onRemoveComment() {
            try {
                commentsCRUDUseCase.removeComment(currentFamilyMember, comment.getId());

                refreshComments();
            } catch (RightsViolationException exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }

        public void init() {
            commentContent.setText(comment.getContent());

            FamilyMember author = familyMembersCRUDUseCase.loadFamilyMember(comment.getAuthor());

            commentAuthor.setText(author.getNickname());
        }
    }

    public void goToComments() {
        if (currentEntryToShow != null) {
            stage.setScene(loadFXML(COMMENTS_FXML, Arrays.asList(
                    getClass().getResource(MODENA_CSS).toExternalForm(),
                    getClass().getResource(MAIN_CSS).toExternalForm()
            )));

            refreshComments();

            showStage();
        }
    }

    private void refreshComments() {
        if (currentEntryToShow != null) {
            try {
                commentsBlock.getChildren().clear();

                List<Comment> comments = commentsCRUDUseCase.loadCommentsByEntry(currentFamilyMember, currentEntryToShow.getId());

                for (Comment comment : comments) {
                    CommentUI commentUI = new CommentUI(comment);
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setController(commentUI);

                    VBox commmentVbox = fxmlLoader.load(getClass().getResourceAsStream(COMMENT_FXML));

                    commentUI.init();

                    commentsBlock.getChildren().add(commmentVbox);
                }
            } catch (Exception exc) {
                exc.printStackTrace();

                showMessage(exc.getMessage());
            }
        }
    }

    public void goToCreateComment() {
        if (currentEntryToShow != null) {
            String content = askQuestion("New comment", "Message");

            if (content != null) {
                try {
                    commentsCRUDUseCase.createComment(currentFamilyMember, content, currentEntryToShow.getId());

                    refreshComments();
                } catch (RightsViolationException exc) {
                    exc.printStackTrace();

                    showMessage(exc.getMessage());
                }
            }
        }
    }

    public void goBackFromComments() {
        goToShowEntry(currentEntryToShow);
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

        currentEntryToShow = entry;

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
        stage.setScene(loadFXML(CREATE_ENTRY_FXML, Arrays.asList(
                getClass().getResource(MODENA_CSS).toExternalForm(),
                getClass().getResource(MAIN_CSS).toExternalForm()
        )));

        boolean first = true;

        createEntryToggleGroup = new ToggleGroup();

        createEntryToggleGroup.selectedToggleProperty().addListener((a, oldValue, newValue) -> {
            createEntrySpecificityBlock.getChildren().clear();

            entryTypesUIs.get(newValue.getUserData()).showEntryWhenCreating(createEntrySpecificityBlock);
        });

        for (String typeId : entryTypesUIs.keySet()) {
            RadioButton radioButton = new RadioButton(typeId);
            radioButton.setToggleGroup(createEntryToggleGroup);
            radioButton.setUserData(typeId);

            createEntrySelectTypeBlock.getChildren().add(radioButton);

            if (first) {
                radioButton.setSelected(true);

                first = false;
            }
        }

        showStage();
    }

    public void onCreateEntry() {
        String selectedTypeId = (String)createEntryToggleGroup.getSelectedToggle().getUserData();

        Entry entry = new Entry(new Date(), createEntryComment.getText(), createEntryAuthor.getText(),
                -1, currentCategoryHome.getId(), selectedTypeId, createEntryTitle.getText());

        entryTypesUIs.get(selectedTypeId).onCreateEntry(entry);

        showMessage("New entry was successfully created");

        goToHome(currentCategoryHome);
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
