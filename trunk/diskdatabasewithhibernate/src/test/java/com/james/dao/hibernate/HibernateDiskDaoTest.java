package com.james.dao.hibernate;

import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.fileItems.Dir;
import com.james.fileItems.Disk;

public class HibernateDiskDaoTest extends TestCase {
    private HibernateDiskDao diskDao;

    // protected static final FileSystemXmlApplicationContext CONTEXT = new
    // FileSystemXmlApplicationContext(
    // new String[] { "dao/hibernate/applicationContextTest.xml" });

    protected static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "dao/hibernate/applicationContextTest.xml" });

    @Override
    protected void setUp() throws Exception {
        diskDao = (HibernateDiskDao) CONTEXT.getBean("diskDao");
    }

    public void atestSave() {
        String dirRootLabel = "DirRoot";
        String diskLabel = "DiskLabel";

        Dir root = new Dir();
        root.setDirs(new HashSet());
        root.setFiles(new HashSet());
        root.setMp3s(new HashSet());
        root.setName(dirRootLabel);

        Disk disk = new Disk();
        disk.setLabel(diskLabel);

        disk.setRoot(root);

        diskDao.create(disk);

        List results = diskDao.search(diskLabel);

        Disk found = (Disk) results.get(0);

        System.out.println(found);
    }
}
