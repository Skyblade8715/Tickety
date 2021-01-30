package com.sky.tickety;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SendMessageToPhone extends Thread {

    String path;
    String message;
    Context context;

    public SendMessageToPhone(String p, String m, Context context) {
        this.context = context;
        path = p;
        message = m;
    }

    public void run() {
        System.out.println(message);
        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(context).getConnectedNodes();
        try {
            List<Node> nodes = Tasks.await(nodeListTask);
            for (Node node : nodes) {
                Task<Integer> sendMessageTask =
                        Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
                try {
                    Integer result = Tasks.await(sendMessageTask);
                } catch (ExecutionException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (ExecutionException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}

