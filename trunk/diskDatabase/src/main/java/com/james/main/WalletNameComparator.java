package com.james.main;

import java.util.Comparator;

import com.james.ui.dto.WalletDTO;

public class WalletNameComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		WalletDTO w1 = (WalletDTO) o1;
		WalletDTO w2 = (WalletDTO) o2;

		return w1.getDescription().compareTo(w2.getDescription());
	}
}
