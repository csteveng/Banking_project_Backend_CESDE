package application.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class FormValidationUtil {


    private final static Scanner sc = new Scanner(System.in);

    public static int validateInt(String prompt){

        while(true){

            try{

                System.out.println(prompt);
                int value = sc.nextInt();
                sc.nextLine(); // Limpiar el buffer
                return value;

            }catch (InputMismatchException e){
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                sc.nextLine(); // Limpiar el buffer
            }
        }

    }


    public static double validateDouble(String prompt){

        while(true){

            try{

                System.out.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine(); // Limpiar el buffer
                return value;

            }catch (InputMismatchException e){
                System.out.println("Entrada no válida. Por favor, ingrese un número decimal.");
                sc.nextLine(); // Limpiar el buffer
            }
        }

    }

    public static boolean validateBoolean(String prompt){

        while(true){

            try{

                System.out.println(prompt);
                boolean value = sc.nextBoolean();
                sc.nextLine(); // Limpiar el buffer
                return value;

            }catch (InputMismatchException e){
                System.out.println("Entrada no válida. Por favor, ingrese un valor lógico.");
                sc.nextLine(); // Limpiar el buffer
            }
        }

    }


    public static String validateString(String prompt){
        while(true){
            System.out.println(prompt);
            String value = sc.nextLine().trim();
            if(!value.isEmpty()){
                return value;
            }
            System.out.println("Entrada no válida. El texto no puede estar vacío. Intente de nuevo.");
        }
    }
}
