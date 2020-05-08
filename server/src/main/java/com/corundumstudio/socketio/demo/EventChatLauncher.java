package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class EventChatLauncher {

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                //在回调的时候，如果有耗时操作 需要另外开启一个线程 不然 会阻塞 nio 线程
                server.getBroadcastOperations().sendEvent("chatevent", data);
            }
        });

        server.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < 10000; j++) {

            for (int i = 0; i < 100; i++) {
                ChatObject data = new ChatObject();
                data.setMessage("lingdu" + i);
                data.setUserName("abc111");
                long begin = System.currentTimeMillis();
                //这边发送的时候不是 nio 线程，所以不会有阻塞情况
                server.getBroadcastOperations().sendEvent("chatevent" + (i % 2), data);

                /**
                 * event-index0.html
                 * event-index1.html
                 */
                System.out.println(System.currentTimeMillis() - begin);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

}
