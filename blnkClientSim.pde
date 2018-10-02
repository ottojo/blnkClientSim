import peasy.*;
import processing.net.*;
import java.util.List;
import java.net.SocketException;
PeasyCam cam;


// 1 = 1cm

List<NeoClient> clientList = new ArrayList<NeoClient>();

void setup() {

  JSONArray clients = loadJSONArray("clients.json");
  for (int i = 0; i < clients.size(); i++) {
    JSONObject client = clients.getJSONObject(i);
    clientList.add(new NeoClient(
      this, 
      client.getString("id"), 
      client.getString("address"), 
      new NeoPixelStrip(
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

  size(700, 700, P3D);
  cam = new PeasyCam(this, 0, -180, 0, 1000);
  cam.setMinimumDistance(10);
  cam.setMaximumDistance(10000);
  smooth(8);
  background(0);
}
void draw() {
  background(0);
  lights();

  pushMatrix();
  stroke(255);
  fill(100);
  rectMode(CENTER);
  rotateX(PI/2);
  rect(0, 0, 1000, 1000);
  popMatrix();

  for (NeoClient c : clientList) {
    c.draw();
  }
}
