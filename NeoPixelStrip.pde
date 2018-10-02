class NeoPixelStrip {
  private List<NeoPixel> leds;
  public NeoPixelStrip(float sX, float sY, float sZ, float eX, float eY, float eZ, float ledPerMeter) {
    leds = new ArrayList<NeoPixel>();
    PVector direction = new PVector(eX-sX, eY-sY, eZ-sZ);
    int nLeds = (int) (ledPerMeter * (direction.mag()/100.0));
    direction.setMag(100.0/ledPerMeter);
    for (int i = 0; i < nLeds; i++) {
      leds.add(new NeoPixel(sX, sY, sZ));
      sX += direction.x;
      sY += direction.y;
      sZ += direction.z;
    }
  }

  public void draw() {
    for (NeoPixel p : leds) {
      p.draw();
    }
  }
  public void setAll(color c) {
    for (NeoPixel p : leds) {
      p.setColor(c);
    }
  }
  
  public void setColor(int i, color c){
    leds.get(i).setColor(c);
  }

  public void setColors(color[] colors) {
    for (int i = 0; i < colors.length && i < leds.size(); i++) {
      leds.get(i).setColor(colors[i]);
    }
  }
}
