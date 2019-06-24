/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thepuzzlegame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import static thepuzzlegame.ThePuzzleGame.buttonList;
import static thepuzzlegame.ThePuzzleGame.VerifyMove;
/**
 *
 * @author Yurtsever
 */
public class ButtonsForPuzzle extends JButton {

    public static int caseFlag = 0;
    public static int buttonOnMemory = 0;
    public static int currentButton = -1;

    public ButtonsForPuzzle() {
        ButtonActions();
    }

    private void ButtonActions() {

        setBorder(BorderFactory.createLineBorder(Color.white, 2));
        
        addMouseListener(new MouseAdapter() {           
            @Override 
            public void mouseEntered(MouseEvent evt) {
                // // parca dogruysa disabled olacak, disabled icon var, parca dogru yerde old. anlasilmasi adina kirmizi cerceve cekilir.
                if(isEnabled())
                    setBorder(BorderFactory.createLineBorder(Color.green, 2));
                }
            @Override
            public void mouseExited(MouseEvent e) {
                // parca dogruysa disabled olacak, disabled icon var, parca dogru yerde old. anlasilmasi adina kirmizi cerceve cekilir.
                if(isEnabled())
                    setBorder(BorderFactory.createLineBorder(Color.white, 2));
                }           
        });      
        
        addActionListener(new ActionListener() {           
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonsForPuzzle button = (ButtonsForPuzzle) e.getSource();
                currentButton = buttonList.indexOf(button);

                switch(caseFlag) {
                    case 0:
                    {
                        buttonOnMemory = currentButton;
                        caseFlag++;
                        break;
                    }
                    case 1:
                    {
                        // buttonOnMemory, secilen ikinci buton ile degisecegi icin, o butonun dizilis(order) numarasini kendisine aliyor.
                        buttonList.get(buttonOnMemory).putClientProperty("order", currentButton);                     
                        // secilen ikinci buton da, hafizadaki butonun order numarasini aliyor ve ardindan yer degisecekler.
                        buttonList.get(currentButton).putClientProperty("order", buttonOnMemory);                       
                        VerifyMove(buttonOnMemory, currentButton);
                        caseFlag = 0;
                        break;
                    }
                }                  
                //System.out.println("orj indexi: " + button.getClientProperty("index"));
                //System.out.println("suanki sirasi: " + button.getClientProperty("order") + "\n");          
                }
        });
    }
    
    
    
}

