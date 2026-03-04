package hapibank.domain;

public class Person {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String cellNumber;

    // constructor

    public Person(Long id, String name, String lastName, String email, String password, String cellNumber) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    // getters & setters


    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    // methods
    public Person changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = newPassword;
        return this;
    }

    public Person changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = newEmail;
        return this;
    }

    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + getFullName() + "', email='" + email + "'}";
    }
}


