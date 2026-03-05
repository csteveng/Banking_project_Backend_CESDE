package hapibank;

import hapibank.domain.Customer;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello to the Banking APP!");

        Customer customer = new Customer(
                1L,
                "Juan",
                "Pérez",
                "juan@email.com",
                "password123",
                "3001234567",
                LocalDate.now()
        );
        System.out.println("Hello your data is: " + "\n" + customer);
    }
}
