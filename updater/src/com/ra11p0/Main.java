package com.ra11p0;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class Main {
    final static String path = "https://api.github.com/repos/ra11p0/receiptManager/releases";
    public static void main(String[] args) throws Exception{
        float lastBuild = checkLatestBuild();
        if(lastBuild != Core.getBUILD()) {
            Object choice = JOptionPane.showOptionDialog(null, "New update to v." + lastBuild + " available. Download update?", "Update!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"NO", "YES"}, "YES");
            if((Integer)choice == 1) {
                update();
            }
        }
        Core.main(new String[]{});
    }
    private static float checkLatestBuild() throws Exception{
        String json = readUrl(path);
        JsonObject jsonObject = (JsonObject) new Gson().fromJson(json, JsonArray.class).get(0);
        return jsonObject.get("tag_name").getAsFloat();
    }
    private static URL getUrlToLastBuild() throws Exception{
        String json = readUrl(path);
        JsonObject jsonObject = (JsonObject) new Gson().fromJson(json, JsonArray.class).get(0);
        JsonObject asset = (JsonObject) jsonObject.get("assets").getAsJsonArray().get(0);
        return new URL(asset.get("browser_download_url").getAsString());
    }
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
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
        Label statusLabel = new Label("Downloading...");
        JFrame status = new JFrame();
        status.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        status.setSize(170, 75);
        status.setAlwaysOnTop(true);
        status.add(statusLabel);
        status.setVisible(true);
        URL buildUrl = getUrlToLastBuild();
        BufferedInputStream inputStream = new BufferedInputStream(buildUrl.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream("Core_temp.exe");
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        inputStream.close();
        File coreTemp = new File("Core_temp.exe");
        coreTemp.renameTo(new File("Core.exe"));
        status.setVisible(false);
        status.dispose();
    }
}
