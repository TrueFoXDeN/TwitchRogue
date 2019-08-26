package io;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public final static Commands commands[] = {
            new Commands("moveup", () -> {System.out.println("Hello World");})
    };

    private String mnenomic;
    private Command command;

    private Commands(String mnenomic, Command command) {
        this.mnenomic = mnenomic;
        this.command = command;
    }

    public void fire() {
        command.fire();
    }

    public boolean match(String cmd) {
        return mnenomic.equalsIgnoreCase(cmd);
    }

    @FunctionalInterface
    public interface Command {
        void fire();
    }
}
