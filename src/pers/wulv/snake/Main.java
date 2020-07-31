package pers.wulv.snake;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static SnakeFrame snakeFrame = null;
    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {

                try {
                    snakeFrame = new SnakeFrame();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                snakeFrame.setVisible(true);
            }
        });

        System.out.println("#####Snake Start#####");
//        Random r = new Random();
//        new Thread(()->{
//			while (true){
//                switch (r.nextInt(4)) {
//                    case 1:
//                        snakeFrame.kcSnakePanel.settingDirection("U");
//                        break;
//                    case 2:
//                        snakeFrame.kcSnakePanel.settingDirection("D");
//                        break;
//                    case 3:
//                        snakeFrame.kcSnakePanel.settingDirection("L");
//                        break;
//                    case 0:
//                        snakeFrame.kcSnakePanel.settingDirection("R");
//                        break;
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//		}).start();

    }
}
class SnakeFrame extends JFrame {
    public static SnakePanel kcSnakePanel = new SnakePanel();
    JSplitPane jSplitPane =new JSplitPane();
    JPanel jPanel= new JPanel();
    JButton button = new JButton("Start AI");

    public SnakeFrame() throws AWTException {
        setBounds(150,100, 900, 526);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jPanel.setSize(100,40);
        kcSnakePanel.setSize(800,526);
        button.setSize(100,40);
        jSplitPane.setOneTouchExpandable(true);//让分割线显示出箭头
        jSplitPane.setContinuousLayout(true);//操作箭头，重绘图形
        jSplitPane.setDividerLocation(800);
        jSplitPane.setLeftComponent(kcSnakePanel);//布局中添加组件 ，面板1
        jSplitPane.setRightComponent(jPanel);//添加面板2
        setContentPane(jSplitPane);
        jPanel.add(button);
    }
}
