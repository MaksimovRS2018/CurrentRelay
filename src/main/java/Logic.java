public class Logic {

    private  OutpuData od = new OutpuData(); //объект класса, содержит в себе значение дискретного сигнала
    boolean triper;

    public boolean process(RMSValues rms, double tripPoint) {
        //если хотя бы один ток больше, чем уставка, то происходит отключение
        if (rms.getPhA() > tripPoint | rms.getPhB() > tripPoint | rms.getPhC() > tripPoint) {
            //отключение
            triper = od.trip(true);
        } else {
            triper = od.trip(false);
        }
        return triper;
    }



}
