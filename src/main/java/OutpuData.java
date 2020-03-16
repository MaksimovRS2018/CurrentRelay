public class OutpuData {
    private boolean tripper = false;
    public boolean trip(boolean triper) {
        tripper = triper;
        Charts.addDiscreteData(0, tripper);
        return triper;
    }
}
