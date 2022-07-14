package homework_5.homework.domain;

import java.util.Objects;

public class Customer {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private final String fullName;
    private final String email;

    public Customer(String fullName, String domain) {
        this.fullName = fullName;
        this.email = fullName.toLowerCase().replace(' ', '.') + domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(fullName, customer.fullName) &&
                Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, email);
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    //----------------------------------------------------------------------
    //------   Реализация ваших методов должна быть ниже этой линии   ------
    //----------------------------------------------------------------------

    public void printCustomerInfo() {
        System.out.println("             <--- CUSTOMER --->");
        System.out.println("+==========+==============================+");
        System.out.printf( "|   NAME   | %-28s |%n", fullName);
        System.out.println("+----------+------------------------------+");
        System.out.printf( "|   EMAIL  | %-28s |%n", email);
        System.out.println("+==========+==============================+");
    }

}
