package hibernate.model;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "EMPLOYEE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"first_name","last_name"})})
public class Employee {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "salary")
    private int salary;

    @Column(name = "PESEL", nullable = false, unique = true)
    private int pesel;

    @ElementCollection
    private List<String> phones = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id")
    Set<Address> address;

    public Set<Address> getAddress() {
        return address;
    }

    public void setAddress(Set<Address> address) {
        this.address = address;
    }

    public Employee() {}

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String first_name ) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String last_name ) {
        this.lastName = last_name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary( int salary ) {
        this.salary = salary;
    }

    public int getPesel() {
        return pesel;
    }

    public void setPesel(int pesel) {
        this.pesel = pesel;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public static Employee copyEmployee(Employee emp) {
        Employee person = new Employee();
        person.setAddress(new HashSet<>(emp.getAddress()));
        person.setLastName(emp.getLastName() + new Random().nextInt());
        person.setFirstName(emp.getFirstName());
        person.setPesel(new Random().nextInt());
        person.setSalary(emp.getSalary());
        return person;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", pesel=" + pesel +
                ", phones=" + phones +
                ", address=" + address +
                '}';
    }
}