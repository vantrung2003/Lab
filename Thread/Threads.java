
public class Threads {
     // Xử lý đa luồng
     public static void main(String[] args) {
          MyThread mThread = new MyThread("ABC");
          mThread.start();

          MyThread mThread1 = new MyThread("DEF");
          mThread1.start();

     }
}

class MyThread implements Runnable {
     private Thread t;
     String ten;

     /**
      * Teen luong , kieu String name
      */
     MyThread(String ten) {
          this.ten = ten;
          System.out.println("running myThread contructor" + ten);

     }

     public void start() {
          System.out.println("Ten toi la: " + ten + "Hehehe");
          if (t == null) {
               t = new Thread(this, ten);
               t.start();
          }
     }

     public void run() {
          System.out.println("Ten toi la: " + ten);

     }

}