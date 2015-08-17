package com.makasprzak.matrix;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static javax.swing.SwingUtilities.invokeLater;

public class GuiApp {
    public static void main(String[] args) {
        invokeLater(() -> {
            Optional<List<Statistic>> statistics = new StatisticsRepository().findLatest();
            createAndShowGui(statistics.get());
        });
    }

    private static void createAndShowGui(List<Statistic> statistics) {
        JFrame frame = new JFrame("Counting");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTable table = new JTable();
        table.setModel(tableModel(sort(statistics).collect(toList())));
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }

    private static Stream<Statistic> sort(List<Statistic> statistics) {
        return statistics.stream().sorted((left, right) -> right.getTimeTaken().compareTo(left.getTimeTaken()));
    }

    private static DefaultTableModel tableModel(List<Statistic> statistics) {
        return doGetTableModel(statistics.stream()
                .map(stat -> new Object[]{stat.getEquation().print(), toString(stat.getTimeTaken())})
                .collect(toList()));
    }

    private static DefaultTableModel doGetTableModel(List<Object[]> transformed) {
        return new DefaultTableModel(toArray(transformed),new Object[]{"Equation", "Time taken"});
    }

    private static Object[][] toArray(List<Object[]> transformed) {
        return transformed.toArray(new Object[transformed.size()][]);
    }

    private static String toString(long millis) {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
