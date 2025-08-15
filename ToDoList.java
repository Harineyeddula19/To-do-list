import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ToDoList {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListApp());
    }
}

class TodoListApp extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton, deleteButton, completeButton, editButton, clearButton;

    private ArrayList<String> tasks;
    private final String FILE_NAME = "tasks.txt";

    public TodoListApp() {
        setTitle("To-Do List Application");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tasks = new ArrayList<>();
        loadTasks(); // Load tasks from file

        // Layout
        setLayout(new BorderLayout());

        // Top panel with input
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        taskField = new JTextField();
        addButton = new JButton("Add Task");
        topPanel.add(taskField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center panel with list
        listModel = new DefaultListModel<>();
        for (String task : tasks) {
            listModel.addElement(task);
        }
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        completeButton = new JButton("Mark Complete");
        deleteButton = new JButton("Delete Task");
        editButton = new JButton("Edit Task");
        clearButton = new JButton("Clear All");
        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(editButton);
        bottomPanel.add(clearButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> completeTask());
        editButton.addActionListener(e -> editTask());
        clearButton.addActionListener(e -> clearAllTasks());

        // Save tasks on close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveTasks();
            }
        });

        setVisible(true);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            listModel.addElement(task);
            taskField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Enter a task first!");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            listModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to delete!");
        }
    }

    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String task = tasks.get(selectedIndex);
            if (!task.startsWith("[Done] ")) {
                task = "[Done] " + task;
                tasks.set(selectedIndex, task);
                listModel.set(selectedIndex, task);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to mark complete!");
        }
    }

    private void editTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String currentTask = tasks.get(selectedIndex);
            String newTask = JOptionPane.showInputDialog(this, "Edit Task:", currentTask);
            if (newTask != null && !newTask.trim().isEmpty()) {
                tasks.set(selectedIndex, newTask.trim());
                listModel.set(selectedIndex, newTask.trim());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to edit!");
        }
    }

    private void clearAllTasks() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all tasks?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tasks.clear();
            listModel.clear();
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    tasks.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
