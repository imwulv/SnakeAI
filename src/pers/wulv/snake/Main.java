package pers.wulv.snake;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static SnakeFrame snakeFrame = null;
    //public static SnakePanel kcSnakePanel = new SnakePanel();
    public static AISnakePanel kcSnakePanel = new AISnakePanel();
    Thread panelThread = new Thread(kcSnakePanel);
    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {

                try {
                    snakeFrame = new SnakeFrame(kcSnakePanel);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                snakeFrame.setVisible(true);
                kcSnakePanel.run();
            }
        });

        System.out.println("#####Snake Start#####");
    }
}
class SnakeFrame extends JFrame {

    //JSplitPane jSplitPane =new JSplitPane();
    //JPanel jPanel= new JPanel();
    JButton button = new JButton("Start AI");

    public SnakeFrame(AISnakePanel aiSnakePanel) throws AWTException {
        setBounds(150,100, 1000, 520);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //jPanel.setSize(100,40);
        aiSnakePanel.setSize(1000,520);
        aiSnakePanel.setLayout(null);
        button.setSize(100,40);
        setContentPane(aiSnakePanel);
        aiSnakePanel.add(button);
        button.setVisible(true);
        this.setVisible(true);
//        jSplitPane.setOneTouchExpandable(true);//让分割线显示出箭头
//        jSplitPane.setContinuousLayout(true);//操作箭头，重绘图形
//        jSplitPane.setDividerLocation(800);
//        jSplitPane.setLeftComponent(aiSnakePanel);//布局中添加组件 ，面板1
//        //jSplitPane.setRightComponent(jPanel);//添加面板2
        //setContentPane(jSplitPane);

        //jPanel.add(button);
    }
}
