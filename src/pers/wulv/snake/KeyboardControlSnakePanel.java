package pers.wulv.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class KeyboardControlSnakePanel extends JPanel implements KeyListener, ActionListener {

    protected final int rectLength = 10;
    int score = 0;
    //设置Snake
    protected Snake snake;
    protected Food food;
    //设置Snake的基础长度
    boolean isStarted;
    boolean isFaild;
    String direction = "R";


    /**
     * 必须设置读写切换标志，因为快速按键会导致蛇回头咬到尾部！
     * 比如 当前方向上UP
     * keyPressed快速点按的时候会将方向快速的切换成Left或者Right 再切换成Down
     * 当actionPerformed处理Snake移动时拿到了Down则处理Snake回头咬住尾部。
     * 别问为什么，老子搞半天才发现。
     */
    boolean directionSetFlag = true;

    int[][] orginMapList = {
            {1, 1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1, 1},

            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},

            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},

            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},

            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,1,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},

            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},
            {1, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1},

            {1, 1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1, 1}};

    Timer timer = new Timer(100, this);


    //构造函数
    public KeyboardControlSnakePanel() {
        this.setFocusable(true);
        init();
        this.addKeyListener(this);
        timer.start();
    }
    //初始化地图
    private void init() {
        score = 0;
        isStarted = false;
        isFaild = false;
        snake = new Snake();
        food = new Food();
        direction = "R";
        setFood();
    }

    //初始化蛇
    private void setFood(){
        Random r = new Random();

        int foodx = r.nextInt(60);
        int foody = r.nextInt(50);

        while(!available(foodx,foody)){
            foodx = r.nextInt(60);
            foody = r.nextInt(50);
        }
        food.x = foodx ;
        food.y = foody ;
    }

    //判断食物位置是否可用
    private boolean available(int x, int y){
        if (orginMapList[y][x] == 1){
            return false;
        }
        //如果重设在蛇身上
        for (SnakeNode snakeNode : snake.snakeNodes){
            if (x == snakeNode.x && y == snakeNode.y){
                return false;
            }
        }
        return true;
    }

    public void paint(Graphics g) {

        //绘制基础地图
        this.setBackground(Color.BLACK);
        g.setColor(Color.GRAY);
        for (int i = 0; i < orginMapList.length; i++) {
            for (int j = 0; j < orginMapList[i].length; j++) {
                switch (orginMapList[i][j]){
					//遮挡物，触之即死
                    case 1:
                        g.setColor(Color.GRAY);
                        break;
                    //可用空间
                    case 0:
                        g.setColor( Color.lightGray);
                        break;
                }
                g.fillRect(rectLength * j, rectLength * i, rectLength, rectLength);
            }
        }
        //绘制Snake
        for (SnakeNode snakeNode : snake.snakeNodes) {
            g.setColor(Color.PINK);
            g.fillRect(rectLength * snakeNode.x, rectLength * snakeNode.y, rectLength, rectLength);
        }
        //绘制食物
        g.setColor(Color.orange);
        g.fillRect(rectLength*food.x, rectLength*food.y, rectLength, rectLength);
        //绘制分数信息
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",Font.BOLD,30));
        g.drawString("Score: "+ score, 620, 30);

        //绘制提示信息
        if(!isStarted){
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial",Font.BOLD,30));
            g.drawString("Press Space to Start or Pause", 230, 250);
        }

        if (isFaild) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial",Font.BOLD,30));
            g.drawString("Game Over,Press Space to Start", 230, 250);
        }
    }

    public void settingDirection(String paramDirection) {
        if (!direction.equals("D") && directionSetFlag){
            direction = "U";
            directionSetFlag = false;
        } else if (!direction.equals("U") && directionSetFlag){
            direction = "D";
            directionSetFlag = false;
        } else if (!direction.equals("R") && directionSetFlag){
            direction = "L";
            directionSetFlag = false;
        } else if (!direction.equals("L") && directionSetFlag){
            direction = "R";
            directionSetFlag = false;
        }
        doMove();
    }
    private void doMove(){
        if(direction.equals("R")){
            snake.moveRight();
            directionSetFlag = true;
        }else if(direction.equals("L")){
            snake.moveLeft();
            directionSetFlag = true;
        }else if(direction.equals("U")){
            snake.moveUp();
            directionSetFlag = true;
        }else if(direction.equals("D")){
            snake.moveDown();
            directionSetFlag = true;
        }
        int snakeHeadX = snake.snakeNodes.getFirst().x;
        int snakeHeadY = snake.snakeNodes.getFirst().y;

        //如果吃到食物
        if(snakeHeadX == food.x && snakeHeadY == food.y){
            score++;
            setFood();
        } else {
            //如果没有吃到食物，尾部消失，相当于整体前移
            snake.snakeNodes.removeLast();
        }

        //触壁死亡
        //地图的二维下标要倒转，因为蛇的x轴对应的是地图的第二个下标，y轴对应的是第一个下标
        if(orginMapList[snakeHeadY][snakeHeadX] == 1 ){

            System.out.println("！！！触壁死亡！！！");
            System.out.println("orginMapList[" + snakeHeadY +"]["+snakeHeadX+"]");
            isFaild = true;
        }

        //如果撞到自己
        for (int i = 1; i < snake.snakeNodes.size() ; i++) {
            if (snakeHeadX == snake.snakeNodes.get(i).x && snakeHeadY == snake.snakeNodes.get(i).y){
                System.out.println("！！！自杀死亡！！！");
                isFaild = true;
                break;
            }
        }
        if (isFaild){
            System.out.println(snake);
            System.out.println("！！！#######！！！");
        }
        repaint();

    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        /**
         * 0 = KeyEvent.VK_SPACE
         * 1 = KeyEvent.VK_UP
         * 2 = KeyEvent.VK_DOWN
         * 3 = KeyEvent.VK_LEFT
         * 4 = KeyEvent.VK_RIGHT
         */
         if(keyCode == KeyEvent.VK_SPACE){
            if(isFaild){
                init();
            } else {
                isStarted = !isStarted;
            }
         } else if (keyCode == KeyEvent.VK_UP && !direction.equals("D") && directionSetFlag){
             direction = "U";
             directionSetFlag = false;
         } else if (keyCode == KeyEvent.VK_DOWN && !direction.equals("U") && directionSetFlag){
             direction = "D";
             directionSetFlag = false;
         } else if (keyCode == KeyEvent.VK_LEFT && !direction.equals("R") && directionSetFlag){
             direction = "L";
             directionSetFlag = false;
         } else if (keyCode == KeyEvent.VK_RIGHT && !direction.equals("L") && directionSetFlag){
             direction = "R";
             directionSetFlag = false;
         }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        /**
         * 0 = KeyEvent.VK_SPACE
         * 1 = KeyEvent.VK_UP
         * 2 = KeyEvent.VK_DOWN
         * 3 = KeyEvent.VK_LEFT
         * 4 = KeyEvent.VK_RIGHT
         */
        if(isStarted && !isFaild){
            doMove();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

//食物类
class Food {
    int x;
    int y;
}
//Snake类
class Snake {
    LinkedList<SnakeNode> snakeNodes = new LinkedList<SnakeNode>();
    public Snake () {
        SnakeNode head = new SnakeNode();
        head.x = 10;
        head.y = 5;
        SnakeNode body = new SnakeNode();
        body.x = 9;
        body.y = 5;
        SnakeNode tail = new SnakeNode();
        tail.x = 8;
        tail.y = 5;
        snakeNodes.add(head);
        snakeNodes.add(body);
        snakeNodes.add(tail);
    }

    public void moveLeft(){
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x-1;
        tempSnakeNode.y = snakeNodes.getFirst().y;
        snakeNodes.addFirst(tempSnakeNode);
    }
    public void moveRight(){
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x+1;
        tempSnakeNode.y = snakeNodes.getFirst().y;
        snakeNodes.addFirst(tempSnakeNode);
    }
    public void moveUp(){
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x;
        tempSnakeNode.y = snakeNodes.getFirst().y-1;
        snakeNodes.addFirst(tempSnakeNode);
    }
    public void moveDown(){
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x;
        tempSnakeNode.y = snakeNodes.getFirst().y+1;
        snakeNodes.addFirst(tempSnakeNode);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(SnakeNode snakeNode : this.snakeNodes){
            sb.append("[");
            sb.append(snakeNode.x);
            sb.append(",");
            sb.append(snakeNode.y);
            sb.append("]<-");
        }
        return sb.toString();
    }
}
//Snake节点类
class SnakeNode {
    int x;
    int y;
}
