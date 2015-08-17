package com.makasprzak.matrix;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.makasprzak.matrix.Equation.Builder.equation;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class Matrix {

    public final StatisticsRepository statisticsRepository = new StatisticsRepository();

    public static void main(String[] args) throws Exception {
        new Matrix().run(args);

    }

    private void run(String[] args) throws Exception {
        if (args.length == 1 && args[0].equals("t")) {
            List<Equation> equations = buildEquations(2, 3);
            execute(20_000,equations);
            return;
        }
        long timeLimit = Integer.parseInt(args[0]) * 1000;
        Optional<List<Statistic>> statistics = statisticsRepository.findLatest();
        if (!(args.length > 1 && args[1].equals("n")) && statistics.isPresent()) {
            try {
                List<Equation> equationList = statistics.get().stream()
                        .filter(stat -> stat.getTimeTaken() > threshold(args))
                        .map(Statistic::getEquation)
                        .collect(toList());
                new Matrix().execute(timeLimit, equationList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else new Matrix().execute(timeLimit);
    }

    private static long threshold(String[] args) {
        if (args.length == 2) {
            return (long) (Integer.parseInt(args[1]) * 1000);
        }
        if (args.length == 3) {
            return (long) (Integer.parseInt(args[2]) * 1000);
        }
        return 20_000;
    }



    private void execute(long timeLimit) throws Exception {
        execute(timeLimit, buildEquationList());
    }

    private void execute(long timeLimit, List<Equation> equationList) throws Exception {
        EquationManager equations = EquationManager.create(equationList);
        performLearningSession(timeLimit, equations);
        List<Statistic> statistics = equations.getStatistics();
        statisticsRepository.save(statistics);
    }


    private void performLearningSession(long timeLimit, EquationManager equations) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (equations.isEmpty()) {
                System.out.println("KONIEC! Zawolaj Tate");
                break;
            } else {
                System.out.println("Jeszcze " + equations.size() + " dzialan");
            }
            int index = (int) (Math.random() * equations.size());
            Equation equation = equations.get(index);
            long start = System.currentTimeMillis();
            System.out.println(equation.print());
            int answer = readAnswer(scanner);
            long stop = System.currentTimeMillis();
            boolean incorrect = answer != equation.getExpected();
            long timeTaken = stop - start;
            boolean timeout = timeTaken > timeLimit;
            if (incorrect) {
                System.out.println("Niestety wynik niepoprawny :(");
                equations.addTimeSpent(equation,timeTaken);
                play("/error.wav");
            } else {
                if (timeout) {
                    System.out.println("Wynik dobry, ale niestety zajelo Ci to zbyt wiele czasu :(");
                    play("/faster.wav");
                    equations.addTimeSpent(equation,timeTaken);
                } else {
                    System.out.println("Dobrze!");
                    play("/yuppi.wav");
                    equations.addTimeSpent(equation,timeTaken);
                    equations.remove(index);
                }
            }

        }
    }

    private List<Equation> buildEquationList() {
        return buildEquations(14, 9);
    }

    private List<Equation> buildEquations(int leftsRange, int rightsRange) {
        List<Integer> lefts = range(1, leftsRange).boxed().collect(toList());
        List<Integer> rights = range(1, rightsRange).boxed().collect(toList());
        List<Equation> multiplyEquations = new LinkedList<>();
        lefts.forEach(
                left -> rights.forEach(
                        right -> multiplyEquations.add(equation().withLeft(left).withRight(right).withOperand(Operand.MULTIPLY).build())

                )

        );
        List<Equation> equations = new LinkedList<>(multiplyEquations);
        multiplyEquations.forEach(multiply -> equations.add(
                equation()
                        .withLeft(multiply.getExpected())
                        .withRight(multiply.getRight())
                        .withOperand(Operand.DIVIDE)
                        .build()));
        return equations;
    }

    private void play(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    private int readAnswer(Scanner scanner) {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            scanner.next();
            System.out.println("Hmm, to chyba nie jest liczba ;>");
            return readAnswer(scanner);
        }
    }

}
