package env;

public class Suite {

    private static Suite instance;

    public static Suite getInstance() {
        if (instance == null) {
            instance = new Suite();
        }
        return instance;
    }

    private Suite() {
        super();
    }

    // --- Suite-level state ---
    private TestState currentTestState;

    public TestState getCurrentTestState() {
        return currentTestState;
    }

    public void setCurrentTestState(TestState currentTestState) {
        this.currentTestState = currentTestState;
    }
}
