package db_console.example;

import db_console.framework.Argument;
import db_console.framework.ArgumentAccessor;
import db_console.framework.Command;

import java.io.PrintStream;
import java.util.List;

public class SayHelloCommand implements Command {
    @Override
    public String getInvite() {
        return "say hello (no args)";
    }

    @Override
    public List<Argument> getArguments() {
        return List.of();
    }

    @Override
    public void run(PrintStream out, ArgumentAccessor argumentAccessor) {
        out.println("Hello!");
    }
}
