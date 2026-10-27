/** A hostel resident who orders meals using a prepaid balance. */
public class Student extends Person {
    private final String roomNo;
    private double balance;

    public Student(String id, String name, String roomNo, double balance) {
        super(id, name);
        this.roomNo = roomNo;
        this.balance = balance;
    }

    public String getRoomNo() { return roomNo; }
    public double getBalance() { return balance; }

    public void credit(double amount) {
        balance += amount;
    }

    /** Deducts the amount if the balance is enough; returns false otherwise. */
    public boolean debit(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    @Override
    public String getRole() { return "Student"; }

    @Override
    public String toString() { return getId() + " - " + getName(); }
}
