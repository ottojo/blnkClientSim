import processing.core.PApplet;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.println;

class NeoClient implements Runnable {
    public String id;
    public String address;
    private int port;
    private final NeoPixelStrip s;
    private PApplet p;

    public NeoClient(PApplet a, String id, String address, NeoPixelStrip s) {
        this.p = a;
        this.id = id;
        this.address = address;
        this.s = s;
        int i = address.lastIndexOf(':');
        if (i > 0) {
            port = Integer.parseInt(address.substring(i + 1));
        }
        this.address = address.substring(0, i);
    }

    public void draw() {
        s.draw();
    }

    enum State {WAITING, WAITING_L1, WAITING_L2, RECEIVING}

    private void handle(List<Integer> message) {
        if (message.size() < 1)
            return;
        if (message.get(0) == 1) {
            int numLeds = (message.size() - 1) / 3;
            for (int i = 0; i < numLeds; i++) {
                s.setColor(i, p.color(message.get(1 + i), message.get(2 + i), message.get(3 + i)));
            }
        }
    }

    public void run() {
        println("Client " + id + " started");


        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            println("Could not create socket for " + id);
            e.printStackTrace();
            return;
        }

        while (socket.isBound()) {
            println("Socket " + id + " waiting.");
            Socket connected = null;
            try {
                connected = socket.accept();
            } catch (IOException e) {
                println("Socket " + id + " could not accept connection.");
                e.printStackTrace();
                continue;
            }
            InputStream inputStream = null;
            try {
                inputStream = connected.getInputStream();
            } catch (IOException e) {
                println("Socket " + id + " could not get input stream.");
                e.printStackTrace();
                continue;
            }
            State state = State.WAITING;

            int dataLength = 0;
            int l1 = 0;
            List<Integer> receivedData = new ArrayList<>();

            while (connected.isConnected()) {
                int inputByte = 0;
                try {
                    inputByte = inputStream.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    println("Socket " + id + " could not read.");
                    break;
                }
                if (inputByte == -1) {
                    break;
                }
                switch (state) {
                    case WAITING:
                        if (inputByte == 0xaf)
                            state = State.WAITING_L1;
                        break;
                    case WAITING_L1:
                        l1 = inputByte;
                        state = State.WAITING_L2;
                        break;
                    case WAITING_L2:
                        dataLength = (l1 << 8) | inputByte;
                        receivedData = new ArrayList<>();
                        state = State.RECEIVING;
                        break;
                    case RECEIVING:
                        receivedData.add(inputByte);
                        if (receivedData.size() >= dataLength) {
                            handle(receivedData);
                            state = State.WAITING;
                        }
                        break;
                }
            }
        }
    }
}