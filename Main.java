package src;
import java.util.Scanner;

public class Main {
    static Employee undo = new Employee();
    static int undoPosition;
    static PaySchedule[] schedules = new PaySchedule[20];

    public static void main(String[] args) {
        Employee[] employees = new Employee[50];
        int option = 1;
        initiateDefaultSchedule();
        while(option != 0){
            option = menu();
            if(option != 0){
                optionSwitcher(option, employees);
            }
        }
    }

    public static int menu(){
        System.out.println("Select which action you want to perform");
        System.out.println("1. Add employee.");
        System.out.println("2. Remove employee.");
        System.out.println("3. Add a timecard.");
        System.out.println("4. Add sales result.");
        System.out.println("5. Add a service tax.");
        System.out.println("6. Change employee's data.");
        System.out.println("7. Set payment wall for today. ");
        System.out.println("8. Undo/Redo.");
        System.out.println("9. Payment wall.");
        System.out.println("10. Creation of a new payments wall.");
        System.out.println("11. List all current registered employees.");
        System.out.println("0. Sair.");
        Scanner readInput = new Scanner(System.in);

        return readInput.nextInt();
    }

    public static void initiateDefaultSchedule(){
        schedules[0] = new PaySchedule();
        schedules[0].method = 'M';
        schedules[0].monthDay = 30;
        schedules[1] = new PaySchedule();
        schedules[1].method = 'S';
        schedules[1].weekDay = 6;
        schedules[2] = new PaySchedule();
        schedules[2].method = 'B';
        schedules[2].weekDay = 6;
    }

