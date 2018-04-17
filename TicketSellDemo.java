package com.fusong.thread;

/**
 * Created by Administrator on 2018/4/1.
 */
public class TicketSellDemo {
    private static int ticket = 100;

    public static void main(String[] args) {
        TicketSale sale = new TicketSale(ticket);
        new Thread(sale).start();
        new Thread(sale).start();

    }

    static class TicketSale implements Runnable {
        private static int tickets;

        public TicketSale(int tickets) {
            this.tickets = tickets;
        }
         /*同步方法，写在run方法外面被调用*/
        private synchronized void sellTicket() {
            if (tickets > 0) {
                System.out.println(Thread.currentThread().getName() + " is selling the " + (tickets--) + "th ticket");
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    /*模仿网络延迟，必须要考虑的*/
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sellTicket();
            }
        }
    }
}
