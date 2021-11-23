package com.ra11p0;

import com.ra11p0.frames.Init;
import com.sun.xml.internal.ws.api.ResourceLoader;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class Core {
    public static final float BUILD = 0.5553F;
    public static final String whatsNew = "What's new in build v." + BUILD + ":\n"+
            "-performance fix\n-(in search result pane) 0 price fix\n-(in search result pane) fixed editing items";
    public static void main(String[] args) throws Exception{
        //check charset compatibility
        if(!Charset.defaultCharset().equals(StandardCharsets.UTF_8))
            JOptionPane.showMessageDialog(null,
                    "Charset " + Charset.defaultCharset() + " may occur incompatible with some languages.",
                    "Incompatible charset warning!",
                    JOptionPane.WARNING_MESSAGE
                    );
        //check version.info file
        if(BUILD != getBuild() && !Arrays.asList(args).contains("-passVersionCheck")) {
            File versionFile = new File("version.info");
            versionFile.delete();
            JOptionPane.showMessageDialog(null,
                    "Error while updating. " +
                            "Ensure, that there is no instances of application  running in the background. " +
                            "We will try to fix it.",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
        //check files integrity
        ArrayList<File> missingFiles = checkFiles();
        if(missingFiles != null){
            JOptionPane.showMessageDialog(null, "Some files are missing. Missing files: " + missingFiles, "Error!", JOptionPane.ERROR_MESSAGE);
            if(missingFiles.contains(new File("updater.exe"))) extractUpdater();
            if(missingFiles.contains(new File("settings.xml"))) extractDefaultSettings();
        }
        Properties settingsProp = new Properties();
        InputStream is = new FileInputStream("settings.xml");
        settingsProp.loadFromXML(is);
        is.close();
        if(settingsProp.getProperty("version") != null && Float.parseFloat(settingsProp.getProperty("version")) < BUILD){
            extractUpdater();
            JOptionPane.showMessageDialog(null, whatsNew);
        }
        //run init
        new Init("Receipt manager v." + BUILD);
    }
    private static float getBuild() throws Exception {
        try {
            Path path = Paths.get("version.info");
            String read = Files.readAllLines(path).get(0);
            return Float.parseFloat(read);
        }catch (Exception ex){
            Path path = Paths.get("version.info");
            byte[] strToBytes = "0.0".getBytes();
            Files.write(path, strToBytes);
            getBuild();
        }
        return 0;
    }
    private static ArrayList<File> checkFiles() throws IOException {
        File temp = new File("res/.temp");
        //*****
        File version = new File("version.info");
        File settings = new File("settings.xml");
        File updater = new File("updater.exe");
        File resCatalog = new File("res");
        File receipts = new File("res/receipts.json");
        ArrayList<File> files = new ArrayList<>(Arrays.asList(version, settings, updater, receipts));
        ArrayList<File> catalogs = new ArrayList<>(Arrays.asList(resCatalog));
        ArrayList<File> missingFiles = new ArrayList<>();
        for(File file : catalogs) if(!file.exists()) missingFiles.add(file);
        for(File file : files) if(!file.exists()) missingFiles.add(file);
        if(missingFiles.size() == 0) return null;
        for(File file : missingFiles){
            if(catalogs.contains(file)) file.mkdir();
            else file.createNewFile();
        }
        if(temp.exists()) {
            try {
                FileUtils.cleanDirectory(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            temp.delete();
        }
        return missingFiles;
    }
    private static void extractUpdater() throws IOException {
        File targetFile = new File("updater.exe");
        java.nio.file.Files.copy(
                Objects.requireNonNull(ResourceLoader.class.getResourceAsStream("/updater.exe")),
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
    private static void extractDefaultSettings() throws IOException {
        File targetFile = new File("settings.xml");
        java.nio.file.Files.copy(
                Objects.requireNonNull(ResourceLoader.class.getResourceAsStream("/defaultSettings.xml")),
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
