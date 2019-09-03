package io;

import java.util.ArrayList;
import java.util.List;

import static application.GameLoop.gHandler;

public class Commands {

    public final static Commands commands[] = {
            new Commands("moveup", () -> gHandler.movePlayer(0, -1)),
            new Commands("movedown", () -> gHandler.movePlayer(0, 1)),
            new Commands("moveRight", () -> gHandler.movePlayer(1, 0)),
            new Commands("moveLeft", () -> gHandler.movePlayer(-1, 0)),
            new Commands("usePotion", () -> {
                if(gHandler.getPlayer().getPotions().size() > 0) {
                    gHandler.getPlayer().getPotions().get(0).use();
                    gHandler.getPlayer().getPotions().remove(0);
                }
            })
    };

    private String mnemonic;
    private Command command;

    private Commands(String mnenomic, Command command) {
        this.mnemonic = mnenomic;
        this.command = command;
    }

    public void fire()
    {
        // TODO: remove debug logging
        System.out.println(String.format("Command %s was executed", mnemonic));
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
