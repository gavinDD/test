package com.luoye.pintu;

import java.util.Random;

/**
 * 用来保存块数据的类
 * 注意：x对应的是col,y对应的row
 */
public class Board {
    private static final String TAG ="Board" ;
    private  int[][] array=null;
    private  int row=0;
    private  int col =0;

    //四个方向
   private int[][] dir={
        {0,1},//下
        {1,0},//右
        {0,-1},//上
        {-1,0}//左
    };

    private void createIntegerArray(int row,int col)//用数字0到N-1(N为块的数量)表示每一块，并用二维数组存储他们。
    {
        array=new int[row][col];
        int idx=0;
        for(int i=0;i<row;i++)
        for(int j=0;j<col;j++)
            array[i][j]=idx++;
    }

    /**
     * 移动块的位置
     * @return 新的位置，错误返回new Point(-1,-1);
     */
    private Point move(int srcX,int srcY,int xOffset,int yOffset)
    {
        int x=srcX+xOffset;
        int y=srcY+yOffset;
        if(x<0||y<0||x>=col||y>=row)
            return new Point(-1,-1);

        int temp=array[y][x];
        array[y][x]=array[srcY][srcX];
        array[srcY][srcX]=temp;

        return new Point(x,y);
    }

    /**
     * 得到下一个可以移动的位置
     */
    private  Point getNextPoint(Point src)
    {
        Random rd=new Random();
        int idx=rd.nextInt(4);//产生0~3的随机数
        int xOffset=dir[idx][0];
        int yOffset=dir[idx][1];
        Point newPoint=move(src.getX(),src.getY(),xOffset,yOffset);
        if(newPoint.getX()!=-1&&newPoint.getY()!=-1) {
            return newPoint;
        }

       return getNextPoint(src);
    }

    /**
     * 生成拼图数据
     */
    public int[][] createRandomBoard(int row,int col)
    {
        this.row=row;
        this.col=col;
        createIntegerArray(row,col);//初始化拼图数据
        int count=0;
        Point tempPoint=new Point(col-1,row-1);//获得下个点，并更新空白块位置
        Random rd=new Random();
        int num=rd.nextInt(1000)+20;//产生20~119的随机数，表示重复的次数
        while (count<num)
       {
           tempPoint=getNextPoint(tempPoint);
           count++;
        }
        return  array;
    }
    /**
     * 判断是否拼图成功
     */
    public boolean isSuccess(int[][] arr)
    {
        int idx=0;

        for(int i=0;i<arr.length;i++)
        {
            for(int j=0;j<arr[i].length&&idx<row*col-1;j++)
            {

                if(arr[idx/row][idx%col]>arr[(idx+1)/row][(idx+1)%col])
                {

                    return false;

                }
                idx++;
            }

        }
        return  true;
    }
}
