package com.james.ui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.james.fileItems.Wallet;
import com.james.helper.WalletHelper;
import com.james.ui.dto.WalletDTO;

public class WalletController extends BaseController implements Controller {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * @param wallet
     */
    private void delete(Wallet wallet) {
        WalletHelper walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");

        walletHelper.delete(wallet.getId());
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered WalletController.handleRequest()");

        String mode = request.getParameter("mode");

        String walletId = request.getParameter("walletId");

        Map model = new HashMap();
        ModelAndView modelAndView;

        if ("edit".equals(mode)) {
            Wallet wallet = loadWallet(walletId);
            model.put("wallet", wallet);
            modelAndView = new ModelAndView("editWallet", model);

        } else if ("view".equals(mode)) {
            Wallet wallet = loadWallet(walletId);
            model.put("wallet", wallet);

            modelAndView = new ModelAndView("viewWallet", model);

        } else if ("new".equals(mode)) {
            modelAndView = new ModelAndView("newWallet");

        } else if ("delete".equals(mode)) {
            Wallet wallet = loadWallet(walletId);

            delete(wallet);

            // Refresh list of wallets
            List wallets = loadWallets();
            model.put("wallets", wallets);

            modelAndView = new ModelAndView("manageWallets", model);

        } else if ("save".equals(mode)) {
            WalletHelper walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");

            String description = request.getParameter("walletDescription");

            // Save
            WalletDTO wallet = new WalletDTO();
            wallet.setDescription(description);
            walletHelper.create(wallet);

            // Refresh list of wallets
            List wallets = loadWallets();
            model.put("wallets", wallets);

            modelAndView = new ModelAndView("manageWallets", model);

        } else {
            // Refresh list of wallets
            List wallets = loadWallets();
            model.put("wallets", wallets);

            modelAndView = new ModelAndView("manageWallets", model);
        }

        return modelAndView;
    }

    private Wallet loadWallet(String walletId) {
        WalletHelper walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");

        return walletHelper.load(Integer.parseInt(walletId));
    }

    private List loadWallets() {
        WalletHelper walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");

        return walletHelper.load();

    }

}