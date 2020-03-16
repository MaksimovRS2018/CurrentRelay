import java.util.ArrayList;
import java.util.List;

public class MiddleValue implements Filter{

    private double[] bufferPhA = new double[80];
    private double[] bufferPhB = new double[80];
    private double[] bufferPhC = new double[80];
//    private List<Double> bufferPhA =  new ArrayList<Double>(0);
//    private List<Double> bufferPhB =  new ArrayList<Double>(0);
//    private List<Double> bufferPhC =  new ArrayList<Double>(0);
    private SampleValues sv;
    private RMSValues rms;
    private double sumPhA = 0;
    private double sumPhB = 0;
    private double sumPhC = 0;
    private double k = 1.11/80;
    private int count= 0;

    private double maxI = 0;
    private boolean t;

    public double calculate(String function, double setPoint) {
        sumPhA+=Math.abs(sv.getPhA()) - bufferPhA[count];
        bufferPhA[count]=Math.abs(sv.getPhA());

        sumPhB+=Math.abs(sv.getPhB()) - bufferPhB[count];
        bufferPhB[count]=Math.abs(sv.getPhB());

        sumPhC+=Math.abs(sv.getPhC()) - bufferPhC[count];
        bufferPhC[count]=Math.abs(sv.getPhC());


        rms.setPhA(k*sumPhA);
        rms.setPhB(k*sumPhB);
        rms.setPhC(k*sumPhC);

        if (function.equals("Посчитать уставку")) {
            if (rms.getPhA() >= setPoint) {
                setPoint = rms.getPhA();
            }
            if (rms.getPhB() >= setPoint) {
                setPoint = rms.getPhB();
            }
            if (rms.getPhC() >= setPoint) {
                setPoint = rms.getPhC();
            }
        } else {
            Charts.addAnalogData(3, 3, setPoint);
        }
        rms.setTime(sv.getTime());

        if (++count==80) {
            count=0;
        }

        return setPoint;

    }


    public SampleValues getSv() {
        return sv;
    }

    public void setSv(SampleValues sv) {
        this.sv = sv;
    }

    public RMSValues getRms() {
        return rms;
    }

    public void setRms(RMSValues rms) {
        this.rms = rms;
    }

}
