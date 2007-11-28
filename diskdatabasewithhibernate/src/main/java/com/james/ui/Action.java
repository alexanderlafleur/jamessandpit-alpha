package com.james.ui;

import java.util.HashMap;
import java.util.Map;

public class Action {
    private static Map<String, Action> actionMap;

    public static final Action CHANGE_DISK = new Action("changeDisk");

    public static final Action CHANGE_WALLET = new Action("changeWallet");

    public static final Action DELETE_DISK = new Action("deleteDisk");

    public static final Action PARENT_DIR = new Action("parentDir");

    public static final Action SELECT_DIR = new Action("selectDir");

    public static final Action SELECT_FILE = new Action("selectFile");

    static {
        actionMap = new HashMap<String, Action>();
        actionMap.put(CHANGE_WALLET.name, CHANGE_WALLET);
        actionMap.put(CHANGE_DISK.name, CHANGE_DISK);
        actionMap.put(DELETE_DISK.name, DELETE_DISK);
        actionMap.put(SELECT_FILE.name, SELECT_FILE);
        actionMap.put(SELECT_DIR.name, SELECT_DIR);
        actionMap.put(PARENT_DIR.name, PARENT_DIR);
    }

    public static Action getAction(String code) {
        return (Action) actionMap.get(code);
    }

    String name;

    private Action(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Action)) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return this.name.equals(((Action) o).name);
    }

    public String getName() {
        return name;
    }

    public boolean isChangeDisk() {
        return this.equals(Action.CHANGE_DISK);
    }

    public boolean isChangeWallet() {
        return this.equals(Action.CHANGE_WALLET);
    }

    public boolean isDeleteDisk() {
        return this.equals(Action.DELETE_DISK);
    }

    public boolean isParentDir() {
        return this.equals(Action.PARENT_DIR);
    }

    public boolean isSelectDir() {
        return this.equals(Action.SELECT_DIR);
    }

    public boolean isSelectFile() {
        return this.equals(Action.SELECT_FILE);
    }

    @Override
    public String toString() {
        // return "Action[" + name + "]";
        // so that JSTL returns a consist value:
        return name;
    }

}