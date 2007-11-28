package com.james.dao;

import java.util.List;

import com.james.fileItems.Wallet;

public interface WalletDao {

    public abstract void create(Wallet wallet);

    public abstract void delete(Wallet wallet);

    public abstract Wallet findWallet(long id);

    public abstract List<Wallet> load();

    public abstract Wallet load(long walletId);

    public abstract List<Wallet> search(String label);

    public abstract void update(Wallet wallet);

    public abstract Wallet loadByDescription(String walletName);

}