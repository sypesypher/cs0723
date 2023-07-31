import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class RentalAgreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private int dailyRentalCharge;
    private int chargeDays;
    private int preDiscountCharge;
    private int discountPercent;
    private int discountAmount;
    private int finalCharge;

    private Map<String,Tool> tools = new HashMap<>() {{
        put("CHNS", new Tool("CHNS","Chainsaw","Stihl"));
        put("LADW", new Tool("LADW","Ladder","Werner"));
        put("JAKD", new Tool("JAKD","Jackhammer","DeWalt"));
        put("JAKR", new Tool("JAKR","Jackhammer","Ridgid"));
    }};

    private Map<String,ToolType> toolTypes = new HashMap<String, ToolType>() {{
        put("Ladder", new ToolType("Ladder", 199, true, true, false));
        put("Chainsaw",new ToolType("Chainsaw", 149, true, false, true));
        put("Jackhammer",new ToolType("Jackhammer", 299, true, false, false));
    }};

    public RentalAgreement(String toolCode, int rentalDays, LocalDate checkoutDate, int discountPercent) {

        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental Days must be greater than 1");
        }
        this.rentalDays = rentalDays;

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Invalid Discount Percent, Discount percentage must be between 0 and 100 (Percent %)");
        }
        this.discountPercent = discountPercent;

        this.toolCode = toolCode;
        Tool thisTool = tools.get(toolCode);
        if (thisTool == null) {
            throw new IllegalArgumentException("Invalid Tool Code/Tool Code Not found");
        } else {
            this.toolType = thisTool.getToolType();
            this.toolBrand = thisTool.getToolBrand();

        }

        this.checkoutDate = checkoutDate;
        this.dueDate = calculateDueDate();
        this.dailyRentalCharge = getDailyRentalCharge();
        this.chargeDays = calculateChargeDays();
        this.preDiscountCharge = calculatePreDiscountCharge();
        this.discountAmount = calculateDiscountAmount();
        this.finalCharge = calculateFinalCharge();
    }

    public LocalDate calculateDueDate() {
        return checkoutDate.plusDays(rentalDays);
    }

    private int getDailyRentalCharge() {
        return toolTypes.get(this.toolType).getDailyCharge();
    }

    public int calculateChargeDays() {
        int chargeDays = 0;

        for (LocalDate date = checkoutDate.plusDays(1); date.isBefore(dueDate.plusDays(1)); date = date.plusDays(1)) {
            if (isWeekend(date)) {
                if (toolTypes.get(toolType).isWeekendCharge()) {
                    chargeDays++;
                }
                continue;
            }
            if (isHoliday(date)) {
                if (toolTypes.get(toolType).isHolidayCharge()) {
                    chargeDays++;
                }
            }
            else {
                if (toolTypes.get(toolType).isWeekdayCharge()) {
                    chargeDays++;
                }
            }
        }
        return chargeDays;
    }

    public int calculatePreDiscountCharge() {
        return chargeDays * dailyRentalCharge;
    }

    public int calculateDiscountAmount() {
        double discount =  (double) preDiscountCharge * (double) discountPercent / 100;
        int roundedCent = (int) Math.round(discount);
        return roundedCent;
    }

    public int calculateFinalCharge() {
        return preDiscountCharge - discountAmount;
    }

    private boolean isHoliday(LocalDate date) {
		int year = date.getYear();

		LocalDate july4th = LocalDate.of(year, Month.JULY, 4);
        if (july4th.getDayOfWeek() == DayOfWeek.SATURDAY) {
            july4th = LocalDate.of(year,Month.JULY, 3);
        } else if (july4th.getDayOfWeek() == DayOfWeek.SUNDAY) {
            july4th = LocalDate.of(year,Month.JULY,5);
        }

        LocalDate firstMondayOfMonth = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));


		if ((date.equals(firstMondayOfMonth) && date.getMonth() == Month.SEPTEMBER) || date.equals(july4th)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isWeekend(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return true;
        } else {
            return false;
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Tool code: ").append(toolCode).append("\n");
        sb.append("Tool type: ").append(toolType).append("\n");
        sb.append("Tool brand: ").append(toolBrand).append("\n");
        sb.append("Rental days: ").append(rentalDays).append("\n");
        sb.append("Checkout date: ").append(checkoutDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"))).append("\n");
        sb.append("Due date: ").append(dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"))).append("\n");
        sb.append("Daily rental charge: ").append(NumberFormat.getCurrencyInstance().format(dailyRentalCharge / 100.0)).append("\n");
        sb.append("Charge days: ").append(chargeDays).append("\n");
        sb.append("Pre-discount charge: ").append(NumberFormat.getCurrencyInstance().format(preDiscountCharge / 100.0)).append("\n");
        sb.append("Discount percent: ").append(discountPercent).append("%").append("\n");
        sb.append("Discount amount: ").append(NumberFormat.getCurrencyInstance().format(discountAmount / 100.0)).append("\n");
        sb.append("Final charge: ").append(NumberFormat.getCurrencyInstance().format(finalCharge / 100.0)).append("\n");

        return sb.toString();
    }

    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter tool code: ");
            String toolCode = scanner.nextLine();

            System.out.println("Enter rental days: ");
            int rentalDays = scanner.nextInt();

            System.out.println("Enter checkout date (MM/dd/yyyy): ");
            String checkoutDateString = scanner.next();
            LocalDate checkoutDate = LocalDate.parse(checkoutDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            System.out.println("Enter discount percent: ");
            int discountPercent = scanner.nextInt();

            RentalAgreement agreement = new RentalAgreement(toolCode, rentalDays, checkoutDate, discountPercent);
            System.out.println();
            System.out.println(agreement.toString());
            System.out.println();
        }
    }

    public static class Tool {
        private String toolcode;
        private String toolType;
        private String brand;

        public Tool(String toolcode, String toolType, String brand) {
            this.toolcode = toolcode;
            this.toolType = toolType;
            this.brand = brand;
        }

        public String getToolType() {
            return this.toolType;
        }

        public String getToolBrand() {
            return this.brand;
        }
    }

    public static class ToolType {
        private String name;
        private int dailyCharge;
        private boolean weekdayCharge;
        private boolean weekendCharge;
        private boolean holidayCharge;

        public ToolType(String name, int dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
            this.name = name;
            this.dailyCharge = dailyCharge;
            this.weekdayCharge = weekdayCharge;
            this.weekendCharge = weekendCharge;
            this.holidayCharge = holidayCharge;
        }

        public int getDailyCharge() {
            return dailyCharge;
        }

        public boolean isWeekdayCharge() {
            return weekdayCharge;
        }

        public boolean isWeekendCharge() {
            return weekendCharge;
        }

        public boolean isHolidayCharge() {
            return holidayCharge;
        }
    }
}
