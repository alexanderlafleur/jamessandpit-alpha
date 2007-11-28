package com.james.ui;


import java.beans.PropertyEditorSupport;

import com.james.fileItems.Wallet;
import com.james.helper.WalletHelper;

public class WalletEditor extends PropertyEditorSupport {
    private WalletHelper helper;
    
    public String getAsText() {
        Wallet wallet = (Wallet) super.getValue();
        if (wallet == null)
            return null;
        return "" + wallet.getId();
    }

    public WalletHelper getHelper() {
        return helper;
    }

    public void setAsText(String id) {
        Wallet wallet = getHelper().load(Long.parseLong(id));

        super.setValue(wallet);
        if (wallet == null)
            throw new IllegalArgumentException("Unsupported wallet id [" + id
                    + "]");
    }

    public void setHelper(WalletHelper helper) {
        this.helper = helper;
    }
}