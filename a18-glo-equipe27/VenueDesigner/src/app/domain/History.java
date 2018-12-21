package app.domain;

import java.util.ArrayList;

public final class History {
    private final static int MAX_SIZE = 10;
    private final ArrayList<String> history = new ArrayList<>();
    private int current = -1;

    public void add(String version) {
        if (history.size() == MAX_SIZE) {
            history.remove(0);
            current--;
        }
        if (current > -1 && version.equals(history.get(current))) {
            return;
        }
        if (current > -1 && !version.equals(history.get(history.size() - 1))) {
            for (int i = current + 1; i < history.size(); i++) {
                history.remove(i);
            }
        }
        history.add(version);
        current++;
    }

    public String undo() {
        if (current == 0) {
            return history.get(0);
        }
        current--;
        return history.get(current);
    }

    public String redo() {
        if (current == MAX_SIZE - 1) {
            return history.get(current - 1);
        }
        current++;
        return history.get(current);
    }

    public boolean canUndo() {
        return current > 0;
    }

    public boolean canRedo() {
        return !history.isEmpty() && current < history.size() - 1;
    }
}
