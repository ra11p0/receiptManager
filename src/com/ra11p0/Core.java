package com.ra11p0;

import com.ra11p0.frames.HomeFrame;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Core {
    public static final float BUILD = 0.54F;
    public static void main(String[] args) throws Exception{
        if(BUILD != getBuild() && !Arrays.asList(args).contains("-passVersionCheck")) {
            File versionFile = new File("version.info");
            versionFile.delete();
            JOptionPane.showMessageDialog(null,
                    "Error while updating. " +
                            "Ensure, that there is no instances of application  running in the background. " +
                            "Try launching application again.",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        HomeFrame instance = new HomeFrame("Receipt manager v." + BUILD, 400, 150);
        //instance.build();
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
