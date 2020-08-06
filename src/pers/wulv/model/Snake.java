package pers.wulv.model;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Snake {
    public LinkedList<SnakeNode> snakeNodes = new LinkedList<SnakeNode>();
    //地图数据
    int[][] orginMapList;
    public Food food = new Food();
    boolean isStarted = true;
    boolean isFaild = false;
    public int score = 0;
    int lifeTime =0;
    String direction = "R";
    public Color snakeColor ;
    public Color foodColor;
    /**
     * 方向读写切换标志
     * 必须设置读写切换标志，因为快速按键会导致蛇回头咬到尾部！
     * 比如：当前方向上UP，持续快速点按的时触发keyPressed(),会将方向快速的切换成Left或者Right，再切换成Down，以此和UP冲突。
     * doMove()根据方向处理Snake移动时，当前方向是UP，拿到了Down，则Snake回头咬住尾部。
     * 别问为什么，老子搞半天才发现。我佛了。
     */
    boolean directionSetFlag = true;


    public Snake(int[][] orginMapList,Color snakecolor,Color foodColor) {
        this.orginMapList = orginMapList;
        this.snakeColor = snakecolor;
        this.foodColor = foodColor;

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

        setFood();

    }

    public synchronized void settingDirection(String paramDirection) {
        if (!isFaild && isStarted) {
            if (paramDirection.equals("U") && !direction.equals("D") && directionSetFlag) {
                direction = "U";
                directionSetFlag = false;
            } else if (paramDirection.equals("D") && !direction.equals("U") && directionSetFlag) {
                direction = "D";
                directionSetFlag = false;
            } else if (paramDirection.equals("L") && !direction.equals("R") && directionSetFlag) {
                direction = "L";
                directionSetFlag = false;
            } else if (paramDirection.equals("R") && !direction.equals("L") && directionSetFlag) {
                direction = "R";
                directionSetFlag = false;
            }
            doMove();
        }
    }

    private void doMove(){
        if(!isFaild && isStarted && lifeTime < 200){

            if(direction.equals("R")){
                moveRight();
                directionSetFlag = true;
            }else if(direction.equals("L")){
                moveLeft();
                directionSetFlag = true;
            }else if(direction.equals("U")){
                moveUp();
                directionSetFlag = true;
            }else if(direction.equals("D")){
                moveDown();
                directionSetFlag = true;
            }

            int snakeHeadX = snakeNodes.getFirst().x;
            int snakeHeadY = snakeNodes.getFirst().y;

            //偶尔会出现为负的情况，直接判负抛出。
            if (snakeHeadX < 0 || snakeHeadY < 0 ){
                isFaild = true;
                return;
            }

            //如果吃到食物
            if(snakeHeadX == food.x && snakeHeadY == food.y){
                score++;
                lifeTime = 0;
                setFood();
            } else {
                //如果没有吃到食物，尾部消失，相当于整体前移
                snakeNodes.removeLast();
            }

            //触壁死亡
            //地图的二维下标要倒转，因为蛇的x轴对应的是地图的第二个下标，y轴对应的是第一个下标
            if(orginMapList[snakeHeadY][snakeHeadX] == 1 ){
                System.out.println("┌─触壁死亡─┐");
                //输出触壁位置
                System.out.println("触壁位置为：[" + snakeHeadY +"]["+snakeHeadX+"]");
                isFaild = true;
            }

            //如果撞到自己
            for (int i = 1; i < snakeNodes.size() ; i++) {
                if (snakeHeadX == snakeNodes.get(i).x && snakeHeadY == snakeNodes.get(i).y){
                    System.out.println("┌─自杀死亡─┐");
                    isFaild = true;
                    break;
                }
            }

            //蛇死亡时输出蛇的节点信息，以便调试。
            if (isFaild){
                System.out.println(toString());
                System.out.println("└─死亡信息─┘");
            }
            lifeTime++;
        }
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
        for (SnakeNode snakeNode : snakeNodes){
            if (x == snakeNode.x && y == snakeNode.y){
                return false;
            }
        }
        return true;
    }

    public void moveLeft() {
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x - 1;
        tempSnakeNode.y = snakeNodes.getFirst().y;
        snakeNodes.addFirst(tempSnakeNode);
    }

    public void moveRight() {
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x + 1;
        tempSnakeNode.y = snakeNodes.getFirst().y;
        snakeNodes.addFirst(tempSnakeNode);
    }

    public void moveUp() {
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x;
        tempSnakeNode.y = snakeNodes.getFirst().y - 1;
        snakeNodes.addFirst(tempSnakeNode);
    }

    public void moveDown() {
        SnakeNode tempSnakeNode = new SnakeNode();
        tempSnakeNode.x = snakeNodes.getFirst().x;
        tempSnakeNode.y = snakeNodes.getFirst().y + 1;
        snakeNodes.addFirst(tempSnakeNode);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("⪽");
        for (SnakeNode snakeNode : this.snakeNodes) {
            sb.append("(");
            sb.append(snakeNode.x);
            sb.append(",");
            sb.append(snakeNode.y);
            sb.append(")");
        }
        sb.append("⪾");
        return sb.toString();
    }
}
