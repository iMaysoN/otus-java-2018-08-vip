import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class DepartmentManager {
    private final Map<DefaultAtmConfig, Atm> atmMap;

    public DepartmentManager(DefaultAtmConfig... atmToInit) {
        List<BanknoteDignity> listOfAvailableBanknote = Arrays.stream(BanknoteDignity.values())
                .filter(b -> Currency.RUB.equals(b.getCurrency()))
                .collect(Collectors.toList());

        atmMap = new HashMap<>();
        for (DefaultAtmConfig atmConfig : atmToInit) {
            Atm initiatedAtm = new Atm(listOfAvailableBanknote, atmConfig.getInitCashValues(), atmConfig.name());
            initiatedAtm.setAtmState(AtmState.PowerOn);
            atmMap.put(atmConfig, initiatedAtm);
        }
    }

    public void printTotalDignityCount() {
        Map<BanknoteDignity, Integer> totalBanknoteCount = new HashMap<>();

        for (Atm atm : atmMap.values()) {
            for (Map.Entry<BanknoteDignity, Integer> banknote : atm.totalByDignity().entrySet()) {
                if (!totalBanknoteCount.containsKey(banknote.getKey())) {
                    totalBanknoteCount.put(banknote.getKey(), banknote.getValue());
                } else {
                    totalBanknoteCount.put(banknote.getKey(), totalBanknoteCount.get(banknote.getKey()) + banknote.getValue());
                }
            }
        }

        System.out.printf("In %s ATMs contains banknotes:%n", atmMap.size());
        printMap(totalBanknoteCount);
    }

    private void printMap(Map<BanknoteDignity, Integer> map) {
        map.keySet().stream()
                .sorted()
                .forEach(key ->
                        System.out.printf("%16s %s: %7s%n",
                                key.getDignity(),
                                key.getCurrency(),
                                map.get(key))
                );
    }

    public void printTotalMoneyEquivalent() {
        System.out.printf("Total money in all ATM: %s.%n", getMoneyEquivalentFromAllAtm());
    }

    public int getMoneyEquivalentFromAllAtm() {
        int total = 0;
        for (Atm atm : atmMap.values()) {
            total += atm.totalStored();
        }
        return total;
    }

    public void printBanknoteCountByAtm() {
        for (Atm atm : atmMap.values()) {
            System.out.printf("------- %13s -------%n", atm.getName());
            System.out.printf("------- %13s -------%n", atm.getAtmState().name());
            printMap(atm.totalByDignity());
            System.out.println("-----------------------------");
        }
    }

    public void getMoneyFromAll(int moneyEquivalent) {
        for (Atm atm : atmMap.values()) {
            atm.getMyMoneyBack(moneyEquivalent);
        }
    }

    public void restoreAll() {
        int wasMoney = getMoneyEquivalentFromAllAtm();
        for (Atm atm : atmMap.values()) {
            atm.restore();
        }
        System.out.printf("All ATMs restored banknote counts to initial state - from `%s` to `%s`.%n", wasMoney, getMoneyEquivalentFromAllAtm());
    }

    public void storeToAll(BanknoteDignity dignity, int count) {
        for (Atm atm : atmMap.values()) {
            atm.store(dignity, count);
        }
    }
}
