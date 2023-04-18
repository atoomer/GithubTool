import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;  
import java.awt.event.*; 


public class Main {
  public static void main(String[] args) {
    
        JFrame frame = new JFrame("Hello");
        frame.setSize(800,600);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("java swing");
        label.setSize(100, 50);
        label.setLocation(300, 100);
        panel.add(label);

        //JButton
        //JTextField

        JButton button = new JButton("click");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setForeground(Color.magenta);
            }
        });

        button.setSize(200,200);
        button.setLocation(50,50);
        panel.add(button);

        frame.setContentPane(panel);
        frame.setVisible(true);
        
    }  
}
