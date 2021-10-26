package com.ra11p0;

import com.ra11p0.frames.HomeFrame;

public class Core {
    private static final float BUILD = 0.53F;
    public static void main(String[] args) throws Exception{
        HomeFrame instance = new HomeFrame("Receipt manager v." + BUILD, 400, 150);
        //instance.build();
    }
}
