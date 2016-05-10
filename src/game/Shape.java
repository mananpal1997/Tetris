package game;

import java.util.Random;
import java.lang.Math;


public class Shape
{
    enum Type_of_shapes { NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape };

    private Type_of_shapes piece_shape;
    private int coords[][];
    private int[][][] coords_table;

    public Shape()
    {
        coords = new int[4][2];
        set_shape(Type_of_shapes.NoShape);
    }

    public void set_shape(Type_of_shapes shape)
    {
         coords_table = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++)
        {
            for (int j = 0; j < 2; ++j)
                coords[i][j] = coords_table[shape.ordinal()][i][j];
        }
        piece_shape = shape;

    }

    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Type_of_shapes get_shape()  { return piece_shape; }

    public void set_random_shape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Type_of_shapes[] values = Type_of_shapes.values(); 
        set_shape(values[x]);
    }

    public int minX()
    {
      int m = coords[0][0];
      for (int i=0; i < 4; i++)
          m = Math.min(m, coords[i][0]);
      
      return m;
    }


    public int minY() 
    {
      int m = coords[0][1];
      for (int i=0; i < 4; i++)
          m = Math.min(m, coords[i][1]);
      
      return m;
    }

    public Shape rotate_left() 
    {
        if (piece_shape == Type_of_shapes.SquareShape)
            return this;

        Shape result = new Shape();
        result.piece_shape = piece_shape;

        for (int i = 0; i < 4; ++i)
        {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Shape rotate_right()
    {
        if (piece_shape == Type_of_shapes.SquareShape)
            return this;

        Shape result = new Shape();
        result.piece_shape = piece_shape;

        for (int i = 0; i < 4; ++i)
        {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}