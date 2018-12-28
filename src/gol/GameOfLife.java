package gol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.ibm.icu.impl.duration.TimeUnit;

import java.awt.FlowLayout;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameOfLife extends JFrame {
	
	private JPanel contentPane;
	final int width = 100, height = 100;
    boolean[][] currentMove = new boolean[height][width], nextMove = new boolean[height][width];
    boolean play;
    Image offScreenImg;
    Graphics offScreenGraph;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameOfLife().setVisible(true);
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public GameOfLife() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 458);
		contentPane = new JPanel();
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int i = width * arg0.getX() / contentPane.getWidth();
				int j = height * arg0.getY() / contentPane.getHeight();
				if(SwingUtilities.isLeftMouseButton(arg0)) {
					currentMove[j][i] = true;
					
				}
				else {
					currentMove[j][i] = false;
				}
				reapin();
			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = width * arg0.getX() / contentPane.getWidth();
				int j = height * arg0.getY() / contentPane.getHeight();
				currentMove[j][i] = true;
				reapin();
			}
		});
		contentPane.setBackground(Color.GRAY);
		contentPane.setVisible(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				play = !play;
				if(play) btnNewButton.setText("Pause");
				else btnNewButton.setText("Play");
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		btnNewButton.setBounds(12, 373, 97, 25);
		contentPane.add(btnNewButton);
		
		
		///////////////////////////
		/*
		offScreenImg = createImage(contentPane.getWidth(), contentPane.getHeight());
		if(offScreenImg != null) 
		{
			offScreenGraph = offScreenImg.getGraphics();
		}
		*/
		play = false;
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run() {
				if(play) {
					for(int i = 0;i<height;i++) {
						for(int j = 0;j<width;j++) {
							nextMove[i][j] = deicide(i, j);
						}
					}
					for (int i = 0;i < height; i++) {
						for(int j = 0; j< width;j++) {
							currentMove[i][j] = nextMove[i][j];
						}
					}
					reapin();
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 100);
		//reapin();
	}
	private void reapin() {
		
		offScreenImg = createImage(contentPane.getWidth(), contentPane.getHeight());
		offScreenGraph = offScreenImg.getGraphics();
		
		offScreenGraph.setColor(contentPane.getBackground());
		offScreenGraph.fillRect(0, 0, contentPane.getWidth(), contentPane.getHeight());
		
		offScreenGraph.setColor(Color.BLACK);
		for (int i = 0;i<height;i++) {
			int y = i * contentPane.getHeight()/height;
			
			offScreenGraph.drawLine(0, y, contentPane.getWidth(), y);
		}
		for (int j = 0;j<width;j++) {
			
			int x = j * contentPane.getWidth()/width;
			offScreenGraph.drawLine(x, 0, x, contentPane.getHeight());
		}
		for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width; j++){
                if(currentMove[i][j]){
                    offScreenGraph.setColor(Color.YELLOW);
                    int x = j * contentPane.getWidth()/width;
                    int y = i * contentPane.getHeight()/height;
                    offScreenGraph.fillRect(x, y, contentPane.getWidth()/width, contentPane.getHeight()/height);
                }
            }
        }
		contentPane.getGraphics().drawImage(offScreenImg, 0, 0, contentPane);
	}
	
	private boolean deicide(int i, int j) 
	{
		int neighbors = 0;
		if(j > 0) {
			if(currentMove[i][j-1])  neighbors++;
			if(i > 0 && currentMove[i][j]) neighbors++;
			if(i<height-1 && currentMove[i+1][j-1]) neighbors++;
		}
		if(j < width-1){
            if(currentMove[i][j+1]) neighbors++;
            if(i>0) if(currentMove[i-1][j+1]) neighbors++;
            if(i<height-1) if(currentMove[i+1][j+1]) neighbors++;
        }
		if(i>0) if(currentMove[i-1][j]) neighbors++;
        if(i<height-1) if(currentMove[i+1][j]) neighbors++;
        if(neighbors == 3) return true;
        if(currentMove[i][j] && neighbors == 2) return true;
        return false;
	}
}
