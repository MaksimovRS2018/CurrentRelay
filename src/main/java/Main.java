import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        double coef = 1.1; //коэф. отстройки
        //график № 1  для построения мгновенных токов при КЗ в конце линии
        Charts.createAnalogChart("мгн. токи для расчета уставок",0);
        Charts.addSeries("Фаза А", 0, 0);
        Charts.addSeries("Фаза B", 0, 1);
        Charts.addSeries("Фаза С", 0, 2);
        //график № 2  для построения действующих значений токов при КЗ в конце линии
        Charts.createAnalogChart("rms токи для расчета уставок",1);
        Charts.addSeries("Фаза А rms", 1, 0);
        Charts.addSeries("Фаза B rms", 1, 1);
        Charts.addSeries("Фаза С rms ", 1, 2);
        //график № 3  для построения мгновенных токов при КЗ в начале линии
        Charts.createAnalogChart("Токи КЗ",2);
        Charts.addSeries("Фаза А", 2, 0);
        Charts.addSeries("Фаза B", 2, 1);
        Charts.addSeries("Фаза С", 2, 2);
        //график № 4  для построения действующих значений токов при КЗ в начале линии
        Charts.createAnalogChart("Rms токи",3);
        Charts.addSeries("Фаза А rms", 3, 0);
        Charts.addSeries("Фаза B rms", 3, 1);
        Charts.addSeries("Фаза С rms ", 3, 2);
        //график № 5 для сигнала срабатывания защиты
        Charts.createDiscreteChart("Trip",4);
        //найдем из комтрейд файла максимальное значение действующего тока при кз в конце линии для расчета уставки ТО
        InputData inD1 = new InputData("Посчитать уставку","PhABC80");
        double getMax = inD1.start(0.,0,1);
        //расчет уставки ТО
        double setPoint = getMax*coef;
        //добавляем для 4 графика сигнал - уставку
        Charts.addSeries("Уставка",3,3);
        System.out.println("Уставка = " + setPoint); //вывод уставки в консоль
        //создаем объект класса inpudata и проверяем ТО с рассчитанной уставкой при кз в начале линии
        InputData inD2 = new InputData("Проверить ТО","PhA80");
        inD2.start(setPoint,2,3);
    }
}
