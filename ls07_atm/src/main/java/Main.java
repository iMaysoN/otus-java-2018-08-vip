public class Main {
    public static void main(String[] args) {
        Atm atm = new Atm();

        atm.store(5000, 2);
        atm.store(1000, 2);
        atm.store(500, 2);
        atm.store(200, 2);
        atm.store(100, 2);
        atm.store(50, 2);
        atm.store(10, 2);
        atm.totalByFace();

        atm.getMyMoneyBack(5200);
        atm.totalByFace();
        atm.getMyMoneyBack(600);
        atm.totalByFace();

        //Errors
        atm.getMyMoneyBack(1080);
        atm.totalStored();
        atm.store(5, 1);
        atm.totalStored();
    }
}
