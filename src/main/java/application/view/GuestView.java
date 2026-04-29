package application.view;

import application.util.FormValidationUtil;

public class GuestView {

    public void showMenu() {
            System.out.println("\n--- REGISTRO DE CLIENTE ---");
            String name = FormValidationUtil.validateString("Ingrese nombre completo: ");
            String id = FormValidationUtil.validateString("Ingrese número de identificación: ");
            boolean active = FormValidationUtil.validateBoolean("¿El cliente está activo? (true/false): ");

            System.out.println("Cliente registrado:");
            System.out.println("Nombre: " + name);
            System.out.println("ID: " + id);
            System.out.println("Activo: " + active);
        }



}
