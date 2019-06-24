/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thepuzzlegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Yurtsever
 */
public class ThePuzzleGame extends JFrame{
    
    private static javax.swing.JTextArea logField;
    private static javax.swing.JTextArea scoreBoard;
    private static javax.swing.JLabel scoreLabel;
    private javax.swing.JButton getImageButton;
    private javax.swing.JLabel numberOfMovesLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel highScoreLabel;
    private javax.swing.JLabel logLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;   
    private javax.swing.JButton suffleButton;
    public static String logs; 
    public static JPanel panel;
    public static ArrayList<ButtonsForPuzzle> buttonList;
    public static ArrayList<Player> playerList;
    static Player player;
    static boolean firstFlag;
    
    public ThePuzzleGame() {        
        InitPanel();
        InitPuzzleGUI();     
        InitComponents();
        ReadScoreBoard();       
    }
    
    private void InitPanel() {
        
        panel = new JPanel();
        buttonList = new ArrayList();
        playerList = new ArrayList();
        scoreBoard = new javax.swing.JTextArea();
        logField = new javax.swing.JTextArea();
        logField.setForeground(Color.BLUE);
        player = new Player();
        firstFlag = true;
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(4, 4, 0, 0));
        add(panel, BorderLayout.CENTER);
        
                logs = "Puzzle oyununa hoş geldiniz.\nDoğru yerleştirilen parçalar kırmızı çerçeve ile kaplanır." +
                        "\nBu noktadan sonra doğru parçaların hareketine izin verilmez.\nParça eşleştirme aşamasında" +  
                        " görüntüyü karıştırırsanız puanınız sıfırlanır.\nResim yükleyerek oyuna başlayabilirsiniz.\n";
                logField.setText(logs);
    }
    
    private void InitPuzzleGUI() {       
        int counter = 0;
        for (int i = 0; i<16; i++) {           
                try {
                    ButtonsForPuzzle buton = new ButtonsForPuzzle();
                    // parcalar karistiktan sonra, hangi sirada olduklarini belirten 'order' property'si de karisiyor. shuffle'dan sonra ismiyle ayni olacak.
                    buton.setName(Integer.toString(counter)); 
                    buton.putClientProperty("index", counter);
                    buton.putClientProperty("order", counter);
                    counter++;
                    BufferedImage img = ImageIO.read(new File("resources/grey.png"));                   
                    buton.setIcon( new javax.swing.ImageIcon(img));   
                    if (firstFlag)
                        buton.setEnabled(false);                    
                    buttonList.add(buton);
                    panel.add(buton);
                } catch (IOException ex) {
                    Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
                }                     
        }
        pack();
    }
    
    private void InitComponents() {
        
        this.setLocationRelativeTo(null);
        getImageButton = new javax.swing.JButton();
        suffleButton = new javax.swing.JButton();
        scoreLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        numberOfMovesLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        highScoreLabel = new javax.swing.JLabel();
        logLabel = new javax.swing.JLabel();
        suffleButton.setVisible(false);       
        
        getImageButton.setText("Resim Yükle");
        getImageButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            panel.removeAll();      
            buttonList.clear();           
            firstFlag = false;
            
            JFileChooser chooser = new JFileChooser();
        
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                        "JPG, PNG, BMP Images", "jpg", "gif", "bmp", "jpeg", "png");
            chooser.setFileFilter(filter);
            
            if(chooser.showOpenDialog(getImageButton) == JFileChooser.APPROVE_OPTION) {
                suffleButton.setVisible(true); // karistir butonu artik aktiftir
                System.out.println("Acilan dosya: " +
                chooser.getSelectedFile().getName());
                String pathToFile = chooser.getSelectedFile().getAbsolutePath();
                try {
                    SplitPuzzleImage(pathToFile);
                    logs += "Resim doğru bir şekilde yüklendi ve 16 eşit parçaya ayrıldı.\n";
                    logField.setText(logs);
                    InitPuzzleGUI();            
                    String userName = JOptionPane.showInputDialog(null, "Kullanıcı adınızı giriniz: ", "Puzzle Game", JOptionPane.INFORMATION_MESSAGE);
                    while (userName == null || userName.equals(""))
                         userName = JOptionPane.showInputDialog(null, "Lütfen bir kullanıcı adı giriniz: ", "Puzzle Game", JOptionPane.INFORMATION_MESSAGE);
                    player.setUserName(userName);                  
                    logs += "\t-> Parçaları karıştırarak oyuna başlayabilirsiniz.\n";
                    logField.setText(logs);
                } catch (IOException ex) {
                    Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
                        }    
                }
            }
        });

        suffleButton.setText("Parçaları Karıştır");
        suffleButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            int correctPieces = 0;
            //while (correctPieces == 16) {
                panel.removeAll();
            Collections.shuffle(buttonList);
            player.setScore(0);
            
        
            for (int i = 0; i<16; i++) {
                buttonList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, 2));
                BufferedImage img = null;
            try {
                img = ImageIO.read(new File("resources/"+ buttonList.get(i).getName() +".jpg"));
            } catch (IOException ex) {
                Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
            }
                buttonList.get(i).setIcon( new javax.swing.ImageIcon(img.getScaledInstance(220, 115, java.awt.Image.SCALE_SMOOTH)));
                
            // karistirma isleminden dolayi dizilis, adiyla ayni degilse, dizilisi (order) ayni yap.     
            if (!buttonList.get(i).getName().equals(i)) 
                buttonList.get(i).putClientProperty("order", i);
                       
            // Shuffle'dan sonra parcalari aktif et
            if (!buttonList.get(i).isEnabled())
                buttonList.get(i).setEnabled(true);
            
            panel.add(buttonList.get(i));                
            }
      
           
     
            
            while (correctPieces != 5) {
                for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 16; y++) {
                try {
                if ((CheckPiece((int)buttonList.get(x).getClientProperty("index"), y) == 0) && //hamle dogru mu?
                   ((int)buttonList.get(y).getClientProperty("order") == x)) {
                        correctPieces++;
                    buttonList.get(x).setEnabled(false);
                    buttonList.get(x).setDisabledIcon(buttonList.get(x).getIcon());
                    buttonList.get(x).setBorder(BorderFactory.createLineBorder(Color.red, 2));
                    break;
                    }
                } catch (IOException ex) {
                Logger.getLogger(ButtonsForPuzzle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }            
            }
                if (correctPieces != 0){
                logs += correctPieces + " parça doğru yerine yerleşti, " + correctPieces*6.25 + " puan. Eşleştirmeye başlanabilir.\n";
                player.setScore(player.getScore() + correctPieces*6.25);
                scoreLabel.setText(String.valueOf(player.getScore()));
            }
          
            else if (correctPieces == 16) {
                logs += "Tüm parçalar doğru yerleşti!\n";                
                player.setScore(100);
                logs += "\nTebrikler, " + player.getUserName() + "! Oyun bitti. \n\nSkorunuz: " + player.getScore();
                JOptionPane.showConfirmDialog(null, "Oyun bitti, " + player.getUserName() + "!\n\tSkorunuz: " + player.getScore(), "Puzzle Game", JOptionPane.DEFAULT_OPTION);
                WriteUserScore();
            }
     
            else {
                logs += "Hiçbir parça doğru yerleşmedi. Lütfen tekrar karıştırın.\n";
                    
           for (ButtonsForPuzzle gray : buttonList) {
                try {
                    BufferedImage img = ImageIO.read(new File("resources/grey.png"));
                    gray.setIcon( new javax.swing.ImageIcon(img.getScaledInstance(220, 115, java.awt.Image.SCALE_SMOOTH)));
                } catch (IOException ex) {
                    Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }           
        }              
        logField.setText(logs);       
        panel.validate(); 
                
            }
             
           //  if (correctPieces == 16) 
           //  {
          ///       System.out.println("TÜM PARÇALAR EŞLEŞTİ");
            //     break;
            // }
                 
        
              
            
                
            }
            
           // }
        });

        scoreLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        scoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Puan");

        logField.setColumns(20);
        logField.setFont(new java.awt.Font("Monospaced", 2, 13)); // NOI18N
        logField.setRows(5);
        jScrollPane2.setViewportView(logField);
        logField.getAccessibleContext().setAccessibleDescription("");

        scoreBoard.setColumns(20);
        scoreBoard.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        scoreBoard.setRows(5);
        jScrollPane1.setViewportView(scoreBoard);
        scoreBoard.getAccessibleContext().setAccessibleDescription("");

        numberOfMovesLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        numberOfMovesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberOfMovesLabel.setText("Hamle Sayısı");

        userNameLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        userNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userNameLabel.setText("Kullanıcı Adı");

        highScoreLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        highScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        highScoreLabel.setText("Puan");

        logLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        logLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logLabel.setText("Log");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suffleButton, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(getImageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(numberOfMovesLabel)
                        .addGap(32, 32, 32)
                        .addComponent(userNameLabel)
                        .addGap(34, 34, 34)
                        .addComponent(highScoreLabel)
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(logLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numberOfMovesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(userNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(highScoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(suffleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(getImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scoreLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );
        pack();
    }
    
    private void SplitPuzzleImage(String pathToFile) throws IOException {
        
        final BufferedImage source = ImageIO.read(new File(pathToFile));
        int counter = 0;
        int row = 4, col = 4;

        int pieceWidth = source.getWidth() / row;
        int pieceHeight = source.getHeight() / col;
        
        System.out.println("pieceWidth: " + pieceWidth + " pieceHeight: " + pieceHeight);
        
        // soldan saga pixel okuma ve subimage alma islemi
        int yCoord = 0;
        for (int x = 0; x < 4; x++) {
            int xCoord = 0;
            for (int y = 0; y < 4; y++) {
                ImageIO.write(source.getSubimage(xCoord, yCoord, pieceWidth, pieceHeight), "jpg", new File("resources/" + counter++ + ".jpg"));
                System.out.println("xCoord: " + xCoord + " yCoord: " + yCoord);
                xCoord += pieceWidth;
                }
            yCoord += pieceHeight;
        }
    }
 
    private static void UpdateButtons() {        
        panel.removeAll();
        
        for (ButtonsForPuzzle but : buttonList)
            panel.add(but);
        
        panel.validate();
    }
    
    public static double CheckPiece(int pieceCurrent, int index) throws IOException {
        
        BufferedImage currentPiece = ImageIO.read(new File("resources/" + pieceCurrent + ".jpg"));
        BufferedImage comparingPiece = ImageIO.read(new File("resources/" + index + ".jpg"));
            
        int currWidth = currentPiece.getWidth();
        int currHeight = currentPiece.getHeight();
            
        long difference = 0;
        for (int y = 0; y < currHeight; y = y+4) {        
            for (int x = 0; x < currWidth; x = x+4) {                   
                int rgbA = currentPiece.getRGB(x, y);
                int rgbB = comparingPiece.getRGB(x, y);
                int redA = (rgbA >> 16) & 0xff;
                int greenA = (rgbA >> 8) & 0xff;
                int blueA = (rgbA) & 0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB) & 0xff;
                difference += Math.abs(redA - redB);
                difference += Math.abs(greenA - greenB);
                difference += Math.abs(blueA - blueB);
                    }
            }   
        // Total number of red pixels = width * height
        // Total number of blue pixels = width * height
        // Total number of green pixels = width * height
        // So total number of pixels = width * height * 3
        double total_pixels = currWidth * currHeight * 3;
                
        double avg_different_pixels = difference /
                        total_pixels;
                
        // There are 255 values of pixels in total
        double percentage = (avg_different_pixels /
                255) * 100;
               
        return percentage;
    }
    
    public static void VerifyMove(int buttonOnMemory, int currentButton) {
        try {
            
            // secilen butonun konuldugu yer dogru ise calisir. DisableTrueButtons icerisinde, 
            if (((CheckPiece((int)buttonList.get(buttonOnMemory).getClientProperty("index"), currentButton) == 0) && //hamle dogru mu?
                ((int)buttonList.get(currentButton).getClientProperty("order") == buttonOnMemory))) {
                player.setScore(player.getScore() + 6.25);
                player.setMoves(player.getMoves() + 1);
                scoreLabel.setText(String.valueOf(player.getScore()));
                logs += "Birinci parça doğru bir şekilde yerleştirildi. 6.25 puan.\n";
    
                    if(DisableTrueButtons(buttonOnMemory, currentButton)) {
                        player.setScore(player.getScore() + 6.25);
                        scoreLabel.setText(String.valueOf(player.getScore()));
                        logs += "\t-->İkinci parça da doğru bir şekilde yerleştirildi. 6.25 puan.\n";                      
                    }                                  
                }
                                
            // secilen butonun konuldugu yer dogru olmayabilir. fakat yer degistirdigi diger buton, yeni geldigi yerin parcasi olabilir.
            else if ((CheckPiece( (int) buttonList.get(currentButton).getClientProperty("index"), (int) buttonList.get(currentButton).getClientProperty("order") )== 0)) {
                    buttonList.get(currentButton).setEnabled(false);
                    buttonList.get(currentButton).setDisabledIcon(buttonList.get(currentButton).getIcon());
                    buttonList.get(currentButton).setBorder(BorderFactory.createLineBorder(Color.red, 2));
                player.setScore(player.getScore() + 6.25);
                player.setMoves(player.getMoves() + 1);
                scoreLabel.setText(String.valueOf(player.getScore()));
                logs += "İkinci parça doğru bir şekilde yerleştirildi. 6.25 puan.\n";
                }
            
            else {
                player.setScore(player.getScore() - 6.25);
                player.setMoves(player.getMoves() + 1);
                logs += "Yanlış hamle, -6.25 puan.\n";
                scoreLabel.setText(String.valueOf(player.getScore()));            
              }
            
            } catch (IOException ex) {
                   Logger.getLogger(ButtonsForPuzzle.class.getName()).log(Level.SEVERE, null, ex);
                   }
        Collections.swap(buttonList, buttonOnMemory, currentButton);     
        logField.setText(logs);
        UpdateButtons();
        IsLastMove();
    }
    
    private static boolean DisableTrueButtons(int mainPiece, int matchedPiece) {
        
        buttonList.get(mainPiece).setEnabled(false);
        buttonList.get(mainPiece).setDisabledIcon(buttonList.get(mainPiece).getIcon());
        buttonList.get(mainPiece).setBorder(BorderFactory.createLineBorder(Color.red, 2));
        
        try {
            // eger yerine konulan dogru parca ile, yer degistirdigi parcanın yeri de dogruysa onu da disable ediyor.
            if ((CheckPiece((int)buttonList.get(matchedPiece).getClientProperty("index"), mainPiece) == 0) && //hamle dogru mu?
                    ((int)buttonList.get(mainPiece).getClientProperty("order") == matchedPiece)) {
                
                buttonList.get(matchedPiece).setEnabled(false);
                buttonList.get(matchedPiece).setDisabledIcon(buttonList.get(matchedPiece).getIcon());
                buttonList.get(matchedPiece).setBorder(BorderFactory.createLineBorder(Color.red, 2));
                return true;                
            }
        } catch (IOException ex) {
            Logger.getLogger(ButtonsForPuzzle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static void IsLastMove() {
        
        int disabledCounter = 0;
        for (ButtonsForPuzzle piece : buttonList) {
            if (piece.isEnabled())
                disabledCounter++;
        }
        if (disabledCounter == 0) {
            logs += "\nTebrikler, " + player.getUserName() + "! Oyun bitti. \n\nSkorunuz: " + player.getScore();          
            logField.setText(logs);
            WriteUserScore();
            JOptionPane.showConfirmDialog(null, "Oyun bitti, " + player.getUserName() + "!\n\tSkorunuz: " + player.getScore(), "Puzzle Game", JOptionPane.DEFAULT_OPTION);
        } 
    }
    
    public static void WriteUserScore() {
        
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("enyüksekskor.txt", true)));
            PrintWriter outLog = new PrintWriter(new BufferedWriter(new FileWriter("userLog.txt", false)));
            out.print(player.getMoves()+ "\t" + player.getUserName() + "\t" + player.getScore() + "\n");
            outLog.print(logs);
            out.close();
            outLog.close();            
            }
        catch (IOException e) {  }               
        playerList.clear();
        ReadScoreBoard();
    }
    
    public static void ReadScoreBoard() {

        try {
            File file = new File("enyüksekskor.txt");
            
            if (!file.exists())
                file.createNewFile();
            
            Scanner scanner = new Scanner(file);
            
            scanner.useLocale(Locale.US);// DOUBLE OKUMAK ICIN
            
            while (scanner.hasNext()) {
                
                Player p1 = new Player();
                
                if (scanner.hasNext()) {
                    p1.setMoves(scanner.nextInt());
                    p1.setUserName(scanner.next());
                    p1.setScore(scanner.nextDouble());
                    playerList.add(p1);
                }
            }
            
            Collections.sort(playerList, new Comparator<thepuzzlegame.Player>() {
                @Override public int compare(thepuzzlegame.Player p1, thepuzzlegame.Player p2) {
                    return  (int)(p2.getScore() - (p1.getScore()));
                }
            });
            
            String tempScoreBoard = "";
            
            for (thepuzzlegame.Player player1 : playerList) {
                tempScoreBoard += player1.getMoves() + "\t" + player1.getUserName() + "    " + player1.getScore() + "\n";
                scoreBoard.setText(tempScoreBoard);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThePuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThePuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThePuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThePuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThePuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThePuzzleGame jFrame = new ThePuzzleGame();
                jFrame.setVisible(true);
                jFrame.setSize(1110, 696);
                jFrame.setLocationRelativeTo(null);
                jFrame.setResizable(false);
                jFrame.setTitle("Puzzle Game");
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    
    
    
}
