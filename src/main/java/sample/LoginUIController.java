public class LoginUIController {
    @javafx.fxml.FXML
    private TextField user;
    @javafx.fxml.FXML
    private TextField password;
    @javafx.fxml.FXML
    private Button loginButton;

    public void initialize() {
    }

    public void initManager(final LoginManager loginManager) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String sessionID = authorize();
                if (sessionID != null) {
                    loginManager.authenticated(sessionID);
                }
            }
        });
    }

    /**
     * Check authorization credentials.
     * <p>
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private String authorize() {
        return
                "open".equals(user.getText()) && "sesame".equals(password.getText())
                        ? generateSessionID()
                        : null;
    }

    private static int sessionID = 0;

    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
}
