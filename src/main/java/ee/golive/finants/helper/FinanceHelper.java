package ee.golive.finants.helper;


public class FinanceHelper {

    public static double pmt(double rate, int number_of_periods, double present_value, double future_value) {
        return ((present_value-future_value)*rate/(1 - Math.pow(1 + rate, number_of_periods)));
    }

    public static double fv(double present_value, int number_of_periods, double rate) {
        return present_value*Math.pow(1+rate, number_of_periods);
    }
}
