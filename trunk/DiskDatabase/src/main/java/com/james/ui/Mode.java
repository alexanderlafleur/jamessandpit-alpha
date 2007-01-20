package com.james.ui;

import java.util.HashMap;
import java.util.Map;

public class Mode {

    public static final Mode CHANGE_DISK = new Mode("changeDisk", "changeDisk");

    public static final Mode CHANGE_WALLET = new Mode("changeWallet",
            "changeWallet");

    private static Map codeMap;
    static {
        codeMap = new HashMap();
        codeMap.put(CHANGE_WALLET.code, CHANGE_WALLET);
        codeMap.put(CHANGE_DISK.code, CHANGE_DISK);
    }

    public static Mode getMode(String code) {
        return (Mode) codeMap.get(code);
    }

    private String code;

    private String displayName;

    private Mode(String code, String name) {
        this.code = code;
        this.displayName = name;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Mode))
            return false;
        if (o == null)
            return false;
        return this.code.equals(((Mode) o).code);
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isChangeDisk() {
        return this.equals(CHANGE_DISK);
    }

    public boolean isChangeWallet() {
        return this.equals(CHANGE_WALLET);
    }

    public String toString() {
        return code;
    }
}
