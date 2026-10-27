/** Hostel manager who maintains the meal menu. */
public class Manager extends Person {

    public Manager(String id, String name) {
        super(id, name);
    }

    /** Publishes a new meal item to the menu. */
    public void publishMeal(MealService service, MealItem item) {
        service.addMealItem(item);
    }

    @Override
    public String getRole() { return "Hostel Manager"; }
}
