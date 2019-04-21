package AppModel;

import AppService.Logger;
import AppService.SettingManager;
import AppUtil.Lang;
import AppUtil.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static AppUtil.Converter.*;
import static java.time.temporal.ChronoUnit.SECONDS;

public class TableActive extends Table {


    private LocalDateTime TimeFinished;
    private boolean Finished = false;
    private double TotalPrice;

    public TableActive(int id, int tableNum, String course, int kidsNumber, int adultNumber) {
        Id = id;
        TableNum = tableNum;
        Course = byteName(course);
        KidNumber = kidsNumber;
        AdultNumber = adultNumber;
        TimeCreated = LocalDateTime.now();
        TimeStarted = LocalDateTime.now();
    }

    public void setFinished() {
        if (!Finished)
            TimeFinished = LocalDateTime.now();
        Finished = true;
    }

    public boolean isFinished() {
        return Finished;
    }

    private long calExcessSeconds() {
        return TimeStarted.until(TimeFinished.minus(SettingManager.i().getTimeLimit(), SECONDS), SECONDS);
    }

    public long getTime() {
        long time;
        if (SettingManager.i().isLimitTime())
            time = 0 - TimeStarted.until(LocalDateTime.now().minus(SettingManager.i().getTimeLimit(), SECONDS), SECONDS);
        else
            time = TimeStarted.until(LocalDateTime.now(), SECONDS);
        return time;
    }

    public void updateTime() {
        if (!Finished) {
            long time = getTime();

            long times = Math.abs(time);
            String clock;
            if (times > 3600)
                clock = (times / 3600) + " " + Text.HOUR.get() + (times / 3600 > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
            else if (times > 60)
                clock = (times / 60) + " " + Text.MIN.get() + (times / 60 > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");
            else
                clock = (times) + " " + Text.SEC.get() + (times > 1 && SettingManager.i().getLanguage() == Lang.English ? "s" : "");

            if (SettingManager.i().isLimitTime()) {
                if (time > 0)
                    TimeLab.setText(String.format(Text.ACTIVE_REMAIN.get(), clock));
                else
                    TimeLab.setText(String.format(Text.ACTIVE_EXCESS.get(), clock));
            }
            else
                TimeLab.setText(String.format(Text.ACTIVE_ELAPSED.get(), clock));
        }
        else {
            TimeLab.setText(Text.FINISHED.get());
        }
    }

    public String getTxtTime(char mode) {
        // o = over time
        // t = total time (also everything else)
        long time;
        if (!Finished) {
            if (mode == 'o')
                time = 0 - TimeStarted.until(LocalDateTime.now().minus(SettingManager.i().getTimeLimit(), SECONDS), SECONDS);
            else
                time = TimeStarted.until(LocalDateTime.now(), SECONDS);

        }
        else {
            if (mode == 'o')
                time = 0 - TimeStarted.until(TimeFinished.minus(SettingManager.i().getTimeLimit(), SECONDS), SECONDS);
            else
                time = TimeStarted.until(TimeFinished, SECONDS);

        }

        if (mode == 'o' && time > 0)
            return "00: 00: 00";

        long[] times = sepTime(Math.abs(time));
        return String.format("%02d: %02d: %02d", times[0], times[1], times[2]);

    }

    public double calExcessFine() {
        if (calExcessSeconds() > 0)
            return (double) (calExcessSeconds() / SettingManager.i().getTimePerExcess()) * SettingManager.i().getExcessFine();
        else
            return 0;
    }

    public double calAdultsPrice() {
        return AdultNumber * SettingManager.i().getPriceAdult(Course);
    }

    public double calKidsPrice() {
        return KidNumber * SettingManager.i().getPriceKids(Course);
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public void toLog() {
        String log;
        log = String.format("Customer %d (Created on %s)%n" +
                        "Table number: %d%n" +
                        "Course: %s%n" +
                        "Time started: %s%n" +
                        "Time finished: %s%n" +
                        "Kids: %d%n" +
                        "Adults: %d%n" +
                        "Total Price: %f%n",
                Id,
                TimeCreated.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")),
                TableNum,
                strFromByte(Course),
                TimeStarted.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                TimeFinished.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                KidNumber,
                AdultNumber,
                TotalPrice);
        Logger.i().addLog(log);
    }
}
