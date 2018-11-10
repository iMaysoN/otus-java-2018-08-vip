public enum BanknoteDignity {
    r5000(Currency.RUB, 5000), r1000(Currency.RUB, 1000), r500(Currency.RUB, 500), r200(Currency.RUB, 200),
    r100(Currency.RUB, 100), r50(Currency.RUB, 50), r10(Currency.RUB, 10);

    private final int dignity;
    private final Currency currency;

    BanknoteDignity(Currency currency, int dignity) {
        this.dignity = dignity;
        this.currency = currency;
    }

    public int getDignity() {
        return dignity;
    }

    public Currency getCurrency() {
        return currency;
    }
}
