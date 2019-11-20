package com.luoye.pintu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import android.os.Message;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

public class MainView extends View {
    private static final String TAG ="MainView" ;
    private  Context context;
    private  Bitmap back;
    private  Paint paint;
    private  int tileWidth;//每一块的宽
    private int tileHeight;//每一块的高
    private Bitmap[] bitmapTiles;//存储切割后的图片
    private int[][] dataTiles;
    private Board tilesBoard;
    private final int COL=4;//列，默认3列
    private final int ROW=4;//行，默认3行
    private int[][] dir={
            {-1,0},//左
            {0,-1},//上
            {1,0},//右
            {0,1}//下
            };
    private  boolean isSuccess;
    private boolean timeswitch = true;
    private int time;

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            if (timeswitch){
                    time++;
                    handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };



    public MainView(Context context)
    {
        super(context);
        this.context=context;
        paint=new Paint();
        paint.setAntiAlias(true);//抗锯齿
        init();
        startGame();
        handler.sendEmptyMessageDelayed(1,1000);

    }


    /**
     * 初始化
     */
    private  void init()
    {   //获取选择的图片
        ChooseActivity ca = new ChooseActivity();
        String s = ca.getData();

        //载入图像，并将图片切成块
        AssetManager assetManager= context.getAssets();
        try {
            InputStream assetInputStream=assetManager.open(s);
            Bitmap bitmap=BitmapFactory.decodeStream(assetInputStream);
            back=Bitmap.createScaledBitmap(bitmap,MainActivity.getScreenWidth(),MainActivity.getScreenHeight(),true);//设置拼图大小，设置为屏幕大小
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileWidth=back.getWidth()/COL;
        tileHeight=back.getHeight()/ROW;
        bitmapTiles =new Bitmap[COL*ROW];
        int idx=0;
        for(int i=0;i<ROW;i++)
        {
            for(int j=0;j<COL;j++)
            {
                bitmapTiles[idx++]=Bitmap.createBitmap(back,j*tileWidth,i*tileHeight,tileWidth,tileHeight);
            }
        }
    }

    /**
     * 开始游戏
     */
    private void startGame()
    {
        tilesBoard =new Board();
        dataTiles= tilesBoard.createRandomBoard(ROW,COL);
        isSuccess=false;
        invalidate();//使整个窗口客户区无效，此时就需要重绘，这个就会自动调用窗口类的OnPaint函数，OnPaint负责重绘窗口。视图类中就调用OnDraw函数，实际的重绘工作由OnPaint或者OnDraw来完成。
    }

    @Override
    //在屏幕上绘图
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);//设置canvas颜色为白色
        for(int i=0;i<ROW;i++) {
            for (int j = 0; j < COL; j++) {
                int idx=dataTiles[i][j];
                if(idx==ROW*COL-1&&!isSuccess)
                    continue;
                canvas.drawBitmap(bitmapTiles[idx],j*tileWidth,i*tileHeight,paint);
            }
        }
    }

    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     */
    private Point xyToIndex(int x,int y)
    {
        int extraX=x%tileWidth>0?1:0;
        int extraY=x%tileWidth>0?1:0;
        int col=x/tileWidth+extraX;
        int row=y/tileHeight+extraY;

        return new Point(col-1,row-1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {//	获取事件类型，ACTION_DOWN：初次接触到屏幕时触发
            Point point = xyToIndex((int) event.getX(), (int) event.getY());

            for(int i=0;i<dir.length;i++)
            {
                int newX=point.getX()+dir[i][0];
                int newY=point.getY()+dir[i][1];

                if(newX>=0&&newX<COL&&newY>=0&&newY<ROW){
                    if(dataTiles[newY][newX]==COL*ROW-1)
                    {
                        int temp=dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()]=dataTiles[newY][newX];
                        dataTiles[newY][newX]=temp;
                        invalidate();
                        if(tilesBoard.isSuccess(dataTiles)){
                            isSuccess=true;
                            timeswitch = false;// 成功后，停止计时
                            invalidate();
                            new AlertDialog.Builder(context)
                                    .setTitle("拼图成功")
                                    .setCancelable(false)
                                    .setMessage("恭喜你拼图成功,"+"耗时"+time+"秒")
                                    .setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startGame();
                                        }
                                    })
                                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(0);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        }
        return true;
    }

}
