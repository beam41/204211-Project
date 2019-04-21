package AppService;

import AppModel.Price;
import AppUtil.Lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static AppUtil.Converter.byteName;
import static AppUtil.Converter.byteNameConc;

public class SettingManager {

    private static SettingManager ourInstance = new SettingManager();
    private Lang Language = Lang.English;
    // -1 indicate not set yet
    private int TableCount = -1;
    private boolean LimitTime;
    private long TimeLimit = -1;
    private long TimePerExcess = -1;
    private double ExcessFine = -1;
    private double ServiceCharge = -1;
    private LinkedHashMap<String, Price> PriceMap = new LinkedHashMap<>();
    private SettingManager() {
    }

    public static SettingManager i() {
        return ourInstance;
    }

    static void setInstance(SettingManager newInstance) {
        ourInstance = newInstance;
    }

    private void updateFile() {
        SaverAndLoader.saveTo(SettingManager.i(), "settings.json");
    }

    //region getter setter

    public Lang getLanguage() {
        return Language;
    }

    public void setLanguage(Lang language) {
        Language = language;
        updateFile();
    }

    public int getTableCount() {
        return TableCount;
    }

    public void setTableCount(int tableCount) {
        TableCount = tableCount;
        updateFile();
    }

    public long getTimeLimit() {
        return TimeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        TimeLimit = timeLimit;
        updateFile();
    }

    public long getTimePerExcess() {
        return TimePerExcess;
    }

    public void setTimePerExcess(long timeExcess) {
        TimePerExcess = timeExcess;
        updateFile();
    }

    public double getExcessFine() {
        return ExcessFine;
    }

    public void setExcessFine(double excessFine) {
        ExcessFine = excessFine;
        updateFile();
    }

    public double getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        ServiceCharge = serviceCharge;
        updateFile();
    }

    public boolean isLimitTime() {
        return LimitTime;
    }

    public void setLimitTime(boolean limitTime) {
        LimitTime = limitTime;
    }

    public ArrayList<String> getPriceNameArr() {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, Price> p: PriceMap.entrySet()) {
            arr.add(p.getValue().getName());
        }
        return arr;
    }

    public double getPriceAdult(String name) {
        return PriceMap.get(byteNameConc(byteName(name))).getPriceAdult();
    }

    public double getPriceKids(String name) {
        return PriceMap.get(byteNameConc(byteName(name))).getPriceKids();
    }

    public double getPriceAdult(byte[] name) {
        return PriceMap.get(byteNameConc(name)).getPriceAdult();
    }

    public double getPriceKids(byte[] name) {
        return PriceMap.get(byteNameConc(name)).getPriceKids();
    }

    public void addPrice(String name, double priceAdult, double priceKids) {
        Price newPrice = new Price(byteName(name), priceKids, priceAdult);
        PriceMap.put(byteNameConc(byteName(newPrice.getName())), newPrice);
        updateFile();
    }

    public void clearPrice() {
        PriceMap.clear();
        updateFile();
    }
    //endregion


}
