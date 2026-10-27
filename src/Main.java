import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.util.List;

/** Entry point. Opens the Swing UI, or runs a console demo when no display exists. */
public class Main {

    private static MealService seed() {
        MealService service = new MealService();

        Manager manager = new Manager("M001", "Hostel Manager");
        manager.publishMeal(service, new MealItem(1, "Paratha and Egg", MealType.BREAKFAST, 40));
        manager.publishMeal(service, new MealItem(2, "Chicken Curry and Rice", MealType.LUNCH, 90));
        manager.publishMeal(service, new MealItem(3, "Fish Curry and Rice", MealType.LUNCH, 80));
        manager.publishMeal(service, new MealItem(4, "Vegetable Khichuri", MealType.DINNER, 70));

        service.addStudent(new Student("IT24001", "Rahim", "214", 300));
        service.addStudent(new Student("IT24002", "Karim", "215", 100));
        service.addStudent(new Student("IT24003", "Sumaiya", "310", 0));
        return service;
    }

    public static void main(String[] args) {
        MealService service = seed();

        if (GraphicsEnvironment.isHeadless()) {
            runConsoleDemo(service);
            return;
        }
        SwingUtilities.invokeLater(() -> new MainFrame(service).setVisible(true));
    }

    private static void runConsoleDemo(MealService service) {
        Student s = service.getStudents().get(0);
        System.out.println("Student: " + s + ", balance " + s.getBalance());

        service.deposit(s, 500, "bKash");
        System.out.println("After deposit of 500: balance " + s.getBalance());

        List<MealItem> picks = List.of(service.getMenu().get(1), service.getMenu().get(3));
        MealOrder order = service.placeOrder(s, picks);
        System.out.println("Order #" + order.getOrderId() + " total " + order.getTotal()
            + ", balance now " + s.getBalance());

        Student poor = service.getStudents().get(2);
        try {
            service.placeOrder(poor, picks);
        } catch (IllegalStateException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
    }
}
