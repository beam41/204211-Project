package AppService;

import AppModel.Table;
import AppModel.TableActive;
import AppModel.TableBooking;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.*;

public class TableManager {

    private static TableManager ourInstance = new TableManager();
    private Random random = new Random();
    private LinkedHashMap<Integer, TableActive> tableActives = new LinkedHashMap<>();
    private LinkedHashMap<Integer, TableBooking> tableBookings = new LinkedHashMap<>();

    private TreeSet<Integer> tableNumSet = new TreeSet<>();

    private TableManager() {
        reloadTableNum();
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> clockUpdate()), new KeyFrame(Duration.millis(500)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public static TableManager i() {
        return ourInstance;
    }

    private void updateFile() {
        ArrayList<TableActive> saveActive = new ArrayList<>(tableActives.values());
        SaverAndLoader.saveTo(saveActive.toArray(), "tableActives.json");

        ArrayList<TableBooking> saveBooking = new ArrayList<>(tableBookings.values());
        SaverAndLoader.saveTo(saveBooking.toArray(), "tableBookings.json");
    }

    public void getFile() {
        try {
            for (TableActive table : Objects.requireNonNull(SaverAndLoader.tableGetFromAct())) {
                tableNumSet.remove(table.getTableNum());
                tableActives.put(table.getId(), table);
            }
            for (TableBooking table : Objects.requireNonNull(SaverAndLoader.tableGetFromBook())) {
                tableNumSet.remove(table.getTableNum());
                tableBookings.put(table.getId(), table);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reloadTableNum() {
        tableNumSet.clear();
        for (int i = 1; i <= SettingManager.i().getTableCount(); i++)
            tableNumSet.add(i);
        for (Map.Entry<Integer, TableActive> table : tableActives.entrySet()) {
            tableNumSet.remove(table.getValue().getTableNum());
        }
        for (Map.Entry<Integer, TableBooking> table : tableBookings.entrySet()) {
            tableNumSet.remove(table.getValue().getTableNum());
        }
    }

    public void newTableActive(int tableNum, String course, int kidsNumber, int adultNumber) {
        tableNumSet.remove(tableNum);
        TableActive table = new TableActive(genId(), tableNum, course, kidsNumber, adultNumber);
        tableActives.put(table.getId(), table);
        updateFile();
    }

    public void newTableBooking(int tableNum, String course, String customerName, int kidsNumber, int adultNumber, LocalDateTime timeCheckin) {
        tableNumSet.remove(tableNum);
        TableBooking table = new TableBooking(genId(), tableNum, course, kidsNumber, adultNumber, timeCheckin, customerName);
        tableBookings.put(table.getId(), table);
        updateFile();
    }

    public void delTableActive(int id) {
        if (tableActives.containsKey(id)) {
            tableNumSet.add(tableActives.get(id).getTableNum());
            tableActives.remove(id);
            updateFile();
        }
    }

    public void delTableBooking(int id) {
        if (tableBookings.containsKey(id)) {
            tableNumSet.add(tableBookings.get(id).getTableNum());
            tableBookings.remove(id);
            updateFile();
        }
    }

    public Table findById(int id) {
        Table table = null;
        if (tableActives.containsKey(id)) {
            table = tableActives.get(id);
        }
        if (tableBookings.containsKey(id)) {
            table = tableBookings.get(id);
        }
        return table;
    }

    public void startTable(int id) {
        TableBooking table = tableBookings.get(id);
        tableBookings.remove(id);
        tableActives.put(table.getId(), table.toActive());
        updateFile();
    }

    public LinkedHashMap<Integer, TableActive> getTableActives() {
        return tableActives;
    }

    public LinkedHashMap<Integer, TableBooking> getTableBookings() {
        return tableBookings;
    }

    public TreeSet<Integer> getTableNumSet() {
        return tableNumSet;
    }

    public boolean tableEmpty() {
        return tableNumSet.size() == 0;
    }

    public boolean tableExist(int num) {
        return tableNumSet.contains(num);
    }

    private int genId() {
        return random.nextInt() + random.nextInt() + random.nextInt();
    }

    private void clockUpdate() {
        for (TableActive table : tableActives.values()) {
            table.updateTime();
        }
        for (TableBooking table : tableBookings.values()) {
            table.updateTime();
        }
    }
}
