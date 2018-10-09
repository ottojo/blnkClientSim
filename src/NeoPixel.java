import processing.core.PApplet;
import processing.core.PVector;

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
        p.translate(-100 * pos.y, -100 * pos.z, -100 * pos.x);
        p.fill(c);
        p.noStroke();
        p.sphere(2);
        p.popMatrix();
    }
}