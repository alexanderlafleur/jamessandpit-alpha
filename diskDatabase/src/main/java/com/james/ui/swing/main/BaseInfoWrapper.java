package com.james.ui.swing.main;

import com.james.ui.swing.node.BaseInfo;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.WalletInfo;

public abstract class BaseInfoWrapper {
	private BaseNodeHelper helper;

	private BaseInfo info;

	public BaseInfoWrapper(BaseInfo info, BaseNodeHelper helper) {
		this.info = info;
		this.helper = helper;
	}

	public BaseNodeHelper getHelper() {
		return helper;
	}

	public BaseInfo getInfo() {
		return info;
	}

	public void setHelper(BaseNodeHelper helper) {
		this.helper = helper;
	}

	public void setInfo(BaseInfo info) {
		this.info = info;
	}

	public abstract String toString();
}
