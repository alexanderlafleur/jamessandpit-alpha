/*
 * Created on Mar 12, 2005
 *
 */
package com.james.dao.test;

/**
 * @author James
 */
public class WalletDAOTest extends BaseTestCase {
    //
    // private static final int NUM_WALLETS = 3;
    //
    // private WalletDao dao;
    //
    // private Wallet wallet1;
    //
    // private Wallet wallet2;
    //
    // private Wallet wallet3;
    //
    // private Wallet wallets[] = new Wallet[NUM_WALLETS];
    //
    // public void setUp() throws Exception {
    // super.setUp();
    // dao = DaoFactory.newWalletDao();
    //
    // for (int i = 0; i < NUM_WALLETS; i++) {
    // wallets[i] = createWallet();
    // }
    // }
    //
    // protected void tearDown() throws Exception {
    // super.tearDown();
    //
    // for (int i = 0; i < wallets.length; i++) {
    // deleteWallet(wallets[i]);
    // }
    // }
    //
    // public void testDelete() throws NotFoundException, SQLException {
    // dao.delete(wallets[0]);
    //
    // Wallet foundWallet = dao.getWallet(wallets[0].getId());
    // assertTrue(foundWallet == null);
    // }
    //
    // public void testGetWallet() throws SQLException, NotFoundException {
    // Wallet foundWallet = dao.getWallet(wallets[0].getId());
    //
    // assertTrue(foundWallet != null);
    // }
    //
    // public void testGetWallets() throws SQLException, NotFoundException {
    // String NEW_DESCRIPTION = "NewDescription";
    //
    // List walletsBefore = dao.getWallets();
    //
    // Wallet newWallet = new Wallet(NEW_DESCRIPTION);
    // newWallet.setId(dao.insert(newWallet));
    //
    // List foundWallets = dao.getWallets();
    //
    // assertNotNull(foundWallets);
    //
    // assertTrue(walletsBefore.size() == foundWallets.size() - 1);
    //
    // // for (int i = 0; i < foundWallets.size(); i++) {
    // for (Iterator i = foundWallets.iterator(); i.hasNext();) {
    // Wallet foundWallet = (Wallet) i.next();
    //
    // boolean found = false;
    //
    // if (foundWallet.getId() == newWallet.getId()) {
    // found = true;
    // assertTrue(newWallet.getDescription().equals(
    // foundWallet.getDescription()));
    //
    // } else {
    //
    // for (Iterator j = walletsBefore.iterator(); j.hasNext();) {
    // Wallet before = (Wallet) j.next();
    //
    // if (before.getId() == foundWallet.getId()) {
    // found = true;
    //
    // assertTrue(before.getDescription().equals(
    // foundWallet.getDescription()));
    // }
    // }
    // }
    // assertTrue(found);
    //
    // }
    // }
    //
    // public void testInsert() throws SQLException {
    // createWallet();
    // }
    //
    // public void testUpdate() throws SQLException, NotFoundException {
    // String NEW_DESCRIPTION = "NewDescription";
    //
    // wallets[0].setDescription(NEW_DESCRIPTION);
    // dao.update(wallets[0]);
    //
    // Wallet newWallet = dao.getWallet(wallets[0].getId());
    //
    // assertTrue(newWallet.getId() == wallets[0].getId());
    // assertTrue(newWallet.getDescription().equals(
    // wallets[0].getDescription()));
    //
    // }

}