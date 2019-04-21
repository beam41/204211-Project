package AppModel;

import AppService.SettingManager;
import AppUtil.Lang;
import AppUtil.Text;

import java.time.LocalDateTime;

import static AppUtil.Converter.byteName;
import static AppUtil.Converter.strFromByte;
import static java.time.temporal.ChronoUnit.SECONDS;

public class TableBooking extends Table {

    private LocalDateTime TimeCheckin;
    private byte[] customerName;

    public TableBooking(int id, int tableNum, String course, int kidsNumber, int adultNumber, LocalDateTime timeCheckin, String customerName) {
        Id = id;
        TableNum = tableNum;
        Course = byteName(course);
        KidNumber = kidsNumber;
        AdultNumber = adultNumber;
        TimeCheckin = timeCheckin;
        TimeCreated = LocalDateTime.now();
        this.customerName = byteName(customerName);
    }

    public String getCustomerName() {
        return strFromByte(customerName);
    }

    public long getTime() {
        return LocalDateTime.now().until(TimeCheckin, SECONDS);
    }

    public void updateTime() {
        long time = getTime();

        long times = Math.abs(time);
        String clock;
        if (time > 86400) {
            clock = (times / 86400) + " " + Text.DAY.get() + (times / 86400 > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
        }
        else if (times > 3600)
            clock = (times / 3600) + " " + Text.HOUR.get() + (times / 3600 > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
        else if (times > 60)
            clock = (times / 60) + " " + Text.MIN.get() + (times / 60 > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
        else
            clock = (times) + " " + Text.SEC.get() + (times > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
        if (time > 0)
            TimeLab.setText(String.format(Text.BOOK_REMAIN.get(), clock));
        else
            TimeLab.setText(String.format(Text.BOOK_EXCESS.get(), clock));
    }

    public String getCheckinDate() {
        return String.format("%d/%d/%d", TimeCheckin.getDayOfMonth(), TimeCheckin.getMonthValue(), TimeCheckin.getYear());
    }

    public String getCheckinTime() {
        return String.format("%02d:%02d", TimeCheckin.getHour(), TimeCheckin.getMinute());
    }
}
