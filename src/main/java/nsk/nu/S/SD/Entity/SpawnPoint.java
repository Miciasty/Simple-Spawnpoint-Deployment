package nsk.nu.S.SD.Entity;

public class SpawnPoint {

    private final int x;
    private final int y;
    private final int z;

    private int id;

    public SpawnPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    @Override
    public String toString() {
        return "x=<yellow>" + x + "</yellow>, y=<yellow>" + y + "</yellow>, z=<yellow>" + z + "</yellow>";
    }

    public String toVerify() { return "[x=" + x + "_y=" + y + "_z=" + z + "]"; }

    public boolean verify(SpawnPoint sp) {
        return this.toVerify().equals( sp.toVerify() );
    }
}
