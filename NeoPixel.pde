class NeoPixel {
  private color c;
  public final float x, y, z;
  public NeoPixel(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setColor(color c) {
    this.c = c;
  }

  public void draw() {
    pushMatrix();
    translate(x, y, z);
    fill(c);
    noStroke();
    sphere(2);
    popMatrix();
  }
}
