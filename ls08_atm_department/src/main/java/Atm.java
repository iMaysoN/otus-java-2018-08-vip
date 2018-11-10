import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Atm {
    private Map<BanknoteDignity, Integer> moneyStorage;
    private List<BanknoteDignity> initialDignities;

    private AtmState atmState;

    public Atm(List<BanknoteDignity> initialDignities) {
        initBanknotDignities(initialDignities);
    }

    private void initBanknotDignities(List<BanknoteDignity> banknoteDignities) {
        this.initialDignities = banknoteDignities;
        this.moneyStorage = banknoteDignities.stream()
                .collect(Collectors.toMap(f -> f, f -> 0));
    }

    public Atm(List<BanknoteDignity> initialDignities, Map<BanknoteDignity, Integer> settings) {
        initBanknotDignities(initialDignities);
        for (BanknoteDignity dignity : settings.keySet()) {
            this.moneyStorage.put(dignity, settings.get(dignity));
        }
    }

    public AtmState getAtmState() {
        return atmState;
    }

    public void setAtmState(AtmState atmState) {
        this.atmState = atmState;
    }

    public void store(BanknoteDignity dignity, int count) {
        if (atmState.equals(AtmState.PowerOn)) {
            if (!initialDignities.contains(dignity)) {
                System.out.printf("Wrong dignity, ATM can't use this money: dignity - %s, count - %s. It will be returned.", dignity, count);
            } else if (count <= 0){
                System.out.println("Wrong count, can't be zero or less.");
            } else {
                moneyStorage.put(dignity, moneyStorage.get(dignity) + count);
            }
        } else {
            System.out.println("Sorry, ATM doesn't work currently.");
        }
    }

    public int totalStored() {
        return moneyStorage.entrySet().stream()
                .map(entry -> entry.getKey().getDignity() * entry.getValue())
                .reduce((i1, i2) -> i1 + i2)
                .orElse(0);
    }

    public Map<BanknoteDignity, Integer> totalByDignity() {
        return Map.copyOf(moneyStorage);
    }

    public void getMyMoneyBack(int moneySum) {
        if (atmState.equals(AtmState.PowerOn)) {
            Map<BanknoteDignity, Integer> cashAdvance = new Supplier<Map<BanknoteDignity, Integer>>() {
                private Map<BanknoteDignity, Integer> cashAdvance = new HashMap<>();
                private int nextSumForService = moneySum;

                private Iterator<BanknoteDignity> reversedDignity = initialDignities.stream()
                        .sorted((d1, d2) -> d2.getDignity() - d1.getDignity())
                        .collect(Collectors.toList())
                        .iterator();

                @Override
                public Map<BanknoteDignity, Integer> get() {
                    collectMoneyFromAtm(reversedDignity);
                    return cashAdvance;
                }

                private void collectMoneyFromAtm(Iterator<BanknoteDignity> integerListIterator) {
                    if (integerListIterator.hasNext()) {
                        BanknoteDignity currentDignity = integerListIterator.next();
                        int currentDignityCountInAtm = moneyStorage.get(currentDignity);
                        if (currentDignityCountInAtm != 0) {
                            int countForAdvance = nextSumForService / currentDignity.getDignity();
                            int counted = currentDignityCountInAtm >= countForAdvance ? countForAdvance : currentDignityCountInAtm;
                            cashAdvance.put(currentDignity, counted);
                            nextSumForService -= counted * currentDignity.getDignity();
                        }
                        collectMoneyFromAtm(integerListIterator);
                    }
                }
            }.get();

            Integer totalCashed = cashAdvance.entrySet().stream()
                    .map(entry -> entry.getKey().getDignity() * entry.getValue())
                    .reduce((i1, i2) -> i1 + i2)
                    .orElse(0);
            if (totalCashed < moneySum) {
                System.out.printf("Can't collect [%s].%n", moneySum);
            } else {
                cashAdvance.forEach((dignity, count) -> moneyStorage.put(dignity, moneyStorage.get(dignity) - count));
                System.out.println("----------- TO CASH ------------");
                for (BanknoteDignity dignity : initialDignities) {
                    if (cashAdvance.containsKey(dignity) && cashAdvance.get(dignity) > 0) {
                        System.out.printf("Issued: %s x %s.%n", dignity, cashAdvance.get(dignity));
                    }
                }
                System.out.println("--------------------------------");
            }
        } else {
            System.out.println("Sorry, ATM doesn't work currently.");
        }
    }
}
