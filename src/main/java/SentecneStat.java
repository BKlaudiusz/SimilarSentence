class SentecneStat {
    private String value;
    private double rate;

    SentecneStat(String value) {
        this.value = value;
        rate = 0;
    }
    SentecneStat()
    {
        value = "";
        rate = 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return value+"\n";
    }
}
