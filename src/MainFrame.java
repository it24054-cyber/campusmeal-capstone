import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Swing user interface for CampusMeal. */
public class MainFrame extends JFrame {
    private final MealService service;

    private final JComboBox<Student> studentBox = new JComboBox<>();
    private final JLabel balanceLabel = new JLabel();

    private final DefaultTableModel menuModel = readOnlyModel("ID", "Meal", "Type", "Price (BDT)");
    private final DefaultListModel<MealItem> itemListModel = new DefaultListModel<>();
    private final JList<MealItem> itemList = new JList<>(itemListModel);
    private final DefaultTableModel historyModel = readOnlyModel("Date", "Kind", "Details", "Amount (BDT)");

    public MainFrame(MealService service) {
        super("CampusMeal - Hostel Meal Management");
        this.service = service;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);

        for (Student s : service.getStudents()) {
            studentBox.addItem(s);
        }
        studentBox.addActionListener(e -> refresh());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Student:"));
        top.add(studentBox);
        top.add(balanceLabel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Menu", new JScrollPane(new JTable(menuModel)));
        tabs.addTab("Place Order", buildOrderTab());
        tabs.addTab("Deposit", buildDepositTab());
        tabs.addTab("History", new JScrollPane(new JTable(historyModel)));

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        refresh();
    }

    private static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JPanel buildOrderTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Select one or more meals (Ctrl+click for multiple):"), BorderLayout.NORTH);
        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JButton orderButton = new JButton("Place Order");
        orderButton.addActionListener(e -> {
            Student s = (Student) studentBox.getSelectedItem();
            List<MealItem> selected = itemList.getSelectedValuesList();
            try {
                MealOrder order = service.placeOrder(s, selected);
                JOptionPane.showMessageDialog(this,
                    "Order #" + order.getOrderId() + " placed. Total: " + order.getTotal() + " BDT");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot place order", JOptionPane.WARNING_MESSAGE);
            }
            refresh();
        });
        panel.add(orderButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDepositTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        JTextField amountField = new JTextField(8);
        JComboBox<String> methodBox = new JComboBox<>(new String[]{"Cash", "bKash", "Nagad"});
        JButton depositButton = new JButton("Deposit");

        depositButton.addActionListener(e -> {
            Student s = (Student) studentBox.getSelectedItem();
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                Payment p = service.deposit(s, amount, (String) methodBox.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Deposited " + p.getAmount() + " BDT via " + p.getMethod());
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number.", "Invalid amount", JOptionPane.WARNING_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot deposit", JOptionPane.WARNING_MESSAGE);
            }
            refresh();
        });

        panel.add(new JLabel("Amount (BDT):"));
        panel.add(amountField);
        panel.add(new JLabel("Method:"));
        panel.add(methodBox);
        panel.add(depositButton);
        return panel;
    }

    /** Reloads every view from the service. */
    public void refresh() {
        Student s = (Student) studentBox.getSelectedItem();
        balanceLabel.setText(s == null ? "" : "  Balance: " + s.getBalance() + " BDT  |  Room " + s.getRoomNo());

        menuModel.setRowCount(0);
        itemListModel.clear();
        for (MealItem m : service.getMenu()) {
            menuModel.addRow(new Object[]{m.getId(), m.getName(), m.getType(), m.getPrice()});
            itemListModel.addElement(m);
        }

        historyModel.setRowCount(0);
        if (s != null) {
            for (MealOrder o : service.getOrdersFor(s)) {
                StringBuilder names = new StringBuilder();
                for (MealItem m : o.getItems()) {
                    if (names.length() > 0) names.append(", ");
                    names.append(m.getName());
                }
                historyModel.addRow(new Object[]{o.getDate(), "Order #" + o.getOrderId(), names, "-" + o.getTotal()});
            }
            for (Payment p : service.getPaymentsFor(s)) {
                historyModel.addRow(new Object[]{p.getDate(), "Deposit #" + p.getPaymentId(), p.getMethod(), "+" + p.getAmount()});
            }
        }
    }
}
