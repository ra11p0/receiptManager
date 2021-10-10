package com.ra11p0;

import com.ra11p0.frames.HomeFrame;

public class Main {

    public static void main(String[] args) {
        HomeFrame instance = new HomeFrame("Receipt manager.", 200, 100);
        instance.build();
    }
}
