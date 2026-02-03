package model;

public class Cashier extends Staff {

    private int registerNumber;

    public Cashier(int id, String name, double salary, int registerNumber) {
        super(id, name, salary, "Cashier");
        setRegisterNumber(registerNumber);
    }

    public int getRegisterNumber() { return registerNumber; }

    public void setRegisterNumber(int registerNumber) {
        if (registerNumber <= 0) {
            throw new IllegalArgumentException("Register number must be positive");
        }
        this.registerNumber = registerNumber;
    }

    @Override
    public void work() {
        System.out.println("Cashier is scanning products at register " + registerNumber);
    }

    @Override
    public String toString() {
        return super.toString() + " [Cashier Register=" + registerNumber + "]";
    }
}