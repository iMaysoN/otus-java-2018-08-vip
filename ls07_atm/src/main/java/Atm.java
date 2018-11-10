import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Atm {
    private static final List<Integer> banknoteDignities = Arrays.asList(10, 50, 100, 200, 500, 1000, 5000);
    private Map<Integer, Integer> moneyStorage;

    public Atm() {
        moneyStorage = banknoteDignities.stream()
                .collect(Collectors.toMap(f -> f, f -> 0));
    }

    public void store(int dignity, int count) {
        if (!banknoteDignities.contains(dignity)) {
            System.out.printf("Wrong dignity, ATM can't use this money: dignity - %s, count - %s. It will be returned.", dignity, count);
        } else {
            moneyStorage.put(dignity, moneyStorage.get(dignity) + count);
        }
    }

    public void totalStored() {
        int total = moneyStorage.entrySet().stream()
                .map(entry -> entry.getKey() * entry.getValue())
                .reduce((i1, i2) -> i1 + i2)
                .orElse(0);
        System.out.printf("Total - %s.%n", total);
    }

    public void totalByDignity() {
        for (int dignity : banknoteDignities) {
            System.out.printf("Dignity %s: %s count.%n", dignity, moneyStorage.get(dignity));
        }
    }

    public void getMyMoneyBack(int moneySum) {
        Map<Integer, Integer> cashAdvance = new Supplier<Map<Integer, Integer>>() {
            private Map<Integer, Integer> cashAdvance = new HashMap<>();
            private int nextSumForService = moneySum;

            private Iterator<Integer> reversedDignity = banknoteDignities.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList())
                    .iterator();

            @Override
            public Map<Integer, Integer> get() {
                collectMoneyFromAtm(reversedDignity);
                return cashAdvance;
            }

            private void collectMoneyFromAtm(Iterator<Integer> integerListIterator) {
                if (integerListIterator.hasNext()) {
                    int currentDignity = integerListIterator.next();
                    int currentDignityCountInAtm = moneyStorage.get(currentDignity);
                    if (currentDignityCountInAtm != 0) {
                        int countForAdvance = nextSumForService / currentDignity;
                        int counted = currentDignityCountInAtm >= countForAdvance ? countForAdvance : currentDignityCountInAtm;
                        cashAdvance.put(currentDignity, counted);
                        nextSumForService -= counted * currentDignity;
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
            cashAdvance.forEach((dignity, count) -> moneyStorage.put(dignity, moneyStorage.get(dignity) - count));
            System.out.println("----------- TO CASH ------------");
            for (int dignity : banknoteDignities) {
                if (cashAdvance.containsKey(dignity) && cashAdvance.get(dignity) > 0) {
                    System.out.printf("Issued: %s x %s.%n", dignity, cashAdvance.get(dignity));
                }
            }
            System.out.println("--------------------------------");
        }
    }
}
