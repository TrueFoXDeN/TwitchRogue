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

    private boolean match(String cmd) {
        return mnemonic.equalsIgnoreCase(cmd);
    }

    public static Commands checkCommand(String cmd) {
        return null;
    }

    @FunctionalInterface
    public interface Command {
        void fire();
    }
}
