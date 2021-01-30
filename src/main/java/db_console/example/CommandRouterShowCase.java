package db_console.example;

import db_console.framework.CommandHandlerImpl;
import db_console.framework.CommandRouter;

import java.util.List;
import java.util.Scanner;

public class CommandRouterShowCase {
    public static void main(String[] args) {
        new CommandRouter(
                List.of(
                        new GetDoublesCommand(),
                        new SayHelloCommand()
                ),
                new CommandHandlerImpl()
        ).handleCommands(new Scanner(System.in), System.out);
    }
}
