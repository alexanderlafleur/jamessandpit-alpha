package com.james.calendar.util;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderHelper {
    public static String read(String file) {
        FileInputStream fis = null;

        StringBuffer sb = new StringBuffer();

        try {
            fis = new FileInputStream(file);

            byte buff[] = new byte[1024];

            while (true) {
                int numBytes = fis.read(buff);

                if (numBytes <= 0) {
                    break;
                }
                sb.append(new String(buff, 0, numBytes));

            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return sb.toString();
    }

}
