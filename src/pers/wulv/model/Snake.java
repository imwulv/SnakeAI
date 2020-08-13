package pers.wulv.model;

import pers.wulv.ai.NeuralNet;
import pers.wulv.ai.math.IActivationFunction;
import pers.wulv.ai.math.Linear;
import pers.wulv.ai.math.RandomNumberGenerator;
import pers.wulv.ai.math.Sigmoid;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Snake {
    public String snakeName ;
    public LinkedList<SnakeNode> snakeNodes = new LinkedList<SnakeNode>();
    //地图数据
    int[][] orginMapList;
    public Food food = new Food();
    public boolean isStarted = false;
    public boolean isFaild = false;
    public int score = 0;
    public int lifeTime =0;
    int deathLifeNum = 200;
    String direction = "R";
    int directionNum = 1;
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

    NeuralNet nn = new NeuralNet(9,4);


    public void reset(){
        snakeNodes.clear();
        this.direction = "R";
        this.isFaild = false;
        this.isStarted = true;
        this.lifeTime = 0;
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


        IActivationFunction[] hiddenAcFnc = { new Sigmoid(1.0) } ;
        Linear outputAcFnc = new Linear(1.0);
        System.out.println("Creating Neural Netword...");
        nn.reset();
        nn.setInputLayer(9);
        nn.addHiddenLayer(10);
        nn.addHiddenLayer(10);
        nn.setOutputLayer(4);

        System.out.println("Neural Network Network...");


    }
    public Snake(String snakeName,int[][] orginMapList,Color snakecolor,Color foodColor) {
        this.snakeName = snakeName;
        this.orginMapList = orginMapList;
        this.snakeColor = snakecolor;
        this.foodColor = foodColor;
        reset();
    }

    public synchronized void calc(){
        if (!isFaild && isStarted) {

            switch (direction) {
                case "U":
                    directionNum = 1;
                    break;
                case "D":
                    directionNum = 1;
                    break;
                case "L":
                    directionNum = 1;
                    break;
                case "R":
                    directionNum = 1;
                    break;
            }

            double [] neuralInput = {Double.valueOf(distanceU()),
                    Double.valueOf(distanceD()),
                    Double.valueOf(distanceL()),
                    Double.valueOf(distanceR()),
                    Double.valueOf(snakeNodes.getFirst().x),
                    Double.valueOf(snakeNodes.getFirst().y),
                    Double.valueOf(food.x),
                    Double.valueOf(food.y),
                    Double.valueOf(directionNum)};
            nn.setInputs(neuralInput);
            nn.calc();
            setDirectionByNeuralNet();
        }
    }


    private void setDirectionByNeuralNet(){
        int flag = 0;
        System.out.println("begin");
        for (int i = 0; i < nn.getOutputs().length ; i++) {

            System.out.println(i+":"+nn.getOutputs()[i]);
            if(i + 1 == nn.getOutputs().length){break;}
            flag = nn.getOutputs()[i] > nn.getOutputs()[i+1]? i : i+1;
        }
        System.out.println("end");
        switch (flag) {
                case 1:
                    settingDirection("U");
                    //System.out.println("U");
                    break;
                case 2:
                    settingDirection("D");
                    //System.out.println("D");
                    break;
                case 3:
                    settingDirection("L");
                    //System.out.println("L");
                    break;
                case 0:
                    settingDirection("R");
                    //System.out.println("R");
                    break;
        }
    }
    //距离上方障碍物距离
    private int distanceU(){
        int snakeHeadX = snakeNodes.getFirst().x;
        int snakeHeadY = snakeNodes.getFirst().y;
        int distance=0;
        for (int i = snakeHeadY; i >= 0 ; i--) {
            if (orginMapList[i][snakeHeadX] != 1){
                distance++;
            }
        }
        return distance;
    }
    //距离下方障碍物距离
    private int distanceD(){
        int snakeHeadX = snakeNodes.getFirst().x;
        int snakeHeadY = snakeNodes.getFirst().y;
        int distance=0;
        for (int i = snakeHeadY; i < orginMapList.length ; i++) {
            if (orginMapList[i][snakeHeadX] != 1){
                distance++;
            }
        }
        return distance;
    }
    //距离左方障碍物距离
    private int distanceL(){
        int snakeHeadX = snakeNodes.getFirst().x;
        int snakeHeadY = snakeNodes.getFirst().y;
        int distance=0;
        for (int i = snakeHeadX; i >= 0 ; i--) {
            if (orginMapList[snakeHeadY][i] != 1){
                distance++;
            }
        }
        return distance;
    }
    //距离右方障碍物距离
    private int distanceR(){
        int snakeHeadX = snakeNodes.getFirst().x;
        int snakeHeadY = snakeNodes.getFirst().y;
        int distance=0;
        for (int i = snakeHeadX; i < orginMapList[0].length ; i++) {
            if (orginMapList[snakeHeadY][i] != 1){
                distance++;
            }
        }
        return distance;
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
        }
        doMove();
    }

    private void doMove(){

        if(!isFaild && isStarted && lifeTime < deathLifeNum){
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
            if (lifeTime == deathLifeNum ){
                isFaild = true;
            }
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
