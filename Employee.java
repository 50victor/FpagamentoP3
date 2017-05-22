package src;

import src.PaySchedule;

public class Employee {
    public String name;
    public float salary;
    public char salaryType; //[H]ourly, [S]alaried, [C]omissioned
    public float commission;
    public int identification;
    public String address;
    public boolean unionized;
    public int unionID;
    public float hoursWorked;
    public float extraHoursWorked;
    public int daysWorked;
    public float totalCommission;
    public float unionTax;
    public float unionServicesTax;
    public int lastDayPaid;
    public int lastMonthPaid;
    public int paymentReception;
    public String[] paymentReceptionStrings = {"Bank Check", "Bank Check via Mail", "Bank Deposit"};
    public PaySchedule paySchedule;
}
