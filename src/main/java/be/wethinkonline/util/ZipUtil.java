package be.wethinkonline.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ZipUtil {
    private static final Logger log;

    static { 
    	log = Logger.getLogger(ZipUtil.class.getName());
    }

    public void unzipArchive(File archive, File outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, outputDir);
            }
        } catch (Exception e) {
            log.log(Level.WARNING,"Errore extracting file " + archive, e);
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {


        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }
        
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()){
            createDir(outputFile.getParentFile());
        }

        log.log(Level.FINEST, "Extracting entry);");
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private void createDir(File dir) {
        log.log(Level.FINEST,"Creating"+dir.getName());
        if(!dir.mkdirs()) throw new RuntimeException("Can not create dir "+dir);
    }
}
