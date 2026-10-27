/** A single item on the hostel menu. */
public class MealItem {
    private final int id;
    private final String name;
    private final MealType type;
    private final double price;

    public MealItem(int id, String name, MealType type, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public MealType getType() { return type; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " (" + type + ") - " + price + " BDT";
    }
}
