package com.ra11p0;

import com.ra11p0.frames.HomeFrame;

public class Core {
    private static final float BUILD = 0.45F;
    public static void main(String[] args) {
        HomeFrame instance = new HomeFrame("Receipt manager v." + BUILD, 300, 150);
        instance.build();
    }
    public static float getBUILD(){
        return BUILD;
    }
}
