import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class Test {

    private static final String EMPLOYEES_FILE = "employees.dat";

    public static void main(String... args) {

        List<Employee> allEmployees = new ArrayList<>();
        List<Employee> itDepartmentEmployees = new ArrayList<>();
        List<Employee> financialEmployees = new ArrayList<>();


        Employee ben = new Employee("Ben", 35, 35);
        Employee lloyd = new Employee("Lloyd", 30, 30);
        Employee li = new Employee("Li", 20, 20);
        Employee melanie = new Employee("Melanie", 40, 40);

        itDepartmentEmployees.add(ben);
        itDepartmentEmployees.add(lloyd);

        financialEmployees.add(li);
        financialEmployees.add(melanie);


        Department itDepartment = new Department("IT Department", itDepartmentEmployees);
        Department financialDepartment = new Department("Financial Department", financialEmployees);

        allEmployees.addAll(itDepartment.getEmployees());
        allEmployees.addAll(financialDepartment.getEmployees());

        try {
            System.out.format(" Attempting to write allEmployees to file [%s] \n ", EMPLOYEES_FILE);
            writeEmployeesToFile(allEmployees, EMPLOYEES_FILE);
        } catch (IOException e) {
            System.out.println(" An error occurred while writing employees to file ");
            e.printStackTrace();
        }

        List<Employee> employeesFromFile = null;
        try {
            System.out.format(" Attempting to read employees from file [%s] \n ", EMPLOYEES_FILE);
            employeesFromFile = readEmployeesFromFile(EMPLOYEES_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(" An error occurred while reading  employees from file, file format is incorrect ");
            e.printStackTrace();
        }


        if (employeesFromFile != null) {
            System.out.println(" Employees read from file " + employeesFromFile);
            Optional<Employee> employeeWithMinimumWorkedHours = employeesFromFile.stream().max(Comparator.comparingInt(Employee::getWorkedHours));
            employeeWithMinimumWorkedHours.ifPresent(employee -> System.out
                    .println(" Employee with minimum worked hour is " + employee));

            Optional<Employee> employeeWithMinmalPayment =
                    employeesFromFile.stream().max(Comparator.comparingInt(Employee::getHourPaid));
            employeeWithMinmalPayment.ifPresent(employee -> System.out
                    .println(" Employee with maximum payed hours is " + employee));

        }



    }

    private static List<Employee> readEmployeesFromFile(String fileName)
            throws IOException, ClassNotFoundException {
        ObjectInputStream employeesInputStream = new ObjectInputStream(new FileInputStream(fileName));
        return (List<Employee>) employeesInputStream.readObject();
    }

    private static void writeEmployeesToFile(List<Employee> employees, String fileName) throws IOException {
        ObjectOutputStream employeesOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        employeesOutputStream.writeObject(employees);
    }


    static class Employee implements Serializable {
        private String name;
        private int workedHours;
        private int hourPaid;

        Employee(String name, int workedHours, int hourPaid) {
            this.name = name;
            this.workedHours = workedHours;
            this.hourPaid = hourPaid;
        }

        int getWorkedHours() {
            return workedHours;
        }

        public void setWorkedHours(int workedHours) {
            this.workedHours = workedHours;
        }

        public int getHourPaid() {
            return hourPaid;
        }

        public void setHourPaid(int hourPaid) {
            this.hourPaid = hourPaid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Employee.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'").add("workedHours=" + workedHours)
                    .add("hourPaid=" + hourPaid).toString();
        }
    }


    static class Department implements Serializable {
        private String title;
        private List<Employee> employees;


        public Department(String title, List<Employee> employees) {
            this.title = title;
            this.employees = employees;
        }

        public List<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(List<Employee> employees) {
            this.employees = employees;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Department.class.getSimpleName() + "[", "]")
                    .add("title='" + title + "'").add("employees=" + employees).toString();
        }
    }
}
