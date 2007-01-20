package com.james.dao.test;

import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;

public class TestDiskHelper extends BaseTestCase {

	public void testDirCascade() {
		DiskDTO disk = new DiskDTO();
		disk.setLabel("TestDiskHelper");

		safeCreate(disk);

		DirDTO root = new DirDTO();
		root.setName("rootDir");

		safeCreate(root, null);

		disk.setRoot(root);

		DirDTO childDir = new DirDTO();
		childDir.setName("childDir");

		safeCreate(childDir, null);

		root.addDir(childDir);

		getDiskHelper().update(disk);

		// Now load the disk and we shouldn't see the child loaded
		DiskDTO loadedDisk = getDiskHelper().loadDTO(disk.getId());

		print(loadedDisk);
	}
}
