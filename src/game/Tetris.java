package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Tetris extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel statusbar;
	public Tetris()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/3-this.getSize().width/3, dim.height/3-this.getSize().height/3);
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(200, 400);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getStatusBar()
   {
       return statusbar;
   }

	public static void main(String[] args)
	{
		Object[] options = {"OK"};
		JOptionPane.showOptionDialog(null, "1. Press up and down arrow keys to rotate shape left and right respectively.\n\n2.Press left and right arrow keys to move the falling shape.\n\n3. Press space key to force down the falling shape quickly.\n\n4. Press d to drop down the shape quickly by one line.\n\n5. Press p to pause (or unpause).\n\n6. Press r to reset the board.\n\n7. Press escape key to exit.", "Instructions", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, options, "OK");
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    } 
}