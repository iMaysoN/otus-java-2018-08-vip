import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Atm {
    private static final List<Integer> faceValues = Arrays.asList(10, 50, 100, 200, 500, 1000, 5000);
    private Map<Integer, Integer> moneyStorage;

    public Atm() {
        moneyStorage = faceValues.stream()
                .collect(Collectors.toMap(f -> f, f -> 0));
    }

    public void store(int face, int count) {
        if (!faceValues.contains(face)) {
            System.out.printf("Wrong face, ATM can't use this money: face - %s, count - %s. It will be returned.", face, count);
        } else {
            moneyStorage.put(face, count);
        }
    }

    public void totalStored() {
        int total = 0;
        for (int face : faceValues) {
            total += face * moneyStorage.get(face);
        }
        System.out.printf("Total - %s.%n", total);
    }

    public void totalByFace() {
        for (int face : faceValues) {
            System.out.printf("Face %s: %s count.%n", face, moneyStorage.get(face));
        }
    }

    public void getMyMoneyBack(int moneySum) {
        Map<Integer, Integer> cashAdvance = new Supplier<Map<Integer, Integer>>() {
            private Map<Integer, Integer> cashAdvance = new HashMap<>();
            private int nextSumForService = moneySum;

            private Iterator<Integer> reversedFace = faceValues.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList())
                    .iterator();

            @Override
            public Map<Integer, Integer> get() {
                collectMoneyFromAtm(reversedFace);
                return cashAdvance;
            }

            private void collectMoneyFromAtm(Iterator<Integer> integerListIterator) {
                if (integerListIterator.hasNext()) {
                    int currentFace = integerListIterator.next();
                    int currentFaceCountInAtm = moneyStorage.get(currentFace);
                    if (currentFaceCountInAtm != 0) {
                        int countForAdvance = nextSumForService / currentFace;
                        int counted = currentFaceCountInAtm >= countForAdvance ? countForAdvance : currentFaceCountInAtm;
                        cashAdvance.put(currentFace, counted);
                        nextSumForService -= counted * currentFace;
                    }
                    collectMoneyFromAtm(integerListIterator);
                }
            }
        }.get();

        Integer totalCashed = cashAdvance.entrySet().stream()
                .map(entry -> entry.getKey() * entry.getValue())
                .reduce((i1, i2) -> i1 + i2)
                .orElse(0);
        if (totalCashed < moneySum) {
            System.out.printf("Can't collect [%s].%n", moneySum);
        } else {
            cashAdvance.forEach((face, count) -> moneyStorage.put(face, moneyStorage.get(face) - count));
            System.out.println("----------- TO CASH ------------");
            for (int face : faceValues) {
                if (cashAdvance.containsKey(face) && cashAdvance.get(face) > 0) {
                    System.out.printf("Issued: %s x %s.%n", face, cashAdvance.get(face));
                }
            }
            System.out.println("--------------------------------");
        }
    }
}
