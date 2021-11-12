package com.ra11p0;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    final static String path = "https://api.github.com/repos/ra11p0/receiptManager/releases";
    public static void main(String[] args) throws Exception{
        float lastBuild = checkLatestBuild();
        if(lastBuild != getBuild()) {
            int choice = JOptionPane.showOptionDialog(null,
                    "New update to v." + lastBuild + " available. Current version: v." +
                            getBuild()+ ". Download update?", "Update!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new Object[]{"NO", "YES"},
                    "YES");
            if(choice == 1) {
                update();
            }
        }
        File file = new File("core.exe");
        if(!file.exists()){
            JOptionPane.showMessageDialog(null,
                    "The problem has occurred while running program! Try again!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            File versionFile = new File("version.info");
            if(!versionFile.delete()){
                JOptionPane.showMessageDialog(null,
                        "The problem has occurred while updating! Check if there is no instances of program in the background!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                getBuild();
                System.exit(0);
            }
            System.exit(0);
        }
        Runtime.getRuntime().exec(file.getAbsolutePath(), null, new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-8)));
    }
    private static float checkLatestBuild() throws Exception{
        String json = readUrl();
        JsonObject jsonObject = (JsonObject) new Gson().fromJson(json, JsonArray.class).get(0);
        return jsonObject.get("tag_name").getAsFloat();
    }
    private static URL getUrlToLastBuild() throws Exception{
        String json = readUrl();
        JsonObject jsonObject = (JsonObject) new Gson().fromJson(json, JsonArray.class).get(0);
        JsonObject asset = (JsonObject) jsonObject.get("assets").getAsJsonArray().get(0);
        return new URL(asset.get("browser_download_url").getAsString());
    }
    private static String readUrl() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(Main.path);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    private static void update() throws Exception {
        File oldCore = new File("core.exe");
        Label statusLabel = new Label("Downloading...");
        if(!oldCore.delete() && oldCore.exists()) {
            JOptionPane.showMessageDialog(null,
                    "The problem has occurred while updating! Check if there is no instances of program in the background!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                    );
            System.exit(0);
        }
        JFrame status = new JFrame();
        status.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        status.setSize(170, 75);
        status.setAlwaysOnTop(true);
        status.add(statusLabel);
        status.setVisible(true);
        URL buildUrl = getUrlToLastBuild();
        BufferedInputStream inputStream = new BufferedInputStream(buildUrl.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream("core.exe");
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        inputStream.close();
        File versionFile = new File("version.info");
        if(!versionFile.delete()){
            JOptionPane.showMessageDialog(null,
                    "The problem has occurred while updating! Check if there is no instances of program in the background!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            getBuild();
            System.exit(0);
        }
        Path path = Paths.get("version.info");
        byte[] strToBytes = String.valueOf(checkLatestBuild()).getBytes();
        Files.write(path, strToBytes);
        status.setVisible(false);
        status.dispose();
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
}
