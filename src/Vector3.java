public class Vector3 {
    public static final double PI = Math.acos(-1);
    private double x, y, z;
    private double modulus;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        modulus = Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        modulus = Math.sqrt(x * x + y * y + z * z);
    }

    public double getModulus() {
        return modulus;
    }


    public int calcAngle(Vector3 vector3) {
        System.out.println(Math.acos((x * vector3.x + y * vector3.y) / (modulus * vector3.getModulus())));
        int angle = (int) ((180 / PI) * Math.acos((x * vector3.x + y * vector3.y) / (modulus * vector3.getModulus())));

        return angle;
    }

    public String print() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vector3 normalization() {
        return new Vector3(x / modulus, y / modulus, z / modulus);
    }

}
