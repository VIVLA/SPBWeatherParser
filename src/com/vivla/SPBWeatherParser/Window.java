package com.vivla.SPBWeatherParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Window extends JFrame implements Runnable {
    private JTable table;
    private DefaultTableModel model;

    private JScrollPane scroll;

    @Override
    public void run() {
        try {
            initFrame();
        } catch (Exception ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initFrame() throws Exception {
        table = new JTable();
        model = new DefaultTableModel();

        String headers[] = {"Погода в Санкт-Петербурге"};

        model.setColumnIdentifiers(headers);
        table.setModel(model);
        scroll = new JScrollPane(table);

        insert();

        add(scroll, BorderLayout.CENTER);
        setTitle("Weather");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void insert() throws Exception {
        Parser p = new Parser();
        ArrayList<String> data = p.getArray();

        ArrayList<String> ar = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ar.add(data.get(i));
        }
        for (int i = 0; i < ar.size(); i++) {
            model.addRow(new Object[] { String.valueOf(ar.get(i)) });
        }
    }
}
