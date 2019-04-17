package AppUtil;

import AppService.SettingManager;

public enum Text {
    //SETUP
    START("เริ่มต้น", "Start"),
    //SETTING
    SETTING("ตั้งค่า", "Setting"),
    LANGUAGE("ภาษา", "Language"),
    TABLE("จำนวนโต๊ะ", "Amount of table"),
    TIMELIMIT("จำกัดเวลา", "Time limit"),
    HOUR("ชั่วโมง", "Hour"),
    MIN("นาที", "Minute"),
    SEC("วินาที", "Second"),
    DAY("วัน", "Day"),
    EXCESSFINE("ค่าปรับ", "Excess fine"),
    PER("ต่อ", "Per"),
    SERVICECHARGE("ค่าบริการ", "Service charge"),
    COURSE("คอร์ส", "Course"),
    PRICE("ราคา", "Price"),
    ADULT("ผู้ใหญ่", "Adult"),
    KID("เด็ก", "Kid"),
    BACK("กลับ", "Back"),
    SAVE("บันทึก", "Save"),
    MSG_EMPTY("กรุณากรอกข้อมูลให้ครบ", "Please fill in the information"),
    MSG_NOC("กรุณาระบุประเภทอาหาร", "Please enter course"),
    MSG_MUL("กรุณากรอกข้อมูลให้ถูกต้อง", "Please fill in the correct information"),
    MSG_SUCCESS("บันทึกสำเร็จ", "Save Success"),
    // Table text
    BOOK_REMAIN("เหลืออีก\n%s", "%s\nremaining"),
    BOOK_EXCESS("เกินมา\n%s", "%s\nexcess"),
    ACTIVE_REMAIN("เหลืออีก\n%s", "%s\nremaining"),
    ACTIVE_EXCESS("เกินมา\n%s", "%s\nexcess"),
    ACTIVE_ELAPSED("ผ่านไป\n%s", "%s\nelapsed"),
    FINISHED("คิดเงินแล้ว","Finished"),
    // Main
    MAIN("หน้าหลัก", "Home"),
    ACTIVE("กำลังใช้งาน", "Active"),
    BOOKING("จอง", "Booking"),
    // Sub page
    TTABLE("โต๊ะ", "Table"),
    NAME("ชื่อ", "Name"),
    AMOUNT("จำนวน", "Amount"),
    TIME("เวลา", "Time"),
    CANCEL("ยกเลิก", "Cancel"),
    TOACTIVE("เริ่ม", "Start"),
    OK("ตกลง", "OK"),
    FINE("ค่าปรับ", "Fine"),
    OTHER("อื่นๆ", "Other"),
    TOTAL("ยอดสุทธิ", "Total"),
    STOP("คิดเงิน", "Check bill"),
    END("เสร็จสิ้น", "Finish"),
    ERR_PEMPTY("กรุณากรอกข้อมูลให้ครบ", "Please fill in the information"),
    ERR_TIMEIN("กรุณากรอกเวลาให้ถูกต้อง", "Please enter the correct time"),
    ERR_MUL("กรุณากรอกข้อมูลให้ถูกต้อง", "Please fill in the correct information"),
    ERR_TEMPTY("โต๊ะหมด", "Table Full"),
    MSG_STOPCLOCK("(คิดเงิน)", "(Check bill)"),
    ERR_TINVALID("กรุณากรอกโต๊ะให้ถูกต้อง", "Please enter the correct table");

    private final String THTxt;
    private final String ENTxt;

    Text(String THTxt, String ENTxt) {
        this.THTxt = THTxt;
        this.ENTxt = ENTxt;
    }

    public String get() {
        if (SettingManager.i().getLanguage() == Lang.Thai)
            return THTxt;
        else
            return ENTxt;
    }
}
