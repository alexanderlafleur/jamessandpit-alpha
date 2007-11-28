package com.james.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.james.fileItems.Dir;
import com.james.fileItems.Disk;
import com.james.fileItems.Wallet;
import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.Action;
import com.james.ui.ActionEditor;
import com.james.ui.DirEditor;
import com.james.ui.DiskEditor;
import com.james.ui.ExplorerForm;
import com.james.ui.WalletEditor;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.WalletDTO;

public class ExplorerController extends SimpleFormController {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    private WalletHelper walletHelper;

    private void changeDir(ExplorerForm form, DirDTO dir) {
        form.setDir(dir);

        form.setFiles(new ArrayList<DiskFileDTO>(dir.getFiles()));
        form.setDirs(new ArrayList<DirDTO>(dir.getDirs()));
    }

    private void changeDisk(ExplorerForm form, DiskDTO disk) {
        form.setDisk(disk);

        changeDir(form, disk.getRoot());
    }

    private void changeFile(ExplorerForm form, DiskFileDTO file) {

    }

    private void changeWallet(ExplorerForm form, WalletDTO wallet) {
        changeDisk(form, (DiskDTO) wallet.getDisks().iterator().next());

        form.setDisks(new ArrayList<DiskDTO>(wallet.getDisks()));

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ExplorerForm form = new ExplorerForm();

        WalletDTO wallet = loadFirstWallet();
        changeWallet(form, wallet);
        // form.setWallet(wallet);
        // form.setDisks(new ArrayList(wallet.getDisks()));
        //
        // Disk disk = (Disk) wallet.getDisks().iterator().next();
        // form.setDisk(disk);
        //
        // form.setDir(disk.getRoot());
        //
        // form.setDirs(new ArrayList(disk.getRoot().getDirs()));
        // form.setFiles(new ArrayList(disk.getRoot().getFiles()));

        return form;
    }

    private DirHelper getDirHelper() {
        if (dirHelper == null) {
            dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
        }

        return dirHelper;
    }

    private DiskHelper getDiskHelper() {
        if (diskHelper == null) {
            diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
        }

        return diskHelper;
    }

    private WalletHelper getWalletHelper() {
        if (walletHelper == null) {
            walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
        }

        return walletHelper;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);

        binder.registerCustomEditor(Action.class, new ActionEditor());

        WalletEditor walletEditor = new WalletEditor();
        walletEditor.setHelper(getWalletHelper());
        binder.registerCustomEditor(Wallet.class, walletEditor);

        DiskEditor diskEditor = new DiskEditor();
        diskEditor.setHelper(getDiskHelper());
        binder.registerCustomEditor(Disk.class, diskEditor);

        DirEditor dirEditor = new DirEditor();
        dirEditor.setHelper(getDirHelper());
        binder.registerCustomEditor(Dir.class, dirEditor);
    }

    private WalletDTO loadFirstWallet() {
        List wallets = getWalletHelper().load();

        if (wallets == null || wallets.size() == 0) {
            return null;
        } else {
            WalletDTO wallet = (WalletDTO) wallets.get(0);

            return wallet;
        }
    }

    private List loadWallets() {
        return getWalletHelper().load();
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object oForm, BindException errors) throws Exception {

        ExplorerForm form = (ExplorerForm) oForm;

        Action action = form.getAction();

        if (action.isChangeWallet()) {
            changeWallet(form, form.getWallet());

        } else if (action.isChangeDisk()) {
            changeDisk(form, form.getDisk());

        } else if (action.isSelectDir()) {
            changeDir(form, form.getDir());

        } else if (action.isSelectFile()) {
            changeFile(form, form.getFile());

        } else if (action.isDeleteDisk()) {
            getDiskHelper().delete(form.getDisk().getId());

        } else if (action.isParentDir()) {
            changeDir(form, form.getParent());

        } else {

        }

        return showForm(request, response, errors);
    }

    // private Dir getParent(Dir dir) {
    // return dirHelper.getParent(dir);
    // }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map referenceData = new HashMap();

        List wallets = loadWallets();
        referenceData.put("wallets", wallets);

        return referenceData;
    }
}