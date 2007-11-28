/*
 * Created on Mar 13, 2005
 *
 */
package com.james.dao.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.james.dao.DirDao;
import com.james.fileItems.Dir;
import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class DirDAOTest {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
    private WalletHelper walletHelper;
    private DiskHelper diskHelper;
    private DirHelper dirHelper;
    private DiskFileHelper diskFileHelper;

    @Test
    public void doesDirDaoCreateDirectory() {
        // DirDao dao = (DirDao) CONTEXT.getBean("dirDao");
        // Dir dir = new Dir(-1, "dir", null);
        // dao.create(dir);
        //        
        // walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
        // diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
        // dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
        // diskFileHelper = (DiskFileHelper) CONTEXT.getBean("diskFileHelper");
        // WalletDTO wallet = new WalletDTO();
        // wallet.setDescription("DirDAOTest");
        // dirDao.safeCreate(wallet);
        // DiskDTO disk = new DiskDTO();
        // disk.setLabel("test");
        // safeCreate(disk);
        // wallet.addDisk(disk);
        // walletHelper.update(wallet);
        // DirDTO root = new DirDTO();
        // root.setName("root");
        // dirHelper.createDTO(root, null);
        // disk.setRoot(root);
        // diskHelper.update(disk);
        // DirDTO dir = new DirDTO();
        // dir.setName("dir");
        // dir.setParent(root);
        // dirHelper.createDTO(dir, null);
        // root.addDir(dir);
        // dirHelper.updateDTO(root, null);
        // DirDTO c1 = new DirDTO();
        // c1.setName("c1");
        // c1.setParent(dir);
        // dir.addDir(c1);
        // dirHelper.createDTO(dir, null);
        // // DiskFile file = new DiskFile();
        // //
        // // file.setName("test");
        // // file.setPath("test");
        // // file.setSize(1);
        // // file.setType("test");
        // //
        // // dir.addFile(file);
        // walletHelper.update(wallet);
        // diskHelper.update(disk);
        // dirHelper.updateDTO(root, null);
        // dirHelper.updateDTO(dir, null);
        // DirDTO dir1 = dirHelper.loadDTO(Long.parseLong(root.getId()));
        // print(dir1, "");
        // System.out.println("printing parent");
        // DirDTO l2 = dirHelper.loadDTO(Long.parseLong(c1.getId()));
        // print(l2, "-");
    }
}