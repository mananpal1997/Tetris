package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.Shape.Type_of_shapes;

public class Board extends JPanel implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int board_width = 10;
    final int board_height = 22;

    Timer timer;
    boolean falling_finished = false, started = false, paused = false;
    int num_lines_removed = 0, curX = 0, curY = 0;
    JLabel status_bar;
    Shape cur_piece;
    Type_of_shapes[] board;

    public Board(Tetris parent)
    {
    	setFocusable(true);
    	cur_piece = new Shape();
    	timer = new Timer(400, this);
    	timer.start(); 
    	
    	status_bar =  parent.getStatusBar();
    	board = new Type_of_shapes[board_width * board_height];
    	addKeyListener(new TAdapter());
    	clear_board();  
    }

    public void actionPerformed(ActionEvent e)
    {
        if (falling_finished)
        {
            falling_finished = false;
            new_piece();
        }
        else
        {
            one_line_down();
        }
    }


    int square_width() { return (int) getSize().getWidth() / board_width; }
    int square_height() { return (int) getSize().getHeight() / board_height; }
    Type_of_shapes shape_position(int x, int y) { return board[(y * board_width) + x]; }


    public void start()
    {
        if (paused)
            return;

        started = true;
        falling_finished = false;
        num_lines_removed = 0;
        clear_board();

        new_piece();
        timer.start();
    }

    private void pause()
    {
        if (!started)
            return;

        paused = !paused;
        if (paused)
        {
            timer.stop();
            status_bar.setText("paused");
        }
        else
        {
            timer.start();
            status_bar.setText(String.valueOf(num_lines_removed));
        }
        repaint();
    }

    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - board_height * square_height();


        for (int i = 0; i < board_height; ++i)
        {
            for (int j = 0; j < board_width; ++j)
            {
                Type_of_shapes shape = shape_position(j, board_height - i - 1);
                if (shape != Type_of_shapes.NoShape)
                    draw_square(g, 0 + j * square_width(), boardTop + i * square_height(), shape);
            }
        }

        if (cur_piece.get_shape() != Type_of_shapes.NoShape)
        {
            for (int i = 0; i < 4; ++i)
            {
                int x = curX + cur_piece.x(i);
                int y = curY - cur_piece.y(i);
                draw_square(g, 0 + x * square_width(), boardTop + (board_height - y - 1) * square_height(), cur_piece.get_shape());
            }
        }
    }

    private void drop_down()
    {
        int newY = curY;
        while (newY > 0)
        {
            if (!try_move(cur_piece, curX, newY - 1))
                break;
            newY--;
        }
        piece_dropped();
    }

    private void one_line_down()
    {
        if (!try_move(cur_piece, curX, curY - 1))
            piece_dropped();
    }


    private void clear_board()
    {
        for (int i = 0; i < board_height * board_width; ++i)
            board[i] = Type_of_shapes.NoShape;
    }

    private void piece_dropped()
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = curX + cur_piece.x(i);
            int y = curY - cur_piece.y(i);
            board[(y * board_width) + x] = cur_piece.get_shape();
        }

        remove_completed_lines();

        if (!falling_finished)
            new_piece();
    }

    private void new_piece()
    {
        cur_piece.set_random_shape();
        curX = board_width / 2 + 1;
        curY = board_height - 1 + cur_piece.minY();

        if (!try_move(cur_piece, curX, curY))
        {
        	cur_piece.set_shape(Type_of_shapes.NoShape);
            timer.stop();
            started = false;
            status_bar.setText("game over    "+"Your score is "+""+num_lines_removed);
            int n = JOptionPane.showConfirmDialog(null, "Reset game ?", "Message", 1);
            switch(n)
            {
            	case 0 : start();status_bar.setText("0");break;
            	case 1 : System.exit(0);break;
            	case 2 : System.exit(0);break;
            }
        }
    }

    private boolean try_move(Shape new_piece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = newX + new_piece.x(i);
            int y = newY - new_piece.y(i);
            if (x < 0 || x >= board_width || y < 0 || y >= board_height)
                return false;
            if (shape_position(x, y) != Type_of_shapes.NoShape)
                return false;
        }

        cur_piece = new_piece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void remove_completed_lines()
    {
        int num_completed_lines = 0;

        for (int i = board_height - 1; i >= 0; --i)
        {
            boolean line_completed = true;
            for (int j = 0; j < board_width; ++j)
            {
                if (shape_position(j, i) == Type_of_shapes.NoShape)
                {
                    line_completed = false;
                    break;
                }
            }

            if (line_completed)
            {
                ++num_completed_lines;
                for (int k = i; k < board_height - 1; ++k)
                {
                    for (int j = 0; j < board_width; ++j)
                         board[(k * board_width) + j] = shape_position(j, k + 1);
                }
            }
        }

        if (num_completed_lines > 0)
        {
            num_lines_removed += num_completed_lines;
            status_bar.setText(String.valueOf(num_lines_removed));
            falling_finished = true;
            cur_piece.set_shape(Type_of_shapes.NoShape);
            repaint();
        }
     }

    private void draw_square(Graphics g, int x, int y, Type_of_shapes shape)
    {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, square_width() - 2, square_height() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + square_height() - 1, x, y);
        g.drawLine(x, y, x + square_width() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + square_height() - 1, x + square_width() - 1, y + square_height() - 1);
        g.drawLine(x + square_width() - 1, y + square_height() - 1, x + square_width() - 1, y + 1);
    }

    class TAdapter extends KeyAdapter
    {
         public void keyPressed(KeyEvent e)
         {
             if (!started || cur_piece.get_shape() == Type_of_shapes.NoShape)
             {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P')
             {
                 pause();
                 return;
             }

             if (paused)
                 return;

             switch (keycode)
             {
	             case KeyEvent.VK_LEFT : try_move(cur_piece, curX - 1, curY);break;
	             case KeyEvent.VK_RIGHT : try_move(cur_piece, curX + 1, curY);break;
	             case KeyEvent.VK_DOWN : try_move(cur_piece.rotate_right(), curX, curY);break;
	             case KeyEvent.VK_UP : try_move(cur_piece.rotate_left(), curX, curY);break;
	             case KeyEvent.VK_SPACE : drop_down();break;
	             case KeyEvent.VK_ESCAPE : timer.stop();JOptionPane.showMessageDialog(null, "Score : " + ""+num_lines_removed);System.exit(0);break;
	             case KeyEvent.VK_R : clear_board();num_lines_removed = 0;status_bar.setText("0");break;
	             case KeyEvent.VK_D : one_line_down();break;
             }
         }
     }
}