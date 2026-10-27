import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Business logic of CampusMeal: students, menu, orders and payments. */
public class MealService {
    private final List<Student> students = new ArrayList<>();
    private final List<MealItem> menu = new ArrayList<>();
    private final List<MealOrder> orders = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();
    private int orderSeq = 1;
    private int paymentSeq = 1;

    public void addStudent(Student s) { students.add(s); }
    public void addMealItem(MealItem m) { menu.add(m); }

    public List<Student> getStudents() { return students; }
    public List<MealItem> getMenu() { return menu; }

    /** Places an order if items were selected and the balance covers the total. */
    public MealOrder placeOrder(Student s, List<MealItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Select at least one meal.");
        }
        double total = 0;
        for (MealItem item : items) {
            total += item.getPrice();
        }
        if (s.getBalance() < total) {
            throw new IllegalStateException(
                "Insufficient balance. Total is " + total + " BDT but balance is " + s.getBalance() + " BDT.");
        }
        s.debit(total);
        MealOrder order = new MealOrder(orderSeq++, s, items, LocalDate.now());
        orders.add(order);
        return order;
    }

    /** Adds money to the student's balance and records the payment. */
    public Payment deposit(Student s, double amount, String method) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        s.credit(amount);
        Payment p = new Payment(paymentSeq++, s, amount, method, LocalDate.now());
        payments.add(p);
        return p;
    }

    public List<MealOrder> getOrdersFor(Student s) {
        List<MealOrder> result = new ArrayList<>();
        for (MealOrder o : orders) {
            if (o.getStudent() == s) result.add(o);
        }
        return result;
    }

    public List<Payment> getPaymentsFor(Student s) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : payments) {
            if (p.getStudent() == s) result.add(p);
        }
        return result;
    }
}
