class NeoClient implements Runnable {
  public final String id;
  public final String address;
  private final NeoPixelStrip s;
  private Server server;

  public NeoClient(PApplet a, String id, String address, NeoPixelStrip s) {
    this.id = id;
    this.address = address;
    this.s = s;
    int port = 0;
    int i = address.lastIndexOf(':');
    if (i > 0) {
      port = Integer.parseInt(address.substring(i+1));
    }
    server = new Server(a, port);
  }

  public void draw() {
    s.draw();
  }

  public void run() {
    println("Client "+ id + " started");
    while (true) {
      Client c = server.available();
      if (c != null) {
        while (c.available() > 0) {
          while (c.read() != 0xaf) {
          }
          println("Client "+ id + " receiving");
          int lh= c.read();
          println("lh "+lh);
          int receiveLength = lh << 8; // Length high bye
          int ll= c.read();
          println("ll "+ll);
          receiveLength |= ll;         // Length low byte
          char[] message = new char[receiveLength+1];
          for (int i = 0; i<receiveLength+1; i++) {
            message[i] = c.readChar();
          }
          println("Client "+ id + " received message of length " + (receiveLength+1) + ":");
          for (int i = 0; i < receiveLength; i++) {
            print(" "+(int)message[i]);
          }
          println("");

          if (message[0]==1) {
            println("Client "+ id + " received \"SET PIXEL RANGE\"");
            for (int i = 0; i < receiveLength/3; i++) {
              s.setColor(i, color(message[3*i+1], message[3*i+2], message[3*i+3]));
            }
          }
        }
      }
    }
  }
}
