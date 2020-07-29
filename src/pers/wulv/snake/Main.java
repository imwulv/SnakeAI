package pers.wulv.snake;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                SnakeFrame snakeFrame = null;
                try {
                    snakeFrame = new SnakeFrame();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                snakeFrame.setVisible(true);
            }
        });
    }
}
class SnakeFrame extends JFrame {
    KeyboardControlSnakePanel kcSnakePanel = new KeyboardControlSnakePanel();
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
