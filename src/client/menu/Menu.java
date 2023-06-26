package client.menu;

import java.util.List;
import java.util.Scanner;

public class Menu {
    Scanner scanner;
    MenuHandler handler;

    public Menu(List<String> data) {
        this.scanner = new Scanner(System.in);
        handler = new MenuHandler(data);
    }

    public void start() {
       Operation operation = null;

       do {
               String[] userInput = scanner.nextLine().split(" ", 3);
               operation = Operation.valueOf(userInput[0].toUpperCase());
               if (Operation.EXIT.equals(operation)) break;

               handler.handle(operation, userInput);

       } while (true);


    }


}

