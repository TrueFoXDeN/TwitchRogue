package io;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public final static Commands commands[] = {
            new Commands("moveup", _ -> {System.out.println("Hello World");})
    };

    private String mnenomic;
    private Command command;

    private Commands(String mnenomic, Commands command) {
        this.mnenomic = mnenomic;
    }

    public void fire() {
        command.fire();
    }

    public boolean match(String cmd) {
        return mnenomic.equalsIgnoreCase(cmd);
    }

    public interface Command {
        abstract void fire();
    }
}
