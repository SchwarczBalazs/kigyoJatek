package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
    

	static final int KEPERNYO_SZELESSEG = 1300;
	static final int KEPERNYO_MAGASSAG = 750;
	static final int EGYSEG_MERET = 30;
	static final int JATEK_EGYSEGEK = (KEPERNYO_SZELESSEG*KEPERNYO_MAGASSAG)/(EGYSEG_MERET*EGYSEG_MERET);
	static final int KESLELTETES = 175;
	final int x[] = new int[JATEK_EGYSEGEK];
	final int y[] = new int[JATEK_EGYSEGEK];
	int testDarab = 6;
	int megevettAlma;
	int almaX;
	int almaY;
	char irany = 'R';
	boolean fut = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(KEPERNYO_SZELESSEG,KEPERNYO_MAGASSAG));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		ujAlma();
		fut = true;
		timer = new Timer(KESLELTETES,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(fut) {
                        // grid mátrix az átláthatóságért
			/* 
			for(int i=0;i<KEPERNYO_MAGASSAG/EGYSEG_MERET;i++) {
				g.drawLine(i*EGYSEG_MERET, 0, i*EGYSEG_MERET, KEPERNYO_MAGASSAG);
				g.drawLine(0, i*EGYSEG_MERET, KEPERNYO_SZELESSEG, i*EGYSEG_MERET);
			}
			*/
			g.setColor(Color.red);
			g.fillOval(almaX, almaY, EGYSEG_MERET, EGYSEG_MERET);
		
			for(int i = 0; i< testDarab;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], EGYSEG_MERET, EGYSEG_MERET);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); // RGB színe a kígyónak
					g.fillRect(x[i], y[i], EGYSEG_MERET, EGYSEG_MERET);
				}			
			}
                        //játék végén kiíró lófasz
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Pontszam: "+megevettAlma, (KEPERNYO_SZELESSEG - metrics.stringWidth("Pontszam: "+megevettAlma))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void ujAlma(){
		almaX = random.nextInt((int)(KEPERNYO_SZELESSEG/EGYSEG_MERET))*EGYSEG_MERET;
		almaY = random.nextInt((int)(KEPERNYO_MAGASSAG/EGYSEG_MERET))*EGYSEG_MERET;
	}
	public void move(){
		for(int i = testDarab;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(irany) {
		case 'U' -> y[0] = y[0] - EGYSEG_MERET;
		case 'D' -> y[0] = y[0] + EGYSEG_MERET;
		case 'L' -> x[0] = x[0] - EGYSEG_MERET;
		case 'R' -> x[0] = x[0] + EGYSEG_MERET;
		}
		
	}
	public void checkAlma() {
		if((x[0] == almaX) && (y[0] == almaY)) {
			testDarab++;
			megevettAlma++;
			ujAlma();
		}
	}
	public void checkUtkozesek() {
		//megnézi, hogy a fej ütközött-e a testtel
		for(int i = testDarab;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				fut = false;
			}
		}
		//megnézi, hogy a fej ütközött-e a bal borderrel
		if(x[0] < 0) {
			fut = false;
		}
		//megnézi, hogy a fej ütközött-e a jobb borderrel
		if(x[0] > KEPERNYO_SZELESSEG) {
			fut = false;
		}
		//megnézi, hogy a fej ütközött-e a felső borderrel
		if(y[0] < 0) {
			fut = false;
		}
		//megnézi, hogy a fej ütközött-e az alsó borderrel
		if(y[0] > KEPERNYO_MAGASSAG) {
			fut = false;
		}
		
		if(!fut) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Pontszám szöveg
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Pontszam: "+megevettAlma, (KEPERNYO_SZELESSEG - metrics1.stringWidth("Pontszam: "+megevettAlma))/2, g.getFont().getSize());
		//Vége a játéknak szöveg
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Vege a jateknak", (KEPERNYO_SZELESSEG - metrics2.stringWidth("Vege a jateknak"))/2, KEPERNYO_MAGASSAG/2);
	}
	@Override
        // lenyomott gomb
	public void actionPerformed(ActionEvent e) {
		
		if(fut) {
			move();
			checkAlma();
			checkUtkozesek();
		}
		repaint();
	}
	
        // Lenyomott billentyű értelmező
        
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT -> {
                            if(irany != 'R') {
                                irany = 'L';
                            }
                        }
			case KeyEvent.VK_RIGHT -> {
                            if(irany != 'L') {
                                irany = 'R';
                            }
                        }
			case KeyEvent.VK_UP -> {
                            if(irany != 'D') {
                                irany = 'U';
                            }
                        }
			case KeyEvent.VK_DOWN -> {
                            if(irany != 'U') {
                                irany = 'D';
                            }
                        }
			}
		}
	}
}