    public static void optionSwitcher(int option, Employee[] array){
        switch (option){
            case 1:
                registerEmployee(array);
                break;
            case 2:
                removeEmployee(array);
                break;
            case 3:
                insertTimecard(array);
                break;
            case 4:
                registerSale(array);
                break;
            case 5:
                unionServices(array);
                break;
            case 6:
                editEmployee(array);
                break;
            case 7:
                payToday(array);
                break;
            case 8:
                undoFunction(array);
                break;
            case 9:
                paymentScheduler(array);
                break;
            case 10:
                customScheduler();
                break;
            case 11:
                displayAllEmployees(array);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    public static int checkAvailablePosition(Employee[] array){
        int available = -1;
        for(int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                available = i;
                break;
            }
        }
        if(available != -1) return available;
        else {
            System.out.println("Nenhuma posiÃ§Ã£o disponÃ­vel.");
            return -1;
        }
    }

    public static Employee[] registerEmployee(Employee[] array){
        Scanner input = new Scanner(System.in);

        System.out.println("Please, insert employee's name.");
        String name = input.nextLine();

        System.out.println("Please, insert the type of payment.\n(H)--HOURLY, (S)--SALARY, (C)--COMISSIONED.");
        char type = input.next().charAt(0);

        float commission = 0;
        if(type == 'C' || type == 'c'){
            System.out.println("Please, insert the comission");
            commission = input.nextFloat();
        }

        System.out.println("Please, insert the salary.");
        float salary = input.nextFloat();


        int position = checkAvailablePosition(array);
        array[position] = addEmployee(name, salary, type, commission, position);
        undo = null;
        undoPosition = position;

        System.out.println("\nEmployee succesfully added!!\nID: " + position +
                "\nReturning to the main menu...");

        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return array;
    }

    public static Employee addEmployee(String name, float salary, char type, float commission, int identification){
        Employee added = new Employee();
        added.name = name;
        added.salary = salary;
        added.salaryType = type;
        added.commission = commission;
        added.identification = identification;
        added.paymentReception = 2;

        if(type == 'c' || type == 'C'){
            added.paySchedule = schedules[2];
        } else if (type == 'h' || type == 'H'){
            added.paySchedule = schedules[1];
        } else {
            added.paySchedule = schedules[0];
        }

        return added;
    }

    public static Employee[] removeEmployee(Employee[] array){
        System.out.println("Insert the ID of the employee you wish to remove.");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();
        undo = array[id];
        undoPosition = id;
        array[id] = null;

        return array;
    }

    public static Employee[] editEmployee(Employee[] array){
        System.out.println("Inser the ID of the employee you wish to edit. Negative values will return to the main menu");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();
        if(id < 0) return array;
        if(array[id] == null){
            System.out.println("Employee not available!");
            return array;
        }
        else {
            int option = 1;
            while (option != 0) {
                System.out.println("Which data you wish to edit from " + array[id].name + "?");
                System.out.println("1. Name");
                System.out.println("2. Salary");
                System.out.println("3. Type");
                System.out.println("4. Comission");
                System.out.println("5. Address");
                System.out.println("6. Unionized");
                System.out.println("0. Main Menu");

                option = scan.nextInt();
                scan.nextLine();
                switch (option) {
                    case 1:
                        System.out.println("Type new name.");
                        array[id].name = scan.nextLine();
                        break;
                    case 2:
                        System.out.println("Insert new salary.");
                        array[id].salary = scan.nextFloat();
                        System.out.println("Which payment method to use? (0) Bank Check; (1) Bank Check via Mail; or (2) Deposit?");
                        int aux = scan.nextInt();
                        if(aux < 0 || aux > 2){
                            System.out.println("Invalid option. Payment method.");
                        } else {
                            array[id].paymentReception = aux;
                        }
                        break;
                    case 3:
                        System.out.println("Insert new value. (H) para hourly, (S) para salary, (C) para comission.");
                        array[id].salaryType = scan.next().charAt(0);
                        break;
                    case 4:
                        if (array[id].salaryType == 'c' || array[id].salaryType == 'C') {
                            System.out.println("Type the commision percentage.");
                            array[id].commission = scan.nextFloat();
                        } else {
                            System.out.println("Non comissioned employee.");
                        }
                        break;
                    case 5:
                        System.out.println("Type the address.");
                        array[id].address = scan.nextLine();
                        break;
                    case 6:
                        System.out.print("Unionized? (Y/N)");
                        char input = scan.next().charAt(0);
                        if (input == 'Y' || input == 'y') {
                            array[id].unionized = true;
                            System.out.println("Insert the employee's union ID.");
                            array[id].unionID = scan.nextInt();
                            System.out.println("Insert union tax.");
                            array[id].unionTax = scan.nextFloat();
                        } else {
                            array[id].unionTax = 0;
                            array[id].unionized = false;
                        }
                        break;
                    default:
                        break;
                }
            }
            return array;
        }
    }

    public static void insertTimecard(Employee[] array){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert the employee's ID.");
        int id = scan.nextInt();
        if(array[id].salaryType == 'h' || array[id].salaryType == 'H'){
            System.out.println("Type the amount of work hours.");
            float hours = scan.nextFloat();

            if(hours < 0) System.out.println("Invalid value.");
            else if(hours > 8){
                array[id].hoursWorked += 8;
                array[id].extraHoursWorked += (hours - 8);
            } else {
                array[id].hoursWorked += hours;
            }
        } else {
            System.out.println("Employee present? 1. Yes; 0. No");
            int input = scan.nextInt();
            if(input == 1){
                array[id].daysWorked = array[id].daysWorked + 1;
            }
        }
    }

    public static void registerSale(Employee[] array){
        Scanner scan = new Scanner(System.in);

        System.out.println("Inser the sales' value.");
        float price = scan.nextFloat();
        System.out.println("Insert the ID of the employee.");
        int id = scan.nextInt();

        if(array[id].salaryType == 'c' || array[id].salaryType == 'C'){
            array[id].totalCommission += price*(array[id].commission/100);
        }
    }

    public static void unionServices(Employee[] array){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert the employee's union ID.");
        int id = scan.nextInt();
        int pos = 0;
        boolean found = false;
        for(int i = 0; i < array.length; i++){
            if(array[i].unionID == id){
                found = true;
                pos = i;
                break;
            }
        }
        if(!found) System.out.println("Employee not available.");
        else{
            System.out.println("Employee "+array[pos].name+" Found. Type the cost of the service.");
            float value = scan.nextFloat();
            array[pos].unionServicesTax += value;
        }
    }

    public static int differenceOfDays(int day1, int month1, int day2, int month2){
        int aux1 = 365*2017 + 2017/4 - 2017/100 + 2017/400 + (month1*306 + 5)/10 + (day1 - 1 );
        int aux2 = 365*2017 + 2017/4 - 2017/100 + 2017/400 + (month2*306 + 5)/10 + (day2 - 1 );
        return Math.abs(aux1 - aux2);
    }

    public static Employee[] payToday(Employee[] array){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert today's day of the month.");
        int today = scan.nextInt();
        System.out.println("Insert the month with 1 being January; e 12, December.");
        int month = scan.nextInt();

        System.out.println("Insert the week day. With 1 being Sunday, and 7 Saturday.");
        int weekDay = scan.nextInt();

        for(int i = 0; i < array.length; i++){
            array[i] = pay(array[i], today, month, weekDay);
        }

        return array;
    }

    public static Employee pay(Employee employee, int today, int month, int weekDay){
        float amount = 0;
        if(employee != null){
            if(employee.unionized){
                amount -= employee.unionTax - employee.unionServicesTax;
                employee.unionServicesTax = 0;
            }
            if(employee.paySchedule.method == 'm' || employee.paySchedule.method == 'M'){
                if(employee.paySchedule.monthDay == today){
                    if(employee.salaryType == 'H' || employee.salaryType == 'h'){
                        amount += employee.hoursWorked*employee.salary;
                        amount += employee.extraHoursWorked*employee.salary*1.5;
                        employee.hoursWorked = 0;
                        employee.extraHoursWorked = 0;
                    } else if(employee.salaryType == 'C' || employee.salaryType == 'c'){
                        amount += employee.daysWorked*(1.0/30.0)*employee.salary;
                        amount += employee.totalCommission;
                        employee.totalCommission = 0;
                        employee.daysWorked = 0;
                    } else if(employee.salaryType == 'S' || employee.salaryType == 's'){
                        amount += employee.daysWorked*(1.0/30.0)*employee.salary;
                        employee.daysWorked = 0;
                    }
                    employee.lastDayPaid = today;
                    employee.lastMonthPaid = month;
                    if(amount > 0){
                        System.out.println("\nO funcionÃ¡rio "+ employee.name +" recebeu o pagamento de "+ amount +" reais.\n");
                    } else if(amount < 0){
                        System.out.println("\nO funcionÃ¡rio "+ employee.name +" adquiriu uma dÃ­vida de "
                                + Math.abs(amount) +" reais com o sindicato.\n");
                    } else {
                        System.out.println("\nO funcionÃ¡rio "+ employee.name +" nÃ£o recebeu nada.\n");
                    }
                    try {
                        Thread.sleep(800);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if((employee.paySchedule.method == 's' || employee.paySchedule.method == 'S')
                    && weekDay == employee.paySchedule.weekDay){
                if(employee.salaryType == 'H' || employee.salaryType == 'h'){
                    amount += employee.hoursWorked*employee.salary;
                    amount += employee.extraHoursWorked*employee.salary*1.5;
                    employee.hoursWorked = 0;
                    employee.extraHoursWorked = 0;
                } else if(employee.salaryType == 'C' || employee.salaryType == 'c'){
                    amount += employee.daysWorked*(1/30)*employee.salary;
                    amount += employee.totalCommission;
                    employee.totalCommission = 0;
                    employee.daysWorked = 0;
                } else if(employee.salaryType == 'S' || employee.salaryType == 's'){
                    amount += employee.daysWorked*(1/30)*employee.salary;
                    employee.daysWorked = 0;
                }
                employee.lastDayPaid = today;
                employee.lastMonthPaid = month;
                if(amount > 0){
                    System.out.println("The employee "+ employee.name +" received a payment of "+ amount +" R$.");
                } else if(amount < 0){
                    System.out.println("The employee "+ employee.name +" has acquired a debt of "
                            + Math.abs(amount) +" R$ with the union.");
                } else {
                    System.out.println("\nThe employee "+ employee.name +" didn't receive.\n");
                }
            }

            if((employee.paySchedule.method == 'b' || employee.paySchedule.method == 'B')
                    && (weekDay == employee.paySchedule.weekDay)
                    && (differenceOfDays(today, month, employee.lastDayPaid, employee.lastMonthPaid) >= 14)){
                if(employee.salaryType == 'H' || employee.salaryType == 'h'){
                    amount += employee.hoursWorked*employee.salary;
                    amount += employee.extraHoursWorked*employee.salary*1.5;
                    employee.hoursWorked = 0;
                    employee.extraHoursWorked = 0;
                } else if(employee.salaryType == 'C' || employee.salaryType == 'c'){
                    amount += employee.daysWorked*(1/30)*employee.salary;
                    amount += employee.totalCommission;
                    employee.totalCommission = 0;
                    employee.daysWorked = 0;
                } else if(employee.salaryType == 'S' || employee.salaryType == 's'){
                    amount += employee.daysWorked*(1/30)*employee.salary;
                    employee.daysWorked = 0;
                }
                employee.lastDayPaid = today;
                employee.lastMonthPaid = month;
                if(amount > 0){
                    System.out.println("The employee "+ employee.name +" received the payment of "+ amount +" R$.");
                } else if(amount < 0){
                    System.out.println("The employee "+ employee.name +" has acquired a debt of "
                            + Math.abs(amount) +" R$ with the union.");
                } else {
                    System.out.println("The employee "+ employee.name +" didn't receive.");
                }
            }
        }
        return employee;
    }

    public static Employee undoFunction(Employee[] array){
        Employee redo = undo;
        undo = array[undoPosition];
        array[undoPosition] = redo;

        return undo;
    }

    public static void paymentScheduler(Employee[] array){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert employee ID.");
        int id = scan.nextInt();
        System.out.println("Choose the payment method of " + array[id].name);

        for(int i = 0; i < schedules.length; i++){
            if(schedules[i] != null){
                if(schedules[i].method == 'm' || schedules[i].method == 'M'){
                    System.out.println(i +". Mensal " + schedules[i].monthDay);
                }
                else if(schedules[i].method == 's' || schedules[i].method == 'S'){
                    System.out.println(i +". Semanal " + schedules[i].weekDay);
                }
                else if(schedules[i].method == 'b' || schedules[i].method == 'B'){
                    System.out.println(i +". Bi-semanal " + schedules[i].weekDay);
                }
            }
        }

        int input = scan.nextInt();
        array[id].paySchedule = schedules[input];
    }

    public static void customScheduler(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert new payment wall with the following scheme:");
        System.out.println("If monthly, type 'Monthly' and next, the day of the month. Ex: Monthly 5");
        System.out.println("If weekly, type 'Weekly' and next, the day of the week; With 2 being monday, and 6, friday.\nEx: Weekly 6");
        System.out.println("If bi-weekly, same thing of 'Weekly', but type 'Bi-weekly' instead.\nEx: Bi-weekly 2");
        char type = scan.next().charAt(0);
        int numAux = scan.nextInt();

        if(type == 'm' || type == 'M'){
            if(numAux > 0 && numAux < 31){
                for(int i = 0; i < schedules.length; i++){
                    if(schedules[i] == null){
                        schedules[i] = new PaySchedule();
                        schedules[i].monthDay = numAux;
                        schedules[i].method = type;
                        break;
                    }
                }
            } else {
                System.out.println("Non valid day inserted. The day must vary from 1 to 30.");
            }
        }

        if(type == 'S' || type == 's' || type == 'B' || type == 'b'){
            if(numAux > 0 && numAux < 7){
                for(int i = 0; i < schedules.length; i++){
                    if(schedules[i] == null){
                        schedules[i] = new PaySchedule();
                        schedules[i].weekDay = numAux;
                        schedules[i].method = type;
                        break;
                    }
                }
            } else {
                System.out.println("Non valid day inserted. The day must vary from 1 to 6.");
            }
        }
    }

    public static void displayAllEmployees(Employee[] array){
        for(int i = 0; i < array.length; i++){
            if(array[i] != null){
                System.out.println("ID: "+ i);
                System.out.println("Nome: "+ array[i].name);
                if(array[i].salaryType == 'c' || array[i].salaryType == 'C') System.out.println("Comissioned.");
                else if(array[i].salaryType == 'h' || array[i].salaryType == 'H') System.out.println("Hourly.");
                else if(array[i].salaryType == 's' || array[i].salaryType == 'S') System.out.println("Wage.");
                System.out.println("Salary: "+ array[i].salary);
                if(array[i].unionized) System.out.println("Unionized.");
                if(array[i].paySchedule.method == 'S' || array[i].paySchedule.method == 's'){
                    System.out.println("Receives weekly "+ array[i].paySchedule.weekDay+"week day.");
                }
                if(array[i].paySchedule.method == 'B' || array[i].paySchedule.method == 'b'){
                    System.out.println("Receives each two weeks on the "+ array[i].paySchedule.weekDay+"th week day.");
                }
                if(array[i].paySchedule.method == 'm' || array[i].paySchedule.method == 'M'){
                    System.out.println("Receives on day "+ array[i].paySchedule.monthDay+" of each month.");
                }
                System.out.println("Receives by "+ array[i].paymentReceptionStrings[array[i].paymentReception]);
                System.out.println();
            }
        }
    }
}