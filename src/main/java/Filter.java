public interface Filter {
    public  double calculate(String function, double setPoint);
    public SampleValues getSv();
    public void setSv(SampleValues sv);
    public RMSValues getRms();
    public void setRms(RMSValues rms);
}
