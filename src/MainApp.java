import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends PApplet {


    PeasyCam cam;


// 1 = 1cm

    List<NeoClient> clientList = new ArrayList<NeoClient>();


    public static void main(String[] args) {
        PApplet.main("MainApp", args);
    }

    @Override
    public void settings() {
        super.settings();
        size(700, 700, P3D);

    }

    @Override
    public void setup() {

        JSONArray clients = loadJSONArray("clients.json");
        for (int i = 0; i < clients.size(); i++) {
            JSONObject client = clients.getJSONObject(i);
            clientList.add(new NeoClient(
                    this,
                    client.getString("id"),
                    client.getString("address"),
                    new NeoPixelStrip(this,
                            client.getJSONObject("startPosition").getFloat("x"),
                            client.getJSONObject("startPosition").getFloat("y"),
                            client.getJSONObject("startPosition").getFloat("z"),
                            client.getJSONObject("endPosition").getFloat("x"),
                            client.getJSONObject("endPosition").getFloat("y"),
                            client.getJSONObject("endPosition").getFloat("z"),
                            client.getFloat("pixelsPerMeter"))));
        }

        for (NeoClient c : clientList) {
            new Thread(c).start();
        }


        cam = new PeasyCam(this, 0, -180, 0, 1000);
        cam.setMinimumDistance(10);
        cam.setMaximumDistance(100000);
        //smooth(8);
        background(0);
        perspective(PI / 3.0f, (float) width / height, 10,100000);
    }

    @Override
    public void draw() {
        background(0);
        lights();

        // Boden
        pushMatrix();
        stroke(255);
        fill(100);
        rectMode(CENTER);
        rotateX(PI / 2);
        rect(0, 0, 5000, 5000);
        popMatrix();

        // Haus

        pushMatrix();
        translate(-1050, -300, -650);
        box(2100, 600, 1300);
        popMatrix();
        pushMatrix();
        translate(-400, -300, -1700);
        box(800, 600, 800);
        popMatrix();

        // NeoPixels
        for (NeoClient c : clientList) {
            c.draw();
        }
    }
}
