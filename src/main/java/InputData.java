import java.io.*;

public class InputData {

    public File comtrCfg, comtrDat;
    private BufferedReader br;
    private String line;
    private String[] lineData;
    private double[] k1;
    private double[] k2;
    private boolean t = false;
    private String comtradeName;
    private String function;
    private String path;
    private String cfgname = path+comtradeName+".cfg";
    private  String datName = path+comtradeName+".dat";
    private String nameFile;



    public InputData(String function, String nameFile) {
        this.function = function;
        this.nameFile = nameFile;
    }

    private SampleValues sv = new SampleValues();
    private RMSValues rms = new RMSValues();
    private Filter filter = new Fourie(); //фурье
//    private Filter filter = new MiddleValue(); //rms
    private Logic logic = new Logic();
    private OutpuData od = new OutpuData();


    public  double start(double setPoint, int numberFirstGraph, int numberSecondGraph) throws FileNotFoundException {
        if (function.equals("Посчитать уставку")) {
            comtradeName = nameFile;
            path = "C:\\Users\\maksimov\\Desktop\\Algoritms\\Опыты\\Конец линии\\";
            t = false;
        } else {
            comtradeName = nameFile;
            path = "C:\\Users\\maksimov\\Desktop\\Algoritms\\Опыты\\Начало линии\\";

        }

        cfgname = path+comtradeName+".cfg";
        datName = path+comtradeName+".dat";
        comtrCfg = new File(cfgname);
        comtrDat = new File(datName);
        filter.setSv(sv); //объект SV помещаем в объект filter,чтобы получать значения
        filter.setRms(rms);//объект rms помещаем в объект filter,чтобы устанавливать значения
            //открываем cfg файл для получения коэф a и b для расчета y = ax+b
            br = new BufferedReader(new FileReader(comtrCfg));
            int lineNumber =0, count =0, numberData = 100;
            try {
                while ((line=br.readLine())!=null)  {
                    System.out.println(line);
                    lineNumber++;
                    if (lineNumber ==2) {
                        //получаем количество аналоговых сигналов во 2 строке cfg файла "4,3A,1D"
                        numberData = Integer.parseInt(line.split(",")[1].replaceAll("A",""));
                        //создаем double " массивы " с размерностью равной количеству
                        k1 = new double[numberData];
                        k2 = new double[numberData];
                    }
                    //коэф находятся на 3,4,5 строке это 5 и 6 элемент строки при парсинге
                    if (lineNumber>2 && lineNumber <numberData+3) {
                        k1[count] = Double.parseDouble(line.split(",")[5]);
                        k2[count] = Double.parseDouble(line.split(",")[6]);
                        count++;
                    };
                }
                count =0;
                //открываем data файл для получения значений
                br = new BufferedReader(new FileReader(comtrDat));
                while ((line=br.readLine())!=null) {
                    count++;
                    if ((count > 2000 && count < 5000)) {
                        lineData = line.split(",");
                        if (!t) {
                            sv.setPhA(Double.parseDouble(lineData[2]) * k1[0] + k2[0]);
                            sv.setPhB(Double.parseDouble(lineData[3]) * k1[1] + k2[1]);
                            sv.setPhC(Double.parseDouble(lineData[4]) * k1[2] + k2[2]);
                            sv.setTime(Double.parseDouble(lineData[1]));
                            //получаем значение уставки, отправляем функцию, чтобы рассчитать или не рассчитать уставку
                            setPoint = filter.calculate(function,setPoint);
                            // ПЕРЕМЕННАЯ T = true при срабатывании, чтобы токи стали равными нулю, типо выключатель отключил их, если ток выше уставки
                            t = logic.process(rms,setPoint);
                        }
                        else {
                            t = true; //отключение КЗ
                            sv.setPhA(0.);
                            sv.setPhB(0.);
                            sv.setPhC(0.);
                            sv.setTime(Double.parseDouble(lineData[1]));
                            rms.setPhA(0);
                            rms.setPhB(0);
                            rms.setPhC(0);
                            Charts.addDiscreteData(0, true);
                        }
                        //добавляем значения для графиков
                        Charts.addAnalogData(numberFirstGraph, 0, sv.getPhA());
                        Charts.addAnalogData(numberFirstGraph, 1, sv.getPhB());
                        Charts.addAnalogData(numberFirstGraph, 2, sv.getPhC());

                        Charts.addAnalogData(numberSecondGraph, 0, rms.getPhA());
                        Charts.addAnalogData(numberSecondGraph, 1, rms.getPhB());
                        Charts.addAnalogData(numberSecondGraph, 2, rms.getPhC());

                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return setPoint;


    }


}
