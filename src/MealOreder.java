import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** An order placed by a student for one or more meal items. */
public class MealOrder {
    private final int orderId;
    private final Student student;
    private final List<MealItem> items;
    private final LocalDate date;

    public MealOrder(int orderId, Student student, List<MealItem> items, LocalDate date) {
        this.orderId = orderId;
        this.student = student;
        this.items = new ArrayList<>(items);
        this.date = date;
    }

    public int getOrderId() { return orderId; }
    public Student getStudent() { return student; }
    public List<MealItem> getItems() { return items; }
    public LocalDate getDate() { return date; }

    public double getTotal() {
        double total = 0;
        for (MealItem item : items) {
            total += item.getPrice();
        }
        return total;
    }
}
