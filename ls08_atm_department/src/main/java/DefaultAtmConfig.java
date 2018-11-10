import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public enum DefaultAtmConfig {
    Vosstanie(ofEntries(
            entry(BanknoteDignity.r5000, 1),
            entry(BanknoteDignity.r500, 10),
            entry(BanknoteDignity.r100, 17))),
    Petrogradka(ofEntries(
            entry(BanknoteDignity.r5000, 1),
            entry(BanknoteDignity.r500, 2),
            entry(BanknoteDignity.r100, 17),
            entry(BanknoteDignity.r200, 15))),
    Lenina(ofEntries(
            entry(BanknoteDignity.r5000, 2),
            entry(BanknoteDignity.r1000, 2),
            entry(BanknoteDignity.r500, 2),
            entry(BanknoteDignity.r200, 2),
            entry(BanknoteDignity.r100, 12),
            entry(BanknoteDignity.r10, 10)));

    private final Map<BanknoteDignity, Integer> initCashValues;

    DefaultAtmConfig(Map<BanknoteDignity, Integer> initCashValues) {
        this.initCashValues = initCashValues;
    }

    public Map<BanknoteDignity, Integer> getInitCashValues() { return initCashValues; }
}
