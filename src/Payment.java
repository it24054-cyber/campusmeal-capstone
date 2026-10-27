import java.time.LocalDate;

/** A deposit made by a student to top up the meal balance. */
public class Payment {
    private final int paymentId;
    private final Student student;
    private final double amount;
    private final String method;
    private final LocalDate date;

    public Payment(int paymentId, Student student, double amount, String method, LocalDate date) {
        this.paymentId = paymentId;
        this.student = student;
        this.amount = amount;
        this.method = method;
        this.date = date;
    }

    public int getPaymentId() { return paymentId; }
    public Student getStudent() { return student; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public LocalDate getDate() { return date; }
}
