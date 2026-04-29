package application;

import application.config.AppConfig;
import application.view.MainMenuView;

public class Main {
    public static void main(String[] args) {
        MainMenuView menu = AppConfig.createMenuApp();
        menu.showMenu();
    }
}
