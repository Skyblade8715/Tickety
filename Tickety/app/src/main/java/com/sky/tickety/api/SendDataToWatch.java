package com.sky.tickety.api;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SendDataToWatch extends Thread {
    String path;
    String message;
    Context context;

    public SendDataToWatch(String p, String m, Context context) {
        path = p;
        message = m;
        this.context = context;
    }

    public void run() {
        Task<List<Node>> wearableList =
                Wearable.getNodeClient(context).getConnectedNodes();
        try {
            List<Node> nodes = Tasks.await(wearableList);
            for (Node node : nodes) {
                Task<Integer> sendMessageTask =
                        Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
                try {
                    Integer result = Tasks.await(sendMessageTask);
                } catch (ExecutionException | InterruptedException exception) {
                }
            }
        } catch (ExecutionException | InterruptedException exception) {
        }
    }
}
