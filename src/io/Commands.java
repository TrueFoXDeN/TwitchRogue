package io;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public final static Commands commands[] = {
            new Commands("moveup", () -> {System.out.println("Hello World");})
    };

    private String mnemonic;
    private Command command;

    private Commands(String mnenomic, Command command) {
        this.mnemonic = mnenomic;
        this.command = command;
    }

    public void fire() {
        command.fire();
    }

    public boolean match(String cmd) {
        return mnemonic.equalsIgnoreCase(cmd);
    }

    @FunctionalInterface
    public interface Command {
        void fire();
    }
}
