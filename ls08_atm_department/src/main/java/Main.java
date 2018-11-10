public class Main {
    public static void main(String[] args) {
        DepartmentManager departmentManager = new DepartmentManager(DefaultAtmConfig.values());

        departmentManager.printBanknoteCountByAtm();
        departmentManager.printTotalDignityCount();
        departmentManager.printTotalMoneyEquivalent();
        departmentManager.getMoneyFromAll(6000);
        departmentManager.printTotalDignityCount();
        departmentManager.printTotalMoneyEquivalent();
        departmentManager.restoreAll();
        departmentManager.printTotalMoneyEquivalent();
        departmentManager.storeToAll(BanknoteDignity.r5000, 10);
        departmentManager.printTotalMoneyEquivalent();
    }
}
