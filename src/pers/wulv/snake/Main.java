package pers.wulv.snake;

import pers.wulv.ai.math.RandomNumberGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static SnakeFrame snakeFrame = null;
    //public static SnakePanel kcSnakePanel = new SnakePanel();
    public static AISnakePanel kcSnakePanel = new AISnakePanel();

    Thread panelThread = new Thread(kcSnakePanel);
    public static void main(String[] args){
        RandomNumberGenerator.seed=0;


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

    JButton button = new JButton("Start AI");


    public SnakeFrame(AISnakePanel aiSnakePanel) throws AWTException {
        setBounds(150,100, 1200, 520);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                aiSnakePanel.reset();
            }
        });
        aiSnakePanel.setSize(1200,520);
        aiSnakePanel.setLayout(null);
        button.setBounds(620,450,100,40);
        setContentPane(aiSnakePanel);
        aiSnakePanel.add(button);
        button.setVisible(true);
        this.setVisible(true);

    }
}
