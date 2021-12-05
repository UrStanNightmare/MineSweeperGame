package ru.cft.focusstart.task3.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class DirectoryBuilder {
    private final static Logger log = LoggerFactory.getLogger(DirectoryBuilder.class.getName());
    private final File checkFile;
    private boolean created;

    public DirectoryBuilder(File file) {
        this.checkFile = file;
    }

    public boolean buildAndVerify() {
        try {
            if (!checkFile.exists()) {
                File subDir = checkFile.getParentFile();

                if (!subDir.exists()) {
                    boolean subDirsCreated = subDir.mkdirs();
                    if (!subDirsCreated) {
                        throw new IOException("Can't create sub directories for database storage");
                    }
                }

                checkFile.createNewFile();
                this.created = true;
            }
            return true;
        } catch (IOException e) {
            log.error("Problem with database file happened! {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean isFileCreated() {
        return this.created;
    }
}
