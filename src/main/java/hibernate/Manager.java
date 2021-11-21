package hibernate;

import hibernate.model.Address;
import hibernate.model.Employee;
import hibernate.queries.Queries;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


class Manager {

    public static void main(String[] args) {

        System.out.println("Start");

        EntityManager entityManager = null;

        EntityManagerFactory entityManagerFactory = null;

        try {

            // FACTORY NAME HAS TO MATCHED THE ONE FROM PERSISTED.XML !!!
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-dynamic");

            entityManager = entityManagerFactory.createEntityManager();
            Session session = entityManager.unwrap(Session.class);

            //New transaction
            session.beginTransaction();

            // Create Employee
            Employee emp = createEmployee();

            // Save in First order Cache (not database yet)
            session.save(emp);

            Employee employee = session.get(Employee.class, emp.getId());
            if (employee == null) {
                System.out.println(emp.getId() + " not found! ");
            } else {
                System.out.println("Found " + employee);
            }
            //session.refresh(employee); //Error object not in DB yet!!

            System.out.println("Employee " + employee.getId() + " " + employee.getFirstName() + employee.getLastName());

            employee.setPhones(Arrays.asList("109212","12121"));
            Address add = new Address("street", "city", "1", "1", "2222");
            session.save(add);
            employee.setAddress(new HashSet<>(Collections.singletonList(add)));

            //emp.getAddress().setStreet(null); error!

            session.flush();// flush changes to DB

            changeFirstGuyToNowak(session);// Flush executed
            employee.setLastName("NowakSEC" + new Random().nextInt()); // No SQL needed

            for (int i = 1; i < 10; i++) {
                Employee em = Employee.copyEmployee(emp);
                session.save(em);
            }

            //old session
            Set<Employee> listInAddress = session.get(Address.class, 1).getEmployee();

            session.getTransaction().commit();
            session.clear();

            session.getTransaction().begin();

            //new session
            listInAddress = session.get(Address.class, 1).getEmployee();

            employee = session.get(Employee.class, emp.getId());
            session.refresh(employee); // force select on DB

            System.out.println(new Queries(session).getThemAll().stream().map(a -> a.toString()).collect(Collectors.joining()));

            System.out.println("Done");

            //Commit transaction to database
            session.getTransaction().commit();

            session.getTransaction().begin();
            //session.delete(emp); ERROR remove detached instance
            emp = createEmployee();
            emp.setId(1);

            // Save in First order Cache (not database yet)
            session.save(emp);

            session.getTransaction().commit();

            System.out.println(new Queries(session).getAllEmployeeByPage(1));

            session.close();

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
        } finally {

        }

    }

    private static Employee createEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Jan");
        emp.setLastName("Polak" + new Random().nextInt());
        emp.setSalary(100);
        emp.setPesel(new Random().nextInt());
        return emp;
    }

    static void changeFirstGuyToNowak(Session session) {

        // Force synchronization
        List<Employee> employees = new Queries(session).getEmployeeByName("Polak");

        employees.get(0).setLastName("NowakPRE" + new Random().nextInt());

    }

}