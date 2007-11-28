package com.james.ui.swing.main;

import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.MP3Info;
import com.james.ui.swing.node.WalletInfo;

public class MP3InfoWrapper extends BaseInfoWrapper {

	public MP3InfoWrapper(String id, String name, MP3DTO mp3DTO,
			BaseNodeHelper nodeHelper) {
		super(new MP3Info(id, name, mp3DTO, nodeHelper), nodeHelper);
	}

	public WalletInfo convertToPartialTree() {
		return null;
	}
	
	public String toString() {
		return getInfo().toString();
	}
}
