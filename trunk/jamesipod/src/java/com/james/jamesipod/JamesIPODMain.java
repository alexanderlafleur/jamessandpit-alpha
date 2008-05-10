package com.james.jamesipod;

public class JamesIPODMain {
    public static final void main(String args[]) {
        new JamesIPODMain().start();
    }

    private void start() {
        JamesIPOD ipod = new JamesIPOD();

        ipod.wipeIpod();
        // ipod.createPlaylist();
        ipod.listContents();

        // ipod.addFile();
    }
}