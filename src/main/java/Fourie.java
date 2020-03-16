public class Fourie implements Filter {
    private double[] bufferPhA = new double[80];
    private double[] bufferPhB = new double[80];
    private double[] bufferPhC = new double[80];
    private SampleValues sv;
    private RMSValues rms;
    private double A0A;
    private double Ak1A;
    private double Bk1A;
    private double A0B;
    private double Ak1B;
    private double Bk1B;
    private double A0C;
    private double Ak1C;
    private double Bk1C;
    private int count = 0;

    public double calculate(String function, double setPoint) {
        int period = 80; //количество точек за период
        //Алгоритм фурье постоянная составляющая + 1 гармоника
        //расчет разницы между двумя значениями
        double sumPhA = sv.getPhA() - bufferPhA[count];
        //расчет постоянной составляющей, возникает при КЗ. В норм. режиме равна нулю -> интеграл синусоиды = 0
        A0A += sumPhA / period;
        //расчет cos и sin составляющей для первой гармоники
        Ak1A += 2 * (Math.cos(count * 2 * Math.PI / period) * sumPhA) / period;
        Bk1A += 2 * (Math.sin(count * 2 * Math.PI / period) * sumPhA) / period;
        //расчет действующего значения для 1 гармоники по cos и sin составляющей
        double Ck1A = Math.sqrt((Math.pow(Ak1A, 2) + Math.pow(Bk1A, 2)) / 2);
        //суммарная составляющая
        double xA = Math.sqrt(Math.pow(Ck1A, 2) + Math.pow(A0A, 2));
        bufferPhA[count] = sv.getPhA();

        double sumPhB = sv.getPhB() - bufferPhB[count];
        A0B += sumPhB / period;
        Ak1B += 2 * (Math.cos(count * 2 * Math.PI / period) * sumPhB) / period;
        Bk1B += 2 * (Math.sin(count * 2 * Math.PI / period) * sumPhB) / period;
        double Ck1B = Math.sqrt((Math.pow(Ak1B, 2) + Math.pow(Bk1B, 2)) / 2);
        double xB = Math.sqrt(Math.pow(Ck1B, 2) + Math.pow(A0B, 2));
        bufferPhB[count] = sv.getPhB();

        double sumPhC = sv.getPhC() - bufferPhC[count];
        A0C += sumPhB / period;
        Ak1C += 2 * (Math.cos(count * 2 * Math.PI / period) * sumPhC) / period;
        Bk1C += 2 * (Math.sin(count * 2 * Math.PI / period) * sumPhC) / period;
        double Ck1C = Math.sqrt((Math.pow(Ak1C, 2) + Math.pow(Bk1C, 2)) / 2);
        double xC = Math.sqrt(Math.pow(Ck1C, 2) + Math.pow(A0C, 2));
        bufferPhC[count] = sv.getPhC();
        //устанавливаем действующие значения для логики
        rms.setPhA(xA);
        rms.setPhB(xB);
        rms.setPhC(xC);


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
        count++;
        if (count == period) {

            count = 0;
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

