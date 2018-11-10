import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentManager {
    private static final List<Integer> faceValues = Arrays.asList(10, 50, 100, 200, 500, 1000, 5000);
    private final Map<DefaultAtmConfig, Atm> atmMap;

    public DepartmentManager(DefaultAtmConfig... atmToInit) {
        List<BanknoteDignity> listOfAvailableBanknote = Arrays.stream(BanknoteDignity.values())
                .filter(b -> Currency.RUB.equals(b.getCurrency()))
                .collect(Collectors.toList());

        atmMap = new HashMap<>();
        for (DefaultAtmConfig atmState : atmToInit) {
            Atm initiatedAtm = new Atm(listOfAvailableBanknote, atmState.getInitCashValues());
            initiatedAtm.setAtmState(AtmState.PowerOn);
            atmMap.put(atmState, initiatedAtm);
        }
    }

    public void collectAllFaceCount() {

    }
}
