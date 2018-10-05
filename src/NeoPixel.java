import processing.core.*;

class NeoPixel {
    private int c;
    public final PVector pos;
    private final PApplet p;

    public NeoPixel(PApplet p, PVector pos) {
        this.p = p;
        this.pos = pos;
    }

    public void setColor(int c) {
        this.c = c;
    }

    public synchronized void draw() {
        p.pushMatrix();
        p.translate(pos.x, pos.y, pos.z);
        p.fill(c);
        p.noStroke();
        p.sphere(2);
        p.popMatrix();
    }
